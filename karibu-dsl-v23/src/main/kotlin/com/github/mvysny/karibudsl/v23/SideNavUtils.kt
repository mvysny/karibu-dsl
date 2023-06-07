package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.PageTitle
import kotlin.reflect.KClass

private fun getRouteTitle(routeClass: KClass<*>): String {
    val title = routeClass.java.getAnnotation(PageTitle::class.java)
    return title?.value ?: routeClass.simpleName ?: ""
}

/**
 * Creates the [SideNav] component, used to show left side navigation bar. Example:
 * ```
 * appLayout {
 *     navbar {
 *         drawerToggle()
 *     }
 *     drawer {
 *         sideNav {
 *             route(TaskListView::class, VaadinIcon.CHECK)
 *             route(AboutView::class, VaadinIcon.QUESTION)
 *         }
 *     }
 *     content {
 *         // ...
 *     }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).sideNav(
    label: String? = null,
    block: (@VaadinDsl SideNav).() -> Unit = {}
): SideNav =
    init(SideNav(label), block)

@VaadinDsl
public fun (@VaadinDsl SideNav).route(
    routeClass: KClass<out Component>,
    icon: VaadinIcon? = null,
    title: String = getRouteTitle(routeClass),
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(title, routeClass.java, icon?.create())
    block(item)
    addItem(item)
    return item
}

@VaadinDsl
public fun (@VaadinDsl SideNav).item(
    title: String,
    path: String? = null,
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(title, path)
    block(item)
    addItem(item)
    return item
}

@VaadinDsl
public fun (@VaadinDsl SideNavItem).route(
    routeClass: KClass<out Component>,
    icon: VaadinIcon? = null,
    title: String = getRouteTitle(routeClass),
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(title, routeClass.java, icon?.create())
    block(item)
    addItem(item)
    return item
}

@VaadinDsl
public fun (@VaadinDsl SideNavItem).item(
    title: String,
    path: String? = null,
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(title, path)
    block(item)
    addItem(item)
    return item
}
