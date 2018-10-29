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
     *      tab.caption = "Tab 1"
     *      tab.icon = VaadinIcons.TAB
     *    }
     * }
     * ```
     */
    @get:JvmName("getTab2")
    val (@VaadinDsl Component).tab: Tab get() = this@_TabSheet.getTab(this) ?: throw IllegalStateException("$this is not child of ${this@_TabSheet}")
}

@VaadinDsl
fun (@VaadinDsl HasComponents).tabSheet(block: _TabSheet.()->Unit = {}) = init(_TabSheet(), block)
// lastTab is intended to be called from a child of TabSheet; don't annotate with @VaadinDsl
val TabSheet.lastTab: TabSheet.Tab get() = getTab(componentCount - 1)
