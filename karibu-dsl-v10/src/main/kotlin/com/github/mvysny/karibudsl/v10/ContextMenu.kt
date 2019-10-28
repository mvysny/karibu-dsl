package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.HasMenuItems
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem

/**
 * Allows you to define context menu for any component as follows:
 *
 * ```
 * button("foo") {
 *   contextMenu {
 *     item("save", { e -> println("saved") })
 *     item("style") {
 *       item("bold", { e -> println("bold") })
 *       item("italic", { e -> println("italic") })
 *     }
 *     item("clear", { e -> println("clear") })
 *   }
 * }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl Component).contextMenu(block: ContextMenu.()->Unit = {}): ContextMenu {
    val menu = ContextMenu(this)
    menu.block()
    return menu
}

@VaadinDsl
fun (@VaadinDsl HasMenuItems).item(text: String, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                                          block: (@VaadinDsl MenuItem).()->Unit = {}): MenuItem =
        addItem(text, clickListener).apply { block() }

@VaadinDsl
fun (@VaadinDsl MenuItem).item(text: String, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                                          block: (@VaadinDsl MenuItem).()->Unit = {}): MenuItem =
        subMenu.item(text, clickListener, block)

@VaadinDsl
fun (@VaadinDsl HasMenuItems).item(component: Component, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                                          block: (@VaadinDsl MenuItem).()->Unit = {}): MenuItem =
        addItem(component, clickListener).apply { block() }

@VaadinDsl
fun (@VaadinDsl MenuItem).item(component: Component, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                               block: (@VaadinDsl MenuItem).()->Unit = {}): MenuItem =
        subMenu.item(component, clickListener, block)

/**
 * Akin to [Grid.addContextMenu], but allows you to configure the context menu via given [block], as follows:
 * ```
 * grid<Person> {
 *   gridContextMenu {
 *     item("edit", { person -> println("editing $person") })
 *     item("delete", { person -> person.delete(); println("deleted $person") })
 *   }
 * }
 * ```
 *
 * Note that you can attach both [GridContextMenu] and [ContextMenu] to the grid, but that's discouraged since both of
 * those will show on right click and will overlap.
 */
@VaadinDsl
fun <T> (@VaadinDsl Grid<T>).gridContextMenu(block: GridContextMenu<T>.()->Unit = {}): GridContextMenu<T> {
    val menu = addContextMenu()
    menu.block()
    return menu
}

private val <T> (((T?)->Unit)?).toListener: ComponentEventListener<GridContextMenu.GridContextMenuItemClickEvent<T>>? get() = when (this) {
    null -> null
    else -> ComponentEventListener { event -> this@toListener(event.item.orElse(null)) }
}

/**
 * @param clickListener may be invoked with null item if there are not enough rows in the grid and the user clicks the
 * empty space in the grid.
 */
@VaadinDsl
fun <T> (@VaadinDsl GridContextMenu<T>).item(text: String, clickListener: ((T?)->Unit)? = null,
                                   block: (@VaadinDsl GridMenuItem<T>).()->Unit = {}): GridMenuItem<T> =
        addItem(text, clickListener.toListener).apply { block() }

@VaadinDsl
fun <T> (@VaadinDsl GridMenuItem<T>).item(text: String, clickListener: ((T?)->Unit)? = null,
                               block: (@VaadinDsl GridMenuItem<T>).()->Unit = {}): GridMenuItem<T> =
        subMenu.addItem(text, clickListener.toListener).apply { block() }

@VaadinDsl
fun <T> (@VaadinDsl GridContextMenu<T>).item(component: Component, clickListener: ((T?)->Unit)? = null,
                                   block: (@VaadinDsl GridMenuItem<T>).()->Unit = {}): GridMenuItem<T> =
        addItem(component, clickListener.toListener).apply { block() }

@VaadinDsl
fun <T> (@VaadinDsl GridMenuItem<T>).item(component: Component, clickListener: ((T?)->Unit)? = null,
                               block: (@VaadinDsl GridMenuItem<T>).()->Unit = {}): GridMenuItem<T> =
        subMenu.addItem(component, clickListener.toListener).apply { block() }

/**
 * [SubMenu] is leveraged instead of HasMenuItems to provide access to [SubMenu.remove] and [SubMenu.removeAll] instead of [HasComponents.remove] / [HasComponents.removeAll]
 */
@VaadinDsl
fun (@VaadinDsl MenuItem).subMenu(block: (@VaadinDsl SubMenu).() -> Unit): SubMenu = subMenu.apply(block)

@VaadinDsl
fun <T> (@VaadinDsl GridMenuItem<T>).subMenu(block: (@VaadinDsl GridSubMenu<T>).() -> Unit): GridSubMenu<T> = subMenu.apply(block)
