package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Text

/**
 * Appends a text node with given [text] to the component.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).text(text: String, block: (@VaadinDsl Text).() -> Unit = {}): Text =
        init(Text(text), block)
