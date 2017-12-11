package com.github.vok.karibudsl.flow

import com.vaadin.ui.Component
import com.vaadin.ui.button.Button
import com.vaadin.ui.checkbox.Checkbox
import com.vaadin.ui.combobox.ComboBox
import com.vaadin.ui.common.HasClickListeners
import com.vaadin.ui.common.HasComponents
import com.vaadin.ui.datepicker.DatePicker
import com.vaadin.ui.dialog.Dialog
import com.vaadin.ui.formlayout.FormLayout
import com.vaadin.ui.grid.Grid
import com.vaadin.ui.icon.Icon
import com.vaadin.ui.icon.VaadinIcons
import com.vaadin.ui.layout.FlexLayout
import com.vaadin.ui.layout.HorizontalLayout
import com.vaadin.ui.layout.VerticalLayout
import com.vaadin.ui.splitlayout.SplitLayout
import com.vaadin.ui.textfield.PasswordField
import com.vaadin.ui.textfield.TextField

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
fun <T: Any?> (@VaadinDsl HasComponents).grid(block: (@VaadinDsl Grid<T>).() -> Unit = {})
        = init(Grid(), block)
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
