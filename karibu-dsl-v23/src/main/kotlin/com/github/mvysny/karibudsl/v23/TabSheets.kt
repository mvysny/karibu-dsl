package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.buildSingleComponent
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v10.buildSingleComponentOrNull
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet

/**
 * Creates a [TabSheet] component which shows both the list of tabs, and the tab contents itself.
 *
 * Example:
 * ```
 * tabSheet {
 *   tab("Dashboard") {
 *     span("This is the Dashboard tab content")
 *   }
 *   tab("Payment") {
 *     span("This is the Payment tab content")
 *   }
 * }
 * ```
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
    var root: Component? = buildSingleComponent("TabSheet.tab{}", block)
    return add(label, root)
}
