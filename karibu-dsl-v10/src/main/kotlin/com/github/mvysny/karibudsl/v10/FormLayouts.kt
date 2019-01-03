package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Label

/**
 * Creates a [Form Layout](https://vaadin.com/elements/vaadin-form-layout). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).formLayout(block: (@VaadinDsl FormLayout).() -> Unit = {})
        = init(FormLayout(), block)

/**
 * Makes [addToLabel] public so that we can call it.
 */
internal class KFormItem : FormLayout.FormItem() {
    public override fun addToLabel(vararg components: Component) {
        super.addToLabel(*components)
    }
}

/**
 * Creates a form item inside of the [FormLayout], with an optional [label]. See [Form Layout](https://vaadin.com/elements/vaadin-form-layout)
 * documentation for more details.
 */
@VaadinDsl
fun (@VaadinDsl FormLayout).formItem(label: Component? = null, block: (@VaadinDsl FormLayout.FormItem).() -> Unit = {})
        = init<FormLayout.FormItem>(KFormItem()) {
    if (label != null) {
        (this as KFormItem).addToLabel(label)
    }
    block()
}

/**
 * Creates a form item inside of the [FormLayout], with an optional [label]. See [Form Layout](https://vaadin.com/elements/vaadin-form-layout)
 * documentation for more details.
 */
@VaadinDsl
fun (@VaadinDsl FormLayout).formItem(label: String, block: (@VaadinDsl FormLayout.FormItem).() -> Unit = {}) =
        formItem(Label(label), block)

/**
 * Builds a list of [com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep].
 */
class FormLayoutResponsiveSteps {
    internal val steps = mutableListOf<FormLayout.ResponsiveStep>()
    val aside = FormLayout.ResponsiveStep.LabelsPosition.ASIDE
    val top = FormLayout.ResponsiveStep.LabelsPosition.TOP
    /**
     * Adds a responsive step with the minimum width, given number of [columns] and [labelsPosition]. Example of use:
     * ```
     * "30em"(2, top)
     * ```
     * @receiver the minimum width defined as CSS format, e.g. "0px", "30em"
     */
    operator fun String.invoke(columns: Int, labelsPosition: FormLayout.ResponsiveStep.LabelsPosition? = null) {
        steps.add(FormLayout.ResponsiveStep(this, columns, labelsPosition))
    }
}

/**
 * Allows you to specify responsive steps in a much more condensed form:
 * ```
 * responsiveSteps { "0px"(1, top); "30em"(2, aside) }
 * ```
 */
fun (@VaadinDsl FormLayout).responsiveSteps(block: FormLayoutResponsiveSteps.()->Unit) {
    val steps = FormLayoutResponsiveSteps()
    steps.block()
    responsiveSteps = steps.steps
}
