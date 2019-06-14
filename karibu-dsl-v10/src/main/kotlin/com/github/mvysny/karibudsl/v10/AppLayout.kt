package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.dom.Element

/**
 * Creates an [App Layout](https://vaadin.com/components/vaadin-app-layout). Example:
 *
 * ```
 * class MainLayout : AppLayout() {
 *   init {
 *     isDrawerOpened = false
 *     navbar {
 *       drawerToggle()
 *       h3("Beverage Buddy")
 *     }
 *     drawer {
 *       div {
 *         routerLink(VaadinIcon.LIST, "Reviews", ReviewsList::class)
 *       }
 *       div {
 *         routerLink(VaadinIcon.ARCHIVES, "Categories", CategoriesList::class)
 *       }
 *     }
 *   }
 * }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).appLayout(block: (@VaadinDsl AppLayout).() -> Unit = {}) = init(AppLayout(), block)

/**
 * Allows you to populate the [AppLayout.addToNavbar] in a DSL fashion:
 * ```
 * appLayout {
 *   navbar { h3("My App") }
 * }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl AppLayout).navbar(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            addToNavbar(*components)
        }
    }
    dummy.block()
}

/**
 * Populates the AppLayout drawer slot.
 */
@VaadinDsl
fun (@VaadinDsl AppLayout).drawer(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            addToDrawer(*components)
        }
    }
    dummy.block()
}

/**
 * Allows you to set the [AppLayout.setContent] in a DSL fashion.
 *
 * The [AppLayout] implements [RouterLayout] and is able to automatically populate itself with the view's contents.
 * If you wish to take advantage of this feature, just make your main layout extend the [AppLayout] class.
 */
@VaadinDsl
fun (@VaadinDsl AppLayout).content(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            require(components.size < 2) { "Too many components to add - AppLayout content can only host one! ${components.toList()}" }
            val component = components.firstOrNull() ?: return
            check(this@content.content == null) { "The content has already been initialized!" }
            this@content.setContent(component)
        }
    }
    dummy.block()
}

/**
 * Adds the [DrawerToggle] to your [AppLayout]:
 * ```
 * appLayout {
 *   navbar {
 *     drawerToggle()
 *     h3("My App")
 *   }
 * }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).drawerToggle(block: (@VaadinDsl DrawerToggle).() -> Unit = {}) = init(DrawerToggle(), block)
