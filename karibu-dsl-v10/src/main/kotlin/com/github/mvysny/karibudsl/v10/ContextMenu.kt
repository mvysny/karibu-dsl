package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.HasMenuItems
import com.vaadin.flow.component.contextmenu.MenuItem

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
        subMenu.addItem(text, clickListener).apply { block() }

@VaadinDsl
fun (@VaadinDsl HasMenuItems).item(component: Component, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                                          block: (@VaadinDsl MenuItem).()->Unit = {}): MenuItem =
        addItem(component, clickListener).apply { block() }

@VaadinDsl
fun (@VaadinDsl MenuItem).item(component: Component, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                               block: (@VaadinDsl MenuItem).()->Unit = {}): MenuItem =
        subMenu.addItem(component, clickListener).apply { block() }
