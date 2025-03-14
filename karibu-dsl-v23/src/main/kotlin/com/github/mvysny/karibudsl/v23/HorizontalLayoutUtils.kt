package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.DummyHasComponents
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

/**
 * Adds components to the 'start' slot, using [HorizontalLayout.addToStart]. Since Vaadin 24.7.
 */
@VaadinDsl
public fun (@VaadinDsl HorizontalLayout).start(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    object : DummyHasComponents {
        override fun add(vararg components: Component) {
            addToStart(*components)
        }
    }.block()
}

/**
 * Adds components to the 'middle' slot, using [HorizontalLayout.addToMiddle]. Since Vaadin 24.7.
 */
@VaadinDsl
public fun (@VaadinDsl HorizontalLayout).middle(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    object : DummyHasComponents {
        override fun add(vararg components: Component) {
            addToMiddle(*components)
        }
    }.block()
}

/**
 * Adds components to the 'end' slot, using [HorizontalLayout.addToEnd]. Since Vaadin 24.7.
 */
@VaadinDsl
public fun (@VaadinDsl HorizontalLayout).end(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    object : DummyHasComponents {
        override fun add(vararg components: Component) {
            addToEnd(*components)
        }
    }.block()
}
