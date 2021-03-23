package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents

// annotating DSL functions with @VaadinDsl will make Intellij mark the DSL functions in a special way
// which makes them stand out apart from the common functions, which is very nice.
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
public annotation class VaadinDsl

/**
 * When introducing extensions for your custom components, just call [init] in your extension function. For example:
 *
 * `fun HasComponents.shinyComponent(caption: String? = null, block: ShinyComponent.()->Unit = {}) = init(ShinyComponent(caption), block)`
 *
 * Adds [component] to receiver, see [HasComponents.add] for details.
 *
 * @param component the component to attach
 * @param block optional block to run over the component, allowing you to add children to the [component]
 */
@VaadinDsl
public fun <T : Component> (@VaadinDsl HasComponents).init(
    component: T,
    id: String?,
    block: T.() -> Unit = {}
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
    block: T.() -> Unit = {}
): T {
    add(component)
    component.block()
    return component
}
