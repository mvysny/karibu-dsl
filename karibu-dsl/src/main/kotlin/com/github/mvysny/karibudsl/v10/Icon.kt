package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.IronIcon
import com.vaadin.flow.component.icon.VaadinIcon

/**
 * Creates a [Iron Icon](https://vaadin.com/elements/vaadin-icons/). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).icon(icon: VaadinIcon, block: (@VaadinDsl Icon).() -> Unit = {}): Icon =
        init(Icon(icon), block)

@Suppress("DEPRECATION")
@Deprecated("Use ironIcon()")
@VaadinDsl
public fun (@VaadinDsl HasComponents).icon(collection: String, icon: String, block: (@VaadinDsl Icon).() -> Unit = {}): Icon =
        init(Icon(collection, icon), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).ironIcon(collection: String, icon: String, block: (@VaadinDsl IronIcon).() -> Unit = {}): IronIcon =
        init(IronIcon(collection, icon), block)
