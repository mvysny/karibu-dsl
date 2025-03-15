package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.DummyHasComponents
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.karibuDslI18n
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.dialog.Dialog

/**
 * Populates the dialog footer:
 * ```kotlin
 * dialog.footer {
 *   button("Save") { }
 * }
 * ```
 */
@VaadinDsl
public fun Dialog.footer(block: (@VaadinDsl HasComponents).() -> Unit) {
    val f = object : DummyHasComponents() {
        override fun add(vararg components: Component) {
            this@footer.footer.add(*components)
        }
    }
    block(f)
}

/**
 * Populates the dialog header:
 * ```kotlin
 * dialog.header {
 *   h3("Title")
 * }
 * ```
 */
@VaadinDsl
public fun Dialog.header(block: (@VaadinDsl HasComponents).() -> Unit) {
    val f = object : DummyHasComponents() {
        override fun add(vararg components: Component) {
            this@header.header.add(*components)
        }
    }
    block(f)
}

/**
 * Creates a dialog with given [header] and [text]. Call [ConfirmDialog.setConfirmButton]
 * to configure the confirm button. The dialog is automatically opened.
 * ```
 * openConfirmDialog(
 *     "Delete beverage",
 *     "Are you sure you want to delete beverage '${item.name}'?") {
 *     setConfirmButton("Delete") {
 *         frame.close()
 *         onDeleteItem(item)
 *     }
 *     setConfirmIsDanger()
 *     setCloseOnCancel("Cancel")
 * }
 * ```
 */
public fun openConfirmDialog(header: String? = null, text: String? = null, block: (@VaadinDsl ConfirmDialog).() -> Unit = {}): ConfirmDialog {
    val dlg = ConfirmDialog()
    if (header != null) {
        dlg.setHeader(header)
    }
    if (text != null) {
        dlg.setText(text)
    }
    dlg.open()
    dlg.block()
    return dlg
}

/**
 * Adds a cancel button to the dialog; the dialog is closed when the button is clicked.
 * @param buttonText the caption of the button.
 */
public fun ConfirmDialog.setCloseOnCancel(buttonText: String = karibuDslI18n("cancel")) {
    setCancelButton(buttonText) { close() }
}

/**
 * Example of usage:
 * ```kotlin
 * confirmButton("Delete", VaadinIcon.CHECK.create()) {
 *   css {
 *      backgroundColor = Color.red
 *   }
 *
 *   onClick {
 *     ...
 *   }
 *
 * }
 * ```
 */
@VaadinDsl
public fun ConfirmDialog.setConfirm(
    text: String? = null,
    icon: Component? = null,
    id: String? = null,
    block: (@VaadinDsl Button).() -> Unit = {}
) {
    setConfirmButton(Button(text, icon).apply {
        if (text != null) this.text = text
        if (icon != null) this.icon = icon
        if (id != null) setId(id)
        block()
    })
}

/**
 * Sets the confirm button variants.
 */
public fun ConfirmDialog.setConfirmButtonVariant(vararg theme: ButtonVariant) {
    setConfirmButtonTheme(theme.joinToString(" ") { it.variantName })
}

/**
 * Sets the confirm button as dangerous, e.g. when you're confirming a deletion action
 * that can not be reversed.
 */
public fun ConfirmDialog.setConfirmIsDanger() {
    setConfirmButtonVariant(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY)
}

/**
 * Sets the cancel button variants.
 */
public fun ConfirmDialog.setCancelButtonVariant(vararg theme: ButtonVariant) {
    setCancelButtonTheme(theme.joinToString(" ") { it.variantName })
}

/**
 * Sets the reject button variants.
 */
public fun ConfirmDialog.setRejectButtonVariant(vararg theme: ButtonVariant) {
    setRejectButtonTheme(theme.joinToString(" ") { it.variantName })
}
