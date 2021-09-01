package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.dialog.Dialog

/**
 * Creates a [Vaadin Dialog](https://vaadin.com/elements/vaadin-dialog). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).dialog(block: (@VaadinDsl Dialog).() -> Unit = {}): Dialog
        = init(Dialog(), block)
