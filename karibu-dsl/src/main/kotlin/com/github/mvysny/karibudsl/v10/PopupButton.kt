package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.HasSize
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.menubar.MenuBarVariant
import com.vaadin.flow.dom.Element
import java.lang.RuntimeException
import java.util.stream.Stream
import kotlin.streams.toList

/**
 * A popup button: a button with a caption, which, upon clicking, will show a
 * popup with contents.
 * @author mavi@vaadin.com
 */
public class PopupButton(caption: String = "") : KComposite(), HasSize {
    private lateinit var menuItem: MenuItem
    private val menu: MenuBar = ui {
        menuBar {
            menuItem = item("")
        }
    }

    /**
     * The popup button caption.
     */
    public var caption: String
        get() = menuItem.text
        set(caption: String) {
            menuItem.text = caption
        }

    init {
        this.caption = caption
    }

    /**
     * Sets the popup contents, removing any previously set content.
     * @param content the new content to set, not null.
     */
    public fun setPopupContent(content: Component) {
        menuItem.subMenu.removeAll()
        menuItem.subMenu.add(content)
    }

    /**
     * Propagate the content of the popup button via a DSL [block]. Removes all
     * previously set content.
     */
    public fun content(block: HasComponents.() -> Unit) {
        menuItem.subMenu.removeAll()
        block(object : HasComponents {
            override fun getElement(): Element = throw RuntimeException("Unexpected")

            override fun add(vararg components: Component) {
                menuItem.subMenu.add(*components)
            }
        })
    }

    /**
     * Adds theme variants to the component.
     * @param variants theme variants to add
     */
    public fun addThemeVariants(vararg variants: MenuBarVariant) {
        menu.addThemeVariants(*variants)
    }

    /**
     * Closes the popup button. Does nothing if the popup button is already closed.
     */
    public fun close() {
        // workaround for https://github.com/vaadin/vaadin-menu-bar/issues/102
        menu.element.executeJs("this._subMenu.close()")
    }
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).popupButton(caption: String = "", block: PopupButton.()->Unit = {}): PopupButton
        = init(PopupButton(caption), block)
