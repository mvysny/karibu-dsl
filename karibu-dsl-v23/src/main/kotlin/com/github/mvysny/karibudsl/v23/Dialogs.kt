package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.dom.Element

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
    val f = object : HasComponents {
        override fun getElement(): Element =
            throw UnsupportedOperationException("footer element is not accessible")

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
    val f = object : HasComponents {
        override fun getElement(): Element =
            throw UnsupportedOperationException("header element is not accessible")

        override fun add(vararg components: Component) {
            this@header.header.add(*components)
        }
    }
    block(f)
}
