package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcons
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.dom.ThemeList
import com.vaadin.flow.shared.Registration

/**
 * Creates a [Vaadin Button](https://vaadin.com/elements/vaadin-button) with an optional [text] and an [icon], and adds it to the parent.
 *
 * See [vaadin-button](https://vaadin.com/elements/vaadin-button/html-examples/button-lumo-theme-demos) for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 * @param icon the icon, to use icons provided by Lumo just use `Icon("lumo", "plus")` or `Icon(VaadinIcons.TRASH)`
 * @param block runs the block with the button as a receiver.
 */
fun (@VaadinDsl HasComponents).button(text: String? = null, icon: Component? = null, block: (@VaadinDsl Button).() -> Unit = {})
        = init(Button(text, icon), block)

/**
 * Sets the button as primary. It effectively adds the "primary" theme.
 */
fun (@VaadinDsl Button).setPrimary() {
    themes.add("primary")
}

/**
 * Provides access to component themes. Effectively [com.vaadin.flow.dom.Element.getThemeList].
 */
val (@VaadinDsl HasElement).themes: ThemeList get() = element.themeList

/**
 * Adds a [leftClickListener].
 */
fun <T: Component> (@VaadinDsl ClickNotifier<T>).onLeftClick(leftClickListener: ((ClickEvent<T>)->Unit)): Registration =
    addClickListener(leftClickListener)

/**
 * Creates a [Vaadin Checkbox](https://vaadin.com/elements/vaadin-checkbox/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).checkBox(label: String? = null, block: (@VaadinDsl Checkbox).() -> Unit = {})
        = init(Checkbox(label), block)

/**
 * Creates a [Vaadin Combo Box](https://vaadin.com/elements/vaadin-combo-box). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun <T: Any?> (@VaadinDsl HasComponents).comboBox(label: String? = null, block: (@VaadinDsl ComboBox<T>).() -> Unit = {})
        = init(ComboBox(label), block)

/**
 * Creates a [Vaadin Date Picker](https://vaadin.com/elements/vaadin-date-picker). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).datePicker(label: String? = null, block: (@VaadinDsl DatePicker).() -> Unit = {})
        = init(DatePicker(label), block)

/**
 * Creates a [Vaadin Dialog](https://vaadin.com/elements/vaadin-dialog). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).dialog(block: (@VaadinDsl Dialog).() -> Unit = {})
        = init(Dialog(), block)

/**
 * Creates a [Form Layout](https://vaadin.com/elements/vaadin-form-layout). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).formLayout(block: (@VaadinDsl FormLayout).() -> Unit = {})
        = init(FormLayout(), block)

/**
 * Creates a [Iron Icon](https://vaadin.com/elements/vaadin-icons/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).icon(icon: VaadinIcons = VaadinIcons.VAADIN_H, block: (@VaadinDsl Icon).() -> Unit = {})
        = init(Icon(icon), block)

/**
 * Creates a [Flex Layout](https://vaadin.com/elements/vaadin-ordered-layout/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).flexLayout(block: (@VaadinDsl FlexLayout).() -> Unit = {})
        = init(FlexLayout(), block)
/**
 * Creates a [Vertical Layout](https://vaadin.com/elements/vaadin-ordered-layout/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 *
 * The default content align is set to `content { align(left, top) }`.
 */
fun (@VaadinDsl HasComponents).verticalLayout(block: (@VaadinDsl VerticalLayout).() -> Unit = {}) = init(VerticalLayout(), {
    content { align(left, top) }
    block()
})
/**
 * Creates a [Horizontal Layout](https://vaadin.com/elements/vaadin-ordered-layout/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 *
 * The default content align is set to `content { align(left, baseline) }`.
 */
fun (@VaadinDsl HasComponents).horizontalLayout(block: (@VaadinDsl HorizontalLayout).() -> Unit = {}) = init(HorizontalLayout(), {
    content { align(left, baseline) }
    block()
})

/**
 * Creates a [Password Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).passwordField(label: String? = null, block: (@VaadinDsl PasswordField).() -> Unit = {})
        = init(PasswordField(label), block)

/**
 * Creates a [Split Layout](https://vaadin.com/elements/vaadin-split-layout). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).splitLayout(block: (@VaadinDsl SplitLayout).() -> Unit = {})
        = init(SplitLayout(), block)
/**
 * Creates a [Text Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
fun (@VaadinDsl HasComponents).textField(label: String? = null, block: (@VaadinDsl TextField).() -> Unit = {})
        = init(TextField(label), block)
fun (@VaadinDsl HasComponents).textArea(label: String? = null, block: (@VaadinDsl TextArea).() -> Unit = {})
        = init(TextArea(label), block)
