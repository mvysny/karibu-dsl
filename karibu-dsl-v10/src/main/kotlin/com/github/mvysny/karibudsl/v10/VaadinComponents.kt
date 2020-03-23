package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.textfield.*
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
fun (@VaadinDsl HasComponents).button(text: String? = null, icon: Component? = null, block: (@VaadinDsl Button).() -> Unit = {}): Button = init(Button(text, icon), block)

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
fun (@VaadinDsl HasComponents).iconButton(icon: Component, block: (@VaadinDsl Button).() -> Unit = {}): Button = button(icon = icon) {
    addThemeVariants(ButtonVariant.LUMO_ICON)
    block()
}

/**
 * Sets the button as primary. It effectively adds the [ButtonVariant.LUMO_PRIMARY] theme variant.
 */
fun (@VaadinDsl Button).setPrimary() {
    addThemeVariants(ButtonVariant.LUMO_PRIMARY)
}

/**
 * Adds a [leftClickListener].
 */
fun <T : Component> (@VaadinDsl ClickNotifier<T>).onLeftClick(leftClickListener: ((ClickEvent<T>) -> Unit)): Registration
        = addClickListener(leftClickListener)

/**
 * Creates a [Vaadin Checkbox](https://vaadin.com/elements/vaadin-checkbox/). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).checkBox(label: String? = null, block: (@VaadinDsl Checkbox).() -> Unit = {}): Checkbox
        = init(Checkbox(label), block)

/**
 * Creates a [Vaadin Combo Box](https://vaadin.com/elements/vaadin-combo-box). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).comboBox(label: String? = null, block: (@VaadinDsl ComboBox<T>).() -> Unit = {}): ComboBox<T>
        = init(ComboBox(label), block)

/**
 * Creates a [Vaadin Select](https://vaadin.com/components/vaadin-select). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 *
 * The difference between combobox and select is that select isn't lazy, but you can add any child component into the select
 * and it will appear in the popup.
 */
@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).select(label: String? = null, block: (@VaadinDsl Select<T>).() -> Unit = {}): Select<T> {
    val select = Select<T>()
    if (label != null) select.label = label
    return init(select, block)
}

/**
 * Creates a [Vaadin Date Picker](https://vaadin.com/elements/vaadin-date-picker). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).datePicker(label: String? = null, block: (@VaadinDsl DatePicker).() -> Unit = {}): DatePicker
        = init(DatePicker(label), block)

/**
 * Creates a [Vaadin Dialog](https://vaadin.com/elements/vaadin-dialog). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).dialog(block: (@VaadinDsl Dialog).() -> Unit = {}): Dialog
        = init(Dialog(), block)

/**
 * Creates a [Password Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).passwordField(label: String? = null, block: (@VaadinDsl PasswordField).() -> Unit = {}): PasswordField
        = init(PasswordField(label), block)

/**
 * Creates a [Split Layout](https://vaadin.com/elements/vaadin-split-layout). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).splitLayout(block: (@VaadinDsl SplitLayout).() -> Unit = {}): SplitLayout
        = init(SplitLayout(), block)

/**
 * Creates a [Text Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).textField(label: String? = null, block: (@VaadinDsl TextField).() -> Unit = {}): TextField
        = init(TextField(label), block)

/**
 * Creates a [Email Field](https://vaadin.com/components/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).emailField(label: String? = null, block: (@VaadinDsl EmailField).() -> Unit = {}): EmailField
        = init(EmailField(label), block)

/**
 * Creates a [Number Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).numberField(label: String? = null, block: (@VaadinDsl NumberField).() -> Unit = {}): NumberField
        = init(NumberField(label), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).textArea(label: String? = null, block: (@VaadinDsl TextArea).() -> Unit = {}): TextArea
        = init(TextArea(label), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).tabs(block: (@VaadinDsl Tabs).() -> Unit = {}) = init(Tabs(), block)

@VaadinDsl
fun (@VaadinDsl Tabs).tab(label: String? = null, block: (@VaadinDsl Tab).() -> Unit = {}) = init(Tab(label), block)

@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).checkBoxGroup(block: (@VaadinDsl CheckboxGroup<T>).() -> Unit = {}): CheckboxGroup<T>
        = init(CheckboxGroup(), block)

/**
 * Creates a [Time Picker](https://vaadin.com/components/vaadin-time-picker) field.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).timePicker(label: String? = null, block: (@VaadinDsl TimePicker).() -> Unit = {}): TimePicker
        = init(TimePicker(label), block)

/**
 * Creates a [Integer Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 *
 * Only available starting with Vaadin 14.1.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).integerField(label: String? = null, block: (@VaadinDsl IntegerField).() -> Unit = {}): IntegerField
        = init(IntegerField(label), block)

/**
 * Creates a [BigDecimal Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 *
 * Only available starting with Vaadin 14.1.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).bigDecimalField(label: String? = null, block: (@VaadinDsl BigDecimalField).() -> Unit = {}): BigDecimalField
        = init(BigDecimalField(label), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).dateTimePicker(label: String? = null, block: (@VaadinDsl DateTimePicker).() -> Unit = {}): DateTimePicker {
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
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).scroller(scrollDirection: Scroller.ScrollDirection = Scroller.ScrollDirection.BOTH,
                                        block: (@VaadinDsl Scroller).() -> Unit = {}): Scroller =
        init(Scroller(scrollDirection), block)

@VaadinDsl
fun <T> (@VaadinDsl Scroller).content(block: (@VaadinDsl HasComponents).() -> T): T {
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
