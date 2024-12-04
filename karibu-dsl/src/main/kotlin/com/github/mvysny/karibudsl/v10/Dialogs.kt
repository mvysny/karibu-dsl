package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.dialog.Dialog

/**
 * Creates a [Vaadin Dialog](https://vaadin.com/elements/vaadin-dialog). See the HTML Examples link for a list
 * of possible alternative themes.
 * ```
 * openDialog {
 *   header { h3("Header") } // or better: this.setHeaderTitle("Header")
 *   verticalLayout(isPadding = false) {
 *     // contents
 *   }
 *   footer {
 *     button("Save") {
 *       setPrimary()
 *     }
 *     button("Cancel")
 *   }
 * }
 *```
 */
public fun openDialog(block: (@VaadinDsl Dialog).() -> Unit = {}): Dialog {
    val dlg = Dialog()
    dlg.open()
    dlg.block()
    return dlg
}
