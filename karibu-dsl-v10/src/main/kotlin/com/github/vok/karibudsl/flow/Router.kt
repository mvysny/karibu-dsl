package com.github.vok.karibudsl.flow

import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcons
import com.vaadin.flow.internal.StateTree
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Router
import com.vaadin.flow.server.VaadinService
import kotlin.reflect.KClass

/**
 * Navigates to given view: `navigateToView<AdminView>()`
 */
inline fun <reified T: Component> navigateToView() = navigateToView(T::class)

/**
 * Navigates to given view: `navigateToView(AdminView::class)`
 */
fun navigateToView(viewType: KClass<out Component>) {
    UI.getCurrent().apply {
        navigate(router.getUrl(viewType.java))
    }
}

/**
 * Navigates to given view with parameters: `navigateToView(DocumentView::class, 25L)`.
 * @param params typically one parameter, but may be empty in case of view's optional parameter.
 */
fun <C, T> navigateToView(viewType: KClass<out T>, vararg params: C) where T: Component, T: HasUrlParameter<C> {
    // don't use this fun with reified C - when there is a parameter T, that would require the user to write something like this:
    // navigateToView<Long, EditArticleView>(article.id!!)   // note the Long
    require(params.isNotEmpty()) { "No parameters passed in" }
    UI.getCurrent().apply {
        val url: String = if (params.size == 1) router.getUrl(viewType.java, params[0]) else router.getUrl(viewType.java, params.toList())
        navigate(url)
    }
}

fun (@VaadinDsl HasComponents).routerLink(icon: VaadinIcons? = null, text: String? = null, viewType: KClass<out Component>,
                                          block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    val link = RouterLink(null, viewType.java)
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}

@JvmName("routerLinkWithParam")
fun <T, C> (@VaadinDsl HasComponents).routerLink(icon: VaadinIcons? = null, text: String? = null, viewType: KClass<out C>,
                                          parameter: T, block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink where C: Component, C: HasUrlParameter<T> {
    val link = RouterLink(null, viewType.java, parameter)
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}

// don't add routerLink with reified C - when there is a parameter T, that would require the user to write something like this:
// routerLink<Long, EditArticleView>(null, "Edit", article.id!!)   // note the Long
//
// with the current approach it's
// routerLink(null, "Edit", EditArticleView::class, article.id)    // no need for Long, compiler will infer it.


fun (@VaadinDsl HasComponents).routerLink(icon: VaadinIcons? = null, text: String? = null, block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    // a RouterLink for which the navigation target is not yet known and is set lazily, perhaps in HasUrlParameter.setParameter()
    val link = RouterLink()
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}

private fun RouterLink.getRouter(): Router {
    var router: Router? = null
    if (element.node.isAttached) {
        val tree = element.node.owner as StateTree
        router = tree.ui.router
    }
    if (router == null) {
        router = VaadinService.getCurrent().router
    }
    if (router == null) {
        throw IllegalStateException("Implicit router instance is not available. Use overloaded method with explicit router parameter.")
    }
    return router
}
/**
 * Set the [navigationTarget] for this link.
 */
fun RouterLink.setRoute(navigationTarget: KClass<out Component>) = setRoute(getRouter(), navigationTarget.java)
/**
 * Set the [navigationTarget] for this link.
 * @param parameter url parameter for navigation target
 * @param T url parameter type
 * @param C navigation target type
 */
fun <T, C> RouterLink.setRoute(navigationTarget: KClass<out C>, parameter: T) where C: Component, C: HasUrlParameter<T> {
    setRoute(getRouter(), navigationTarget.java, parameter)
}
