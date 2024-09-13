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
 *       sideNav {
 *         route(TaskListView::class, VaadinIcon.CHECK)
 *         route(AboutView::class, VaadinIcon.QUESTION)
 *       }
 *     }
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).appLayout(block: (@VaadinDsl AppLayout).() -> Unit = {}): AppLayout
        = init(AppLayout(), block)

/**
 * Allows you to populate the [AppLayout.addToNavbar] in a DSL fashion:
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
public fun (@VaadinDsl AppLayout).navbar(touchOptimized: Boolean = false, block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            addToNavbar(touchOptimized, *components)
        }
    }
    dummy.block()
}

/**
 * Populates the AppLayout drawer slot. `sideNav{}` is the best way to populate the drawer with Vaadin 24.
 */
@VaadinDsl
public fun (@VaadinDsl AppLayout).drawer(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
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
public fun (@VaadinDsl AppLayout).content(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
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
public fun (@VaadinDsl HasComponents).drawerToggle(block: (@VaadinDsl DrawerToggle).() -> Unit = {}): DrawerToggle
        = init(DrawerToggle(), block)
