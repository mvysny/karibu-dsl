package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.IconFactory
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs

/**
 * [Tabs](https://vaadin.com/docs/latest/components/tabs) are used to organize and group content into sections that the user can navigate.
 * ```
 * tabs {
 *   tab("Details")
 *   tab("Payment")
 *   tab("Shipping")
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).tabs(orientation: Tabs.Orientation? = null, block: (@VaadinDsl Tabs).() -> Unit = {}): Tabs {
    val component = Tabs()
    if (orientation != null) {
        component.orientation = orientation
    }
    return init(component, block)
}

/**
 * Adds a tab with given [label] and [icon] to the [Tabs] component. See [tabs] for an example.
 * @param icon optional icon to use; usually `VaadinIcon`.
 */
@VaadinDsl
public fun (@VaadinDsl Tabs).tab(label: String? = null, icon: IconFactory? = null, block: (@VaadinDsl Tab).() -> Unit = {}): Tab {
    val tab = Tab(label)
    if (icon != null) tab.icon(icon)
    add(tab)
    tab.block()
    return tab
}

public typealias OnTabSelectedHandler = () -> Unit

/**
 * Store/Retrieve the state into/from [Tabs] via [ComponentUtil.setData]/[ComponentUtil.getData]
 */
private val (@VaadinDsl Tabs).onSelectedHandlers: MutableMap<Tab, OnTabSelectedHandler>
    get() = data("onSelectedHandlers") {
        val handlers = mutableMapOf<Tab, OnTabSelectedHandler>()
        addSelectedChangeListener { handlers[it.selectedTab]?.invoke() }
        handlers
    }

/**
 * [onSelected] is a utility that facilitates event handling when a tab is selected.
 *
 * Example of usage:
 * ```kotlin
 * tabs {
 *   tab("Users") {
 *     onSelected {
 *       displayUserList()
 *     }
 *   }
 *   tab("Admins") {
 *     onSelected {
 *       displayAdminList()
 *     }
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl Tab).onSelected(handler: OnTabSelectedHandler) : OnTabSelectedHandler {
    val tabs = parent.get() as Tabs
    tabs.onSelectedHandlers[this] = handler
    if (tabs.selectedTab == this) {
        handler()
    }
    return handler
}
