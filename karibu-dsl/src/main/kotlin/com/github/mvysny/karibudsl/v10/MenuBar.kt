package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.menubar.MenuBar

/**
 * Allows you to define a menu bar as follows:
 *
 * ```
 *   menuBar {
 *     item("save", { e -> println("saved") })
 *     item("style") {
 *       item("bold", { e -> println("bold") })
 *       item("italic", { e -> println("italic") })
 *     }
 *     item("clear", { e -> println("clear") })
 *   }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).menuBar(block: MenuBar.()->Unit = {}): MenuBar = init(MenuBar(), block)
