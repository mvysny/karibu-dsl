package com.github.mvysny.karibudsl.v8

import com.vaadin.ui.Accordion
import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents

public open class _Accordion : Accordion() {
    /**
     * Allows you to access the current tab from the DSL:
     * ```kotlin
     * accordion {
     *    // adding a component to tabsheet will create a tab for the component as well.
     *    label("Foo bar baz blah blah blah") {
     *      tab.caption = "Tab 1"
     *      tab.icon = VaadinIcons.TAB
     *    }
     * }
     * ```
     */
    @get:JvmName("getTab2")
    public val (@VaadinDsl Component).tab: Tab
        get() = this@_Accordion.getTab(this) ?: throw IllegalStateException("$this is not child of ${this@_Accordion}")
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).accordion(block: _Accordion.()->Unit = {}): _Accordion =
        init(_Accordion(), block)
