@file:Suppress("DEPRECATION")

package com.github.mvysny.karibudsl.v8.v7compat

import com.github.mvysny.karibudsl.v8.VaadinDsl
import com.github.mvysny.karibudsl.v8.init
import com.vaadin.ui.HasComponents
import com.vaadin.v7.ui.HorizontalLayout
import com.vaadin.v7.ui.VerticalLayout

@Deprecated("Use VerticalLayout from Vaadin 8")
@VaadinDsl
public fun (@VaadinDsl HasComponents).verticalLayout7(block: (@VaadinDsl VerticalLayout).()->Unit = {}): VerticalLayout =
        init(VerticalLayout(), block)

@Deprecated("Use HorizontalLayout from Vaadin 8")
@VaadinDsl
public fun (@VaadinDsl HasComponents).horizontalLayout7(block: (@VaadinDsl HorizontalLayout).()->Unit = {}): HorizontalLayout =
        init(HorizontalLayout(), block)
