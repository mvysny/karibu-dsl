package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.DummyHasComponents
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.dom.Element

@VaadinDsl
public fun (@VaadinDsl LoginOverlay).footer(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val f = footer
    val hc = object : DummyHasComponents {
        override fun add(components: MutableCollection<Component>) {
            f.add(*components.toTypedArray())
        }
    }
    hc.block()
}

@VaadinDsl
public fun (@VaadinDsl LoginOverlay).customFormArea(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val f = customFormArea
    val hc = object : DummyHasComponents {
        override fun add(components: MutableCollection<Component>) {
            f.add(*components.toTypedArray())
        }
    }
    hc.block()
}
