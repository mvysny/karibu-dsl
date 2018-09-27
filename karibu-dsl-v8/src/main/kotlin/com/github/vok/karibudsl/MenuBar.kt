package com.github.vok.karibudsl

import com.vaadin.server.Resource
import com.vaadin.ui.HasComponents
import com.vaadin.ui.MenuBar

@VaadinDsl
fun (@VaadinDsl HasComponents).menuBar(block: (@VaadinDsl MenuBar).()->Unit = {}) = init(MenuBar(), block)
@VaadinDsl
fun (@VaadinDsl MenuBar).item(caption: String, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}) =
        item(caption, null, menuSelected, block)
@VaadinDsl
fun (@VaadinDsl MenuBar).item(caption: String, icon: Resource? = null, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar.MenuItem =
        addItem(caption, icon, if (menuSelected == null) null else MenuBar.Command { menuSelected(it) }).apply { block() }
@VaadinDsl
fun (@VaadinDsl MenuBar.MenuItem).item(caption: String, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}) =
        item(caption, null, menuSelected, block)
@VaadinDsl
fun (@VaadinDsl MenuBar.MenuItem).item(caption: String, icon: Resource? = null, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar.MenuItem =
        addItem(caption, icon, if (menuSelected == null) null else MenuBar.Command { menuSelected(it) }).apply { block() }

/**
 * A drop-down button emulated via a [MenuBar]. See [https://demo.vaadin.com/valo-theme/#!menubars] for demo and details.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).dropDownButton(caption: String, splitButton: Boolean = false, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar = init(MenuBar()) {
    var item = item(caption)
    if (splitButton) item = item("")
    item.block()
}
