package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.HasClickListeners
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcons
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField

fun (@VaadinDsl HasComponents).button(text: String? = null, icon: Component? = null, block: (@VaadinDsl Button).() -> Unit = {})
        = init(Button(text, icon), block)
fun (@VaadinDsl Button).setPrimary() {
    element.setAttribute("theme", "primary")
}

fun <T: Component> (@VaadinDsl HasClickListeners<T>).onLeftClick(leftClickListener: ((HasClickListeners.ClickEvent<T>)->Unit)) {
    addClickListener(leftClickListener)
}

fun (@VaadinDsl HasComponents).checkBox(label: String? = null, block: (@VaadinDsl Checkbox).() -> Unit = {})
        = init(Checkbox(label), block)
fun <T: Any?> (@VaadinDsl HasComponents).comboBox(label: String? = null, block: (@VaadinDsl ComboBox<T>).() -> Unit = {})
        = init(ComboBox(label), block)
fun (@VaadinDsl HasComponents).datePicker(label: String? = null, block: (@VaadinDsl DatePicker).() -> Unit = {})
        = init(DatePicker(label), block)
fun (@VaadinDsl HasComponents).dialog(block: (@VaadinDsl Dialog).() -> Unit = {})
        = init(Dialog(), block)
fun (@VaadinDsl HasComponents).formLayout(block: (@VaadinDsl FormLayout).() -> Unit = {})
        = init(FormLayout(), block)
fun (@VaadinDsl HasComponents).icon(icon: VaadinIcons = VaadinIcons.VAADIN_H, block: (@VaadinDsl Icon).() -> Unit = {})
        = init(Icon(icon), block)

fun (@VaadinDsl HasComponents).flexLayout(block: (@VaadinDsl FlexLayout).() -> Unit = {})
        = init(FlexLayout(), block)
fun (@VaadinDsl HasComponents).verticalLayout(block: (@VaadinDsl VerticalLayout).() -> Unit = {})
        = init(VerticalLayout(), block)
fun (@VaadinDsl HasComponents).horizontalLayout(block: (@VaadinDsl HorizontalLayout).() -> Unit = {})
        = init(HorizontalLayout(), block)
fun (@VaadinDsl HasComponents).passwordField(label: String? = null, block: (@VaadinDsl PasswordField).() -> Unit = {})
        = init(PasswordField(label), block)
fun (@VaadinDsl HasComponents).splitLayout(block: (@VaadinDsl SplitLayout).() -> Unit = {})
        = init(SplitLayout(), block)
fun (@VaadinDsl HasComponents).textField(label: String? = null, block: (@VaadinDsl TextField).() -> Unit = {})
        = init(TextField(label), block)
fun (@VaadinDsl HasComponents).textArea(label: String? = null, block: (@VaadinDsl TextArea).() -> Unit = {})
        = init(TextArea(label), block)

var (@VaadinDsl Component).flexGrow: Double
    get() = (parent.get() as FlexComponent<*>).getFlexGrow(this)
    set(value) { (parent.get() as FlexComponent<*>).setFlexGrow(value, this) }
var (@VaadinDsl Component).isExpand: Boolean
    get() = flexGrow >= 1
    set(value) { flexGrow = if (value) 1.0 else 0.0 }
