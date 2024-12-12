package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.IconFactory
import com.vaadin.flow.component.icon.VaadinIcon

/**
 * Creates a Vaadin [Icon](https://vaadin.com/elements/vaadin-icons/). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).icon(icon: IconFactory, block: (@VaadinDsl Icon).() -> Unit = {}): Icon =
        init(icon.create(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).icon(collection: String, icon: String, block: (@VaadinDsl Icon).() -> Unit = {}): Icon =
        init(Icon(collection, icon), block)
