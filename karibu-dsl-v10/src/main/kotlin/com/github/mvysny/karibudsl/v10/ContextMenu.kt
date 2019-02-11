package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.HasMenuItems
import com.vaadin.flow.component.contextmenu.MenuItem

@VaadinDsl
fun (@VaadinDsl Component).contextMenu(block: ContextMenu.()->Unit = {}): ContextMenu {
    val menu = ContextMenu(this)
    menu.block()
    return menu
}

@VaadinDsl
fun <M: HasMenuItems> (@VaadinDsl M).item(text: String, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                                          block: M.()->Unit = {}): MenuItem =
        addItem(text, clickListener).apply { block() }

@VaadinDsl
fun (@VaadinDsl MenuItem).item(text: String, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                                          block: MenuItem.()->Unit = {}): MenuItem =
        subMenu.addItem(text, clickListener).apply { block() }

@VaadinDsl
fun <M: HasMenuItems> (@VaadinDsl M).item(component: Component, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                                          block: M.()->Unit = {}): MenuItem =
        addItem(component, clickListener).apply { block() }

@VaadinDsl
fun (@VaadinDsl MenuItem).item(component: Component, clickListener: ((ClickEvent<MenuItem>)->Unit)? = null,
                               block: MenuItem.()->Unit = {}): MenuItem =
        subMenu.addItem(component, clickListener).apply { block() }
