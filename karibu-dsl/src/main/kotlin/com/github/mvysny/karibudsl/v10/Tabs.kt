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
 * Store/Retrieve the state into/from [Tabs] via [ComponentUtil.setData] ()/[ComponentUtil.getData] ()
 */
private val (@VaadinDsl Tabs).onSelectedHandlers: MutableMap<Tab, OnTabSelectedHandler>
    get() =
        "onSelectedHandlers".let {key ->
            @Suppress("UNCHECKED_CAST")
            (ComponentUtil.getData(this, key) ?: mutableMapOf<Tab, OnTabSelectedHandler>().also { map ->
                ComponentUtil.setData(this, key, map)
                addSelectedChangeListener { map[it.selectedTab]?.invoke() }
            }) as MutableMap<Tab, OnTabSelectedHandler>
        }


/**
 * [onSelected] is a utility that facilitates event handling when a tab is selected.
 *
 * Example of usage:
 * ```kotlin
 * tabs {
 *
 *          tab("Users") {
 *              onSelected {
 *                  displayUserList()
 *              }
 *          }
 *          tab("Admins") {
 *              onSelected {
 *                  displayAdminList()
 *              }
 *          }
 *          ...
 *
 *      defaultOnSelectedHandler()
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl Tab).onSelected(handler: OnTabSelectedHandler) : OnTabSelectedHandler {
    val tabs = parent.get() as Tabs
    tabs.onSelectedHandlers[this] = handler
    return handler
}

/**
 * By default [Tabs] does not invoke onSelected for the first [Tab].
 * Add defaultOnSelected() after all tabs have been added.
 *
 * To trigger onSelected for other tabs just: selectedTab = someTab
 */
@VaadinDsl
public fun (@VaadinDsl Tabs).defaultOnSelectedHandler() {
    onSelectedHandlers.values.toList().firstOrNull()?.invoke()
}
