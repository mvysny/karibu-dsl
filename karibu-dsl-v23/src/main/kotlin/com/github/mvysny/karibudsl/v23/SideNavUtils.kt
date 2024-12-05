package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.appLayout
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.RouteParameters
import kotlin.reflect.KClass

private fun getRouteTitle(routeClass: KClass<*>): String {
    val title = routeClass.java.getAnnotation(PageTitle::class.java)
    return title?.value ?: routeClass.simpleName ?: ""
}

/**
 * Creates the [SideNav] component, used to show left side navigation bar.
 * See [appLayout] on how to nest [SideNav] into an [com.vaadin.flow.component.applayout.AppLayout]
 * component.
 *
 * Example:
 * ```
 * sideNav("Messages") {
 *   route(MainRoute::class, VaadinIcon.INBOX, "Inbox")
 *   route(MainRoute::class, VaadinIcon.PAPERPLANE, "Sent")
 *   route(MainRoute::class, VaadinIcon.TRASH, "Trash")
 * }
 * sideNav("Admin") {
 *   isCollapsible = true
 *   route(MainRoute::class, VaadinIcon.GROUP, "Users")
 *   route(MainRoute::class, VaadinIcon.KEY, "Permissions")
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).sideNav(
    label: String? = null,
    block: (@VaadinDsl SideNav).() -> Unit = {}
): SideNav =
    init(SideNav(label), block)

/**
 * Adds navigation menu item(s) to the menu.
 * @param routeClass the route class which this item links to
 * @param icon optional icon for this menu item.
 * @param label the title of the route; if absent, the title is read from the [PageTitle] annotation.
 * @param routeParameters optional route parameters, defaults to empty.
 */
@VaadinDsl
public fun (@VaadinDsl SideNav).route(
    routeClass: KClass<out Component>,
    icon: VaadinIcon? = null,
    label: String = getRouteTitle(routeClass),
    routeParameters: RouteParameters = RouteParameters.empty(),
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(label, routeClass.java, icon?.create())
    block(item)
    addItem(item)
    return item
}

/**
 * Adds navigation menu item(s) to the menu.
 * @param label the label for the item
 * @param path the optional path to link to, usually an external link such as `https://vaadin.com`.
 */
@VaadinDsl
public fun (@VaadinDsl SideNav).item(
    label: String,
    path: String? = null,
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(label, path)
    block(item)
    addItem(item)
    return item
}

/**
 * Adds navigation menu item(s) to the menu.
 * @param routeClass the route class which this item links to
 * @param icon optional icon for this menu item.
 * @param label the title of the route; if absent, the title is read from the [PageTitle] annotation.
 * @param routeParameters optional route parameters, defaults to empty.
 */
@VaadinDsl
public fun (@VaadinDsl SideNavItem).route(
    routeClass: KClass<out Component>,
    icon: VaadinIcon? = null,
    label: String = getRouteTitle(routeClass),
    routeParameters: RouteParameters = RouteParameters.empty(),
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(label, routeClass.java, routeParameters, icon?.create())
    block(item)
    addItem(item)
    return item
}

/**
 * Adds navigation menu item(s) to the menu.
 * @param label the label for the item
 * @param path the optional path to link to, usually an external link such as `https://vaadin.com`.
 */
@VaadinDsl
public fun (@VaadinDsl SideNavItem).item(
    label: String,
    path: String? = null,
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(label, path)
    block(item)
    addItem(item)
    return item
}
