package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.HasSize
import com.vaadin.flow.dom.Element

/**
 * Replaces Vaadin's UI auto-creation magic by explicit UI creation. You need to call [ui] to populate this composite with content:
 * * The recommended way is to call [ui] in a field initializer; for example you can create a field named `root` and
 * call the `ui{}` there. See example below for details.
 * * Alternatively you can call [ui] in the `init{}` block, or in the constructor
 * * Alternatively you can override [initContent] and call [ui] there.
 * For example:
 * ```kotlin
 * class ButtonBar : KComposite() {
 *   private val root = ui {
 *     // create the component UI here; maybe even attach very simple listeners here
 *     horizontalLayout {
 *       button("ok") {
 *         onClick { okClicked() }
 *       }
 *       button("cancel") {
 *         onClick { cancelClicked() }
 *       }
 *     }
 *   }
 *
 *   init {
 *     // perform any further initialization here
 *   }
 *
 *   // listener methods here
 *   private fun okClicked() {}
 *   private fun cancelClicked() {}
 * }
 * ```
 * Implements [HasSize] since you almost always need to control the
 * component size when placing into a parent layout.
 * @param root You can optionally specify the content straight in the constructor if it is known upfront.
 * Defaults to `null`: in such case the content is expected to
 * be provided either via [ui] or [initContent].
 */
public abstract class KComposite(private var root: Component? = null) : Composite<Component>(), HasSize {
    override fun initContent(): Component = checkNotNull(root) {
        "The content has not yet been initialized, please call the ui() function in the constructor"
    }

    // prevent accidental override by marking functions final. It's especially important to
    // not to override the getContent() function since that will wreak havoc with Karibu-Testing
    final override fun getContent(): Component = super.getContent()
    final override fun getElement(): Element = super.getElement()

    /**
     * Initializes the UI of this composite. Returns the component created by the block, so that we can store the created
     * component in the `root` field and access it later on, as shown above.
     */
    protected fun <T: Component> ui(block: HasComponents.()->T): T {
        check(root == null) { "The content has already been initialized!" }
        val component = buildSingleComponent(block)
        root = component
        return component as T
    }
}
