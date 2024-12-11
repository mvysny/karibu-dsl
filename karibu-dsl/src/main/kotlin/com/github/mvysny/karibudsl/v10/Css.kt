package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasStyle
import kotlinx.css.CssBuilder
import kotlinx.css.hyphenize

@VaadinDsl
public inline fun HasStyle.css(block: CssBuilder.() -> Unit) =
    CssBuilder().apply {
        block()
        declarations.forEach { (key, value) ->
            style[key.hyphenize()] = value.toString()
        }
    }

