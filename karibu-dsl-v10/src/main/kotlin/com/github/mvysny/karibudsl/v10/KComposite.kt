package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.dom.Element
import java.lang.UnsupportedOperationException

/**
 * Replaces Vaadin's UI auto-creation magic by explicit UI creation. You need to call [ui] to populate this composite with content:
 * * The recommended way is to call [ui] in the `init{}` block, or in the constructor
 * * Alternatively you can override [initContent] and call [ui] there.
 * For example:
 * ```kotlin
 * class ButtonBar : KComposite() {
 *   private val root = ui {
 *     // create the component UI here; maybe even attach very simple listeners here
 *     horizontalLayout {
 *       button("ok") {
 *         onLeftClick { okClicked() }
 *       }
 *       button("cancel") {
 *         onLeftClick { cancelClicked() }
 *       }
 *     }
 *   }
 *
 *   init {
 *     // perform any further initialization here
 *   }
 *
 *   // add listener methods here
 *   private fun okClicked() {}
 *   private fun cancelClicked() {}
 * }
 * ```
 */
abstract class KComposite : Composite<Component>() {
    private var root: Component? = null
    override fun initContent(): Component = checkNotNull(root) { "The content has not yet been initialized, please call the ui() function in the constructor" }

    /**
     * Initializes the UI of this composite.
     */
    protected fun <T> ui(block: HasComponents.()->T): T {
        check(root == null) { "The content has already been initialized!" }
        val dummy = object : HasComponents {
            override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
            override fun add(vararg components: Component) {
                require(components.size < 2) { "Too many components to add - composite can only host one! ${components.toList()}" }
                val component = components.firstOrNull() ?: return
                check(root == null) { "The content has already been initialized!" }
                root = component
            }
        }
        val component = dummy.block()
        checkNotNull(root) { "`block` must add exactly one component" }
        return component
    }
}
