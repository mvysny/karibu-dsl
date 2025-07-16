package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.markdown.Markdown

/**
 * Creates the [Markdown](https://vaadin.com/docs/latest/components/markdown) component.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).markdown(
    content: String? = null,
    block: (@VaadinDsl Markdown).() -> Unit = {}
): Markdown = init(Markdown(content), block)
