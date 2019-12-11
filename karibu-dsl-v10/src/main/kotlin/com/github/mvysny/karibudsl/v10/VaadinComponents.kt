package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.textfield.*
import com.vaadin.flow.component.timepicker.TimePicker
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
fun <T : Component> (@VaadinDsl ClickNotifier<T>).onLeftClick(leftClickListener: ((ClickEvent<T>) -> Unit)): Registration =
        addClickListener(leftClickListener)

/**
 * Creates a [Vaadin Checkbox](https://vaadin.com/elements/vaadin-checkbox/). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).checkBox(label: String? = null, block: (@VaadinDsl Checkbox).() -> Unit = {}): Checkbox = init(Checkbox(label), block)

/**
 * Creates a [Vaadin Combo Box](https://vaadin.com/elements/vaadin-combo-box). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).comboBox(label: String? = null, block: (@VaadinDsl ComboBox<T>).() -> Unit = {}) = init(ComboBox(label), block)

/**
 * Creates a [Vaadin Select](https://vaadin.com/components/vaadin-select). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 *
 * The difference between combobox and select is that select isn't lazy, but you can add any child component into the select
 * and it will appear in the popup.
 */
@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).select(label: String? = null, block: (@VaadinDsl Select<T>).() -> Unit = {}) = init(Select<T>()) {
    if (label != null) setLabel(label)
    block()
}

/**
 * Creates a [Vaadin Date Picker](https://vaadin.com/elements/vaadin-date-picker). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).datePicker(label: String? = null, block: (@VaadinDsl DatePicker).() -> Unit = {}) = init(DatePicker(label), block)

/**
 * Creates a [Vaadin Dialog](https://vaadin.com/elements/vaadin-dialog). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).dialog(block: (@VaadinDsl Dialog).() -> Unit = {}) = init(Dialog(), block)

/**
 * Creates a [Iron Icon](https://vaadin.com/elements/vaadin-icons/). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).icon(icon: VaadinIcon, block: (@VaadinDsl Icon).() -> Unit = {}) = init(Icon(icon), block)

/**
 * Creates a [Password Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).passwordField(label: String? = null, block: (@VaadinDsl PasswordField).() -> Unit = {}) = init(PasswordField(label), block)

/**
 * Creates a [Split Layout](https://vaadin.com/elements/vaadin-split-layout). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).splitLayout(block: (@VaadinDsl SplitLayout).() -> Unit = {}) = init(SplitLayout(), block)

/**
 * Creates a [Text Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).textField(label: String? = null, block: (@VaadinDsl TextField).() -> Unit = {}) = init(TextField(label), block)

/**
 * Creates a [Email Field](https://vaadin.com/components/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).emailField(label: String? = null, block: (@VaadinDsl EmailField).() -> Unit = {}) = init(EmailField(label), block)

/**
 * Creates a [Number Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).numberField(label: String? = null, block: (@VaadinDsl NumberField).() -> Unit = {}) = init(NumberField(label), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).textArea(label: String? = null, block: (@VaadinDsl TextArea).() -> Unit = {}) = init(TextArea(label), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).tabs(block: (@VaadinDsl Tabs).() -> Unit = {}) = init(Tabs(), block)

@VaadinDsl
fun (@VaadinDsl Tabs).tab(label: String? = null, block: (@VaadinDsl Tab).() -> Unit = {}) = init(Tab(label), block)

@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).checkBoxGroup(block: (@VaadinDsl CheckboxGroup<T>).() -> Unit = {}) = init(CheckboxGroup(), block)

/**
 * Creates a [Time Picker](https://vaadin.com/components/vaadin-time-picker) field.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).timePicker(label: String? = null, block: (@VaadinDsl TimePicker).() -> Unit = {}) = init(TimePicker(label), block)

/**
 * Creates a [Integer Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 *
 * Only available starting with Vaadin 14.1.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).integerField(label: String? = null, block: (@VaadinDsl IntegerField).() -> Unit = {}) = init(IntegerField(label), block)

/**
 * Creates a [BigDecimal Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 *
 * Only available starting with Vaadin 14.1.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).bigDecimalField(label: String? = null, block: (@VaadinDsl BigDecimalField).() -> Unit = {}) = init(BigDecimalField(label), block)
