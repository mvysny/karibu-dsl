package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributools.addClassNames2
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.NativeLabel

/**
 * A [FormLayout] which allows to specify [colspan] for its children:
 *
 * ```
 * formLayout {
 *   textArea {
 *     colspan = 2
 *   }
 * }
 * ```
 */
public class KFormLayout : FormLayout() {

    /**
     * Sets colspan for the child of this [FormLayout].
     */
    public inline var (@VaadinDsl Component).colspan: Int
        @JvmName("__getColspanInternal")
        get() = getColspan(this)
        @JvmName("__setColspanInternal")
        set(value) {
            setColspan(this, value)
        }
}

/**
 * Creates a [Form Layout](https://vaadin.com/elements/vaadin-form-layout). See the HTML Examples link for a list
 * of possible alternative themes for the layout.
 *
 * Call [responsiveSteps] to configure the layout for fluent resizing.
 * @param classNames optional additional class names, space-separated
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).formLayout(classNames: String = "", block: (KFormLayout).() -> Unit = {}): KFormLayout {
    val layout = KFormLayout()
    layout.addClassNames2(classNames)
    return init(layout, block)
}

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
public fun (@VaadinDsl FormLayout).formItem(label: Component? = null, block: (@VaadinDsl FormLayout.FormItem).() -> Unit = {}): FormLayout.FormItem {
    val item: KFormItem = KFormItem().apply {
        if (label != null) {
            addToLabel(label)
        }
    }
    return init(item, block)
}

/**
 * Creates a form item inside of the [FormLayout], with an optional [label]. See [Form Layout](https://vaadin.com/elements/vaadin-form-layout)
 * documentation for more details.
 */
@VaadinDsl
public fun (@VaadinDsl FormLayout).formItem(label: String, block: (@VaadinDsl FormLayout.FormItem).() -> Unit = {}): FormLayout.FormItem =
        formItem(NativeLabel(label), block)

/**
 * Builds a list of [com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep].
 */
public class FormLayoutResponsiveSteps {
    internal val steps = mutableListOf<FormLayout.ResponsiveStep>()
    public val aside: FormLayout.ResponsiveStep.LabelsPosition = FormLayout.ResponsiveStep.LabelsPosition.ASIDE
    public val top: FormLayout.ResponsiveStep.LabelsPosition = FormLayout.ResponsiveStep.LabelsPosition.TOP
    /**
     * Adds a responsive step with the minimum width, given number of [columns] and [labelsPosition]. Example of use:
     * ```
     * "30em"(2, top)
     * ```
     * @receiver the minimum width defined as CSS format, e.g. "0px", "30em"
     */
    public operator fun String.invoke(columns: Int, labelsPosition: FormLayout.ResponsiveStep.LabelsPosition? = null) {
        steps.add(FormLayout.ResponsiveStep(this, columns, labelsPosition))
    }
}

/**
 * Allows you to specify responsive steps in a much more condensed form:
 * ```
 * responsiveSteps { "0px"(1, top); "30em"(2, aside) }
 * ```
 */
public fun (@VaadinDsl FormLayout).responsiveSteps(block: FormLayoutResponsiveSteps.()->Unit) {
    val steps = FormLayoutResponsiveSteps()
    steps.block()
    responsiveSteps = steps.steps
}
