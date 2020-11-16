package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import kotlin.streams.toList

/**
 * Creates a [Vaadin Dialog](https://vaadin.com/elements/vaadin-dialog). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).dialog(block: (@VaadinDsl Dialog).() -> Unit = {}): Dialog
        = init(Dialog(), block)

/**
 * Returns all dialogs opened in the current UI.
 */
public fun getAllDialogs(): List<Dialog> = UI.getCurrent().getAllDialogs()

/**
 * Returns all dialogs opened in the given UI.
 */
public fun UI.getAllDialogs(): List<Dialog> = children.toList().filterIsInstance<Dialog>()

/**
 * Centers the dialog on-screen.
 */
public fun Dialog.center() {
    // https://github.com/vaadin/vaadin-dialog/issues/220
    val styles: MutableList<String> = mutableListOf<String>()
    if (width != null) {
        styles.add("width: $width")
    }
    if (height != null) {
        styles.add("height: $height")
    }
    element.executeJs("this.$.overlay.$.overlay.style = '${styles.joinToString(";")}';")
}
