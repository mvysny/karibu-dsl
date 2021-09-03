package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.listbox.ListBox
import com.vaadin.flow.component.listbox.MultiSelectListBox
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.progressbar.ProgressBar
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.timepicker.TimePicker
import com.vaadin.flow.dom.Element
import com.vaadin.flow.shared.Registration

/**
 * Creates a [Vaadin Button](https://vaadin.com/elements/vaadin-button) with an optional [text] and an [icon], and adds it to the parent.
 *
 * See [vaadin-button](https://vaadin.com/elements/vaadin-button/html-examples/button-lumo-theme-demos) for a list
 * of possible alternative themes for the button.
 * @param icon the icon, to use icons provided by Lumo. Just use `VaadinIcon.TRASH.create()` or `Icon("lumo", "plus")`
 * @param block runs the block with the button as a receiver.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).button(
    text: String? = null,
    icon: Component? = null,
    id: String? = null,
    block: (@VaadinDsl Button).() -> Unit = {}
): Button = init(Button(text, icon), id, block)

/**
 * Utility method which creates a [Vaadin Button](https://vaadin.com/elements/vaadin-button) which
 * acts as an icon-only button (using the [ButtonVariant.LUMO_ICON] variant).
 *
 * See [vaadin-button](https://vaadin.com/elements/vaadin-button/html-examples/button-lumo-theme-demos) for a list
 * of possible alternative themes for the button.
 * @param icon the icon, to use icons provided by Lumo just use `Icon("lumo", "plus")` or `Icon(VaadinIcons.TRASH)`
 * @param block runs the block with the button as a receiver.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).iconButton(icon: Component, block: (@VaadinDsl Button).() -> Unit = {}): Button = button(icon = icon) {
    addThemeVariants(ButtonVariant.LUMO_ICON)
    block()
}

/**
 * Sets the button as primary. It effectively adds the [ButtonVariant.LUMO_PRIMARY] theme variant.
 */
@Deprecated("moved to karibu-tools")
public fun (@VaadinDsl Button).setPrimary() {
    addThemeVariants(ButtonVariant.LUMO_PRIMARY)
}

/**
 * Adds a [leftClickListener].
 */
public fun <T : Component> (@VaadinDsl ClickNotifier<T>).onLeftClick(leftClickListener: ((ClickEvent<T>) -> Unit)): Registration
        = addClickListener(leftClickListener)

/**
 * Creates a [Vaadin Checkbox](https://vaadin.com/elements/vaadin-checkbox/). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).checkBox(label: String? = null, block: (@VaadinDsl Checkbox).() -> Unit = {}): Checkbox
        = init(Checkbox(label), block)

/**
 * Creates a [Vaadin Combo Box](https://vaadin.com/elements/vaadin-combo-box). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).comboBox(label: String? = null, block: (@VaadinDsl ComboBox<T>).() -> Unit = {}): ComboBox<T>
        = init(ComboBox(label), block)

/**
 * Creates a [Vaadin Select](https://vaadin.com/components/vaadin-select). See the HTML Examples link for a list
 * of possible alternative themes.
 *
 * The difference between combobox and select is that select isn't lazy, but you can add any child component into the select
 * and it will appear in the popup.
 */
@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).select(label: String? = null, block: (@VaadinDsl Select<T>).() -> Unit = {}): Select<T> {
    val select = Select<T>()
    if (label != null) select.label = label
    return init(select, block)
}

/**
 * Creates a [Vaadin Date Picker](https://vaadin.com/elements/vaadin-date-picker). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).datePicker(label: String? = null, block: (@VaadinDsl DatePicker).() -> Unit = {}): DatePicker
        = init(DatePicker(label), block)

/**
 * Creates a [Split Layout](https://vaadin.com/elements/vaadin-split-layout). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).splitLayout(block: (@VaadinDsl SplitLayout).() -> Unit = {}): SplitLayout
        = init(SplitLayout(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).tabs(orientation: Tabs.Orientation? = null, block: (@VaadinDsl Tabs).() -> Unit = {}): Tabs {
    val component = Tabs()
    if (orientation != null) {
        component.orientation = orientation
    }
    return init(component, block)
}

@VaadinDsl
public fun (@VaadinDsl Tabs).tab(label: String? = null, block: (@VaadinDsl Tab).() -> Unit = {}): Tab =
        init(Tab(label), block)

@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).checkBoxGroup(block: (@VaadinDsl CheckboxGroup<T>).() -> Unit = {}): CheckboxGroup<T>
        = init(CheckboxGroup(), block)

/**
 * Creates a [Time Picker](https://vaadin.com/components/vaadin-time-picker) field.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).timePicker(label: String? = null, block: (@VaadinDsl TimePicker).() -> Unit = {}): TimePicker
        = init(TimePicker(label), block)

/**
 * A [java.time.LocalDateTime] editor.
 * @since Vaadin 14.2
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).dateTimePicker(label: String? = null, block: (@VaadinDsl DateTimePicker).() -> Unit = {}): DateTimePicker {
    val picker = DateTimePicker()
    picker.label = label
    return init(picker, block)
}

/**
 * Use as follows:
 * ```
 * scroller {
 *   content {
 *     div {
 *       width = "200px"; height = "200px"; element.styles.add("background-color", "red")
 *     }
 *   }
 * }
 * ```
 *
 * @since Vaadin 14.2
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).scroller(scrollDirection: Scroller.ScrollDirection = Scroller.ScrollDirection.BOTH,
                                        block: (@VaadinDsl Scroller).() -> Unit = {}): Scroller =
        init(Scroller(scrollDirection), block)

@VaadinDsl
public fun <T> (@VaadinDsl Scroller).content(block: (@VaadinDsl HasComponents).() -> T): T {
    element.removeAllChildren()
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            require(components.size < 2) { "Too many components to add - scroller can only host one! ${components.toList()}" }
            val component: Component = components.firstOrNull() ?: return
            check(this@content.element.childCount == 0) { "The scroller can only host one component at most" }
            content = component
        }
    }
    val result: T = dummy.block()
    checkNotNull(content) { "`block` must add exactly one component to the scroller" }
    return result
}

/**
 * Creates a [Vaadin List Box](https://vaadin.com/components/vaadin-list-box). See the HTML Examples link for a list
 * of possible alternative themes.
 *
 * Unfortunately no label support for now: https://github.com/vaadin/vaadin-list-box-flow/issues/75
 */
@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).listBox(block: (@VaadinDsl ListBox<T>).() -> Unit = {}): ListBox<T>
        = init(ListBox(), block)

/**
 * Creates a multi-select [Vaadin List Box](https://vaadin.com/components/vaadin-list-box). See the HTML Examples link for a list
 * of possible alternative themes.
 *
 * Unfortunately no label support for now: https://github.com/vaadin/vaadin-list-box-flow/issues/75
 */
@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).multiSelectListBox(block: (@VaadinDsl MultiSelectListBox<T>).() -> Unit = {}): MultiSelectListBox<T>
        = init(MultiSelectListBox(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).progressBar(
        min: Double = 0.0,
        max: Double = 1.0,
        value: Double = min,
        indeterminate: Boolean = false,
        block: (@VaadinDsl ProgressBar).() -> Unit = {}
): ProgressBar {
    val component = ProgressBar(min, max, value)
    component.isIndeterminate = indeterminate
    return init(component, block)
}

/**
 * Creates a [Vaadin Radio Button](https://vaadin.com/components/vaadin-radio-button). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).radioButtonGroup(
    label: String? = null,
    block: (@VaadinDsl RadioButtonGroup<T>).() -> Unit = {}
): RadioButtonGroup<T> {
    val component = RadioButtonGroup<T>()
    if (label != null) {
        component.label = label
    }
    return init(component, block)
}

/**
 * Appends a text node with given [text] to the component.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).text(text: String, block: (@VaadinDsl Text).() -> Unit = {}): Text =
    init(Text(text), block)
