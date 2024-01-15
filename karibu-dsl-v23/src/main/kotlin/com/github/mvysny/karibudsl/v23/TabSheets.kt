package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet
import com.vaadin.flow.dom.Element

/**
 * Creates a [TabSheet] component which shows both the list of tabs, and the tab contents itself.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).tabSheet(block: (@VaadinDsl TabSheet).() -> Unit = {}): TabSheet
        = init(TabSheet(), block)

/**
 * Adds a new tab to the tab host, with optional [label] and optional contents.
 *
 * Example:
 * ```
 * tabSheet {
 *   tab("Hello") {
 *     verticalLayout {
 *       span("Hello, world!")
 *     }
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun TabSheet.tab(label: String? = null, block: (@VaadinDsl HasComponents).() -> Unit): Tab {
    var root: Component? = null
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            require(components.size < 2) { "Too many components to add - tab can only host one! ${components.toList()}" }
            val component: Component = components.firstOrNull() ?: return
            check(root == null) { "The content has already been initialized!" }
            root = component
        }
    }
    dummy.block()
    return add(label, root)
}
