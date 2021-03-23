package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.*
import com.vaadin.flow.server.VaadinService
import java.util.*
import kotlin.reflect.KClass

/**
 * Navigates to given view: `navigateToView<AdminView>()`
 */
public inline fun <reified T: Component> navigateToView() {
    navigateToView(T::class)
}

/**
 * Navigates to given view: `navigateToView(AdminView::class)`
 */
public fun navigateToView(viewType: KClass<out Component>) {
    UI.getCurrent().navigate(viewType.java)
}

/**
 * Navigates to given view with parameters: `navigateToView(DocumentView::class, 25L)`.
 * @param param typically a non-null parameter, but may be null in case of view's optional parameter.
 */
public fun <C, T> navigateToView(viewType: KClass<out T>, param: C?) where T: Component, T: HasUrlParameter<C> {
    // don't use this fun with reified C - when there is a parameter T, that would require the user to write something like this:
    // navigateToView<Long, EditArticleView>(article.id!!)   // note the Long
    UI.getCurrent().navigate(viewType.java, param)
}

public fun RouterLink.navigateTo() {
    UI.getCurrent().navigate(href)
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).routerLink(icon: VaadinIcon? = null, text: String? = null, viewType: KClass<out Component>,
                                          block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    val link = RouterLink(null, viewType.java)
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link////
}

@JvmName("routerLinkWithParam")
@VaadinDsl
public fun <T, C> (@VaadinDsl HasComponents).routerLink(icon: VaadinIcon? = null, text: String? = null, viewType: KClass<out C>,
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


@VaadinDsl
public fun (@VaadinDsl HasComponents).routerLink(icon: VaadinIcon? = null, text: String? = null, block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    // a RouterLink for which the navigation target is not yet known and is set lazily, perhaps in HasUrlParameter.setParameter()
    val link = RouterLink()
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}

/**
 * Returns [UI.getRouter]/[VaadinService.router], whichever returns a non-null value.
 */
private fun getRouter(): Router {
    var router: Router? = UI.getCurrent()?.router
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
public fun RouterLink.setRoute(navigationTarget: KClass<out Component>) {
    setRoute(getRouter(), navigationTarget.java)
}
/**
 * Set the [navigationTarget] for this link.
 * @param parameter url parameter for navigation target
 * @param T url parameter type
 * @param C navigation target type
 */
public fun <T, C> RouterLink.setRoute(navigationTarget: KClass<out C>, parameter: T) where C: Component, C: HasUrlParameter<T> {
    setRoute(getRouter(), navigationTarget.java, parameter)
}

/**
 * Returns the navigated-to view class.
 */
public val AfterNavigationEvent.viewClass: Class<out Component>? get() =
    (activeChain.first() as Component).javaClass

/**
 * Finds a view mapped to this location.
 * @param router router to use, defaults to [UI.getRouter]/[VaadinService.router].
 */
public fun Location.getViewClass(router: Router = getRouter()): Class<out Component>? {
    val navigationTarget: Optional<NavigationState> =
            router.resolveNavigationTarget("/$path", queryParameters.parameters.mapValues { it.value.toTypedArray() })
    return navigationTarget.orElse(null)?.navigationTarget
}
