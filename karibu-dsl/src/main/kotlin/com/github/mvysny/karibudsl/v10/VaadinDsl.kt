package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.dom.Element

// annotating DSL functions with @VaadinDsl will make Intellij mark the DSL functions in a special way
// which makes them stand out apart from the common functions, which is very nice.
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
public annotation class VaadinDsl

/**
 * When introducing extensions for your custom components, just call [init] in your extension function. For example:
 *
 * ```kotlin
 * fun HasComponents.shinyComponent(caption: String? = null, block: ShinyComponent.()->Unit = {}) =
 *   init(ShinyComponent(caption), block)
 * ```
 *
 * Adding the [VaadinDsl] annotation makes the function appear in different color in Intellij, and also
 * prevents you from calling a DSL function from a grandparent, but makes the function more complicated:
 *
 * ```kotlin
 * @VaadinDsl
 * fun (@VaadinDsl HasComponents).shinyComponent(caption: String? = null, block: (@VaadinDsl ShinyComponent).()->Unit = {}) =
 *   init(ShinyComponent(caption), block)
 * ```
 *
 * Adds [component] to `receiver` via [HasComponents.add] and runs [block] on the component,
 * allowing you to configure the component further.
 *
 * @param component the component to attach
 * @param block optional block to run over the component, allowing you to add children to the [component]
 */
@VaadinDsl
public fun <T : Component> (@VaadinDsl HasComponents).init(
    component: T,
    id: String?,
    block: (@VaadinDsl T).() -> Unit = {}
): T {
    if (id != null) {
        component.setId(id)
    }
    add(component)
    component.block()
    return component
}

@VaadinDsl
public fun <T : Component> (@VaadinDsl HasComponents).init(
    component: T,
    block: (@VaadinDsl T).() -> Unit = {}
): T {
    add(component)
    component.block()
    return component
}

/**
 * Adapter, which provides dummy [HasComponents] receiver
 * for some karibu-dsl methods accepting parameter block: (@VaadinDsl HasComponents).() -> Unit
 */
public abstract class DummyHasComponents : HasComponents {
    abstract override fun add(vararg components: Component) // forces you to override the right add() function. This one will be called by the DSL
    final override fun add(components: Collection<Component>) {
        add(*components.toTypedArray())
    }
    override fun getElement(): Element =
        throw UnsupportedOperationException("Not expected to be called")
}

/**
 * Runs DSL function in given [block], then returns the component produced by the DSL function:
 * ```kotlin
 * val vl = buildSingleComponentOrNull {
 *   verticalLayout {
 *     button("Hi!")
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun buildSingleComponentOrNull(block: (@VaadinDsl HasComponents).() -> Any?): Component? {
    var component: Component? = null
    object : DummyHasComponents() {
        override fun add(vararg components: Component) {
            require(components.size < 2) { "Too many components to add - this component can only host one! ${components.toList()}" }
            check(component == null) { "Too many components to add - this component can only host one!" }
            component = components.firstOrNull()
        }
    }.block()
    return component
}

/**
 * Runs DSL function in given [block], then returns the component produced by the DSL function:
 * ```kotlin
 * val vl = buildSingleComponentOrNull {
 *   verticalLayout {
 *     button("Hi!")
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun buildSingleComponent(block: (@VaadinDsl HasComponents).() -> Any?): Component {
    val component: Component? = buildSingleComponentOrNull(block)
    return checkNotNull(component) { "`block` must add exactly one component" }
}
