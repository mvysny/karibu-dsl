package com.github.vok.karibudsl

import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents
import com.vaadin.ui.TabSheet

class _TabSheet : TabSheet() {
    /**
     * Allows you to access the current tab from the DSL:
     * ```kotlin
     * tabSheet {
     *    // adding a component to tabsheet will create a tab for the component as well.
     *    label("Foo bar baz blah blah blah") {
     *      ttab.caption = "Tab 1"
     *      ttab.icon = VaadinIcons.TAB
     *    }
     * }
     * ```
     */
    val (@VaadinDsl Component).ttab: Tab get() = this@_TabSheet.getTab(ttab@this) ?: throw IllegalStateException("${ttab@this} is not child of ${this@_TabSheet}")
}

fun (@VaadinDsl HasComponents).tabSheet(block: _TabSheet.()->Unit = {}) = init(_TabSheet(), block)
// lastTab is intended to be called from a child of TabSheet; don't annotate with @VaadinDsl
val TabSheet.lastTab: TabSheet.Tab get() = getTab(componentCount - 1)
