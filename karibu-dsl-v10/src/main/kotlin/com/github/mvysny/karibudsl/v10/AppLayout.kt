package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.AppLayoutMenu
import com.vaadin.flow.component.applayout.AppLayoutMenuItem
import com.vaadin.flow.dom.Element
import com.vaadin.flow.router.*
import kotlin.reflect.KClass

/**
 * Creates an [App Layout](https://vaadin.com/components/vaadin-app-layout). Example:
 *
 * ```kotlin
 * class MainLayout : KComposite(), RouterLayout {
 *   private lateinit var appLayoutMenu: AppLayoutMenu
 *   private val root = ui {
 *     appLayout {
 *       branding { h3("Beverage Buddy") }
 *       appLayoutMenu = withVaadinMenu {
 *         item("Reviews", Icon(VaadinIcon.LIST)) {
 *           setRoute(ReviewsList::class)
 *         }
 *         item("Categories", Icon(VaadinIcon.ARCHIVES)) {
 *           setRoute(CategoriesList::class)
 *         }
 *       }
 *     }
 *   }
 *
 *   override fun showRouterLayoutContent(content: HasElement) {
 *     root.showRouterLayoutContent(content, appLayoutMenu)
 *     content.element.classList.add("main-layout")
 *   }
 * }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).appLayout(block: (@VaadinDsl AppLayout).() -> Unit = {}) = init(AppLayout(), block)

@VaadinDsl
fun (@VaadinDsl AppLayout).branding(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            require(components.size < 2) { "Too many components to add - AppLayout branding can only host one! ${components.toList()}" }
            val component = components.firstOrNull() ?: return
            this@branding.setBranding(component)
        }
    }
    dummy.block()
}

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

@VaadinDsl
fun (@VaadinDsl AppLayout).menu(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            require(components.size < 2) { "Too many components to add - AppLayout menu can only host one! ${components.toList()}" }
            val component = components.firstOrNull() ?: return
            check(this@menu.menu == null) { "The menu has already been initialized!" }
            this@menu.setMenu(component)
        }
    }
    dummy.block()
}

@VaadinDsl
fun (@VaadinDsl HasComponents).appLayoutMenu(block: (@VaadinDsl AppLayoutMenu).() -> Unit = {}) = AppLayoutMenu().apply {
    this@appLayoutMenu.element
}

@VaadinDsl
fun (@VaadinDsl AppLayout).withVaadinMenu(block: (@VaadinDsl AppLayoutMenu).() -> Unit = {}): AppLayoutMenu {
    val menu: AppLayoutMenu = createMenu()
    menu.block()
    return menu
}

@VaadinDsl
fun (@VaadinDsl AppLayoutMenu).item(title: String? = null, icon: Component? = null, route: String? = null, block: (@VaadinDsl AppLayoutMenuItem).() -> Unit = {}): AppLayoutMenuItem {
    val item: AppLayoutMenuItem = addMenuItem(icon, title, route)
    item.block()
    return item
}

fun AppLayoutMenuItem.setRoute(navigationTarget: KClass<out Component>) {
    val link = RouterLink("foo", navigationTarget.java)
    route = link.href
}

/**
 * Call this from your main layout which implements [RouterLayout] and overrides [RouterLayout.showRouterLayoutContent].
 * See [appLayout] for a concrete example.
 */
fun AppLayout.showRouterLayoutContent(content: HasElement, menu: AppLayoutMenu) {
    val component: Component = content.getElement().getComponent().get()
    if (component is RouteNotFoundError) {
        menu.selectMenuItem(null)
    } else {
        val target = UI.getCurrent().router.getUrl(component.javaClass)

        menu.getMenuItemTargetingRoute(target!!)
                .ifPresent({ item -> menu.selectMenuItem(item) })
    }
    setContent(content.element)
}
