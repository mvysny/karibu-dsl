package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.IconFactory
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.RouterLink
import kotlin.reflect.KClass
import com.vaadin.flow.router.RouteParameters

@VaadinDsl
public fun (@VaadinDsl HasComponents).routerLink(icon: IconFactory? = null, text: String? = null, viewType: KClass<out Component>,
                                                 block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    val link = RouterLink(null as String?, viewType.java)
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}

@JvmName("routerLinkWithParams")
@VaadinDsl
public fun (@VaadinDsl HasComponents).routerLink(icon: IconFactory? = null, text: String? = null, viewType: KClass<out Component>,
                                                        parameters: Map<String, String>, block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    val link = RouterLink(null as String?, viewType.java, RouteParameters(parameters))
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}

@JvmName("routerLinkWithParam")
@VaadinDsl
public fun <T, C> (@VaadinDsl HasComponents).routerLink(icon: IconFactory? = null, text: String? = null, viewType: KClass<out C>,
                                                        parameter: T, block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink where C: Component, C: HasUrlParameter<T> {
    val link = RouterLink(null as String?, viewType.java, parameter)
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
public fun (@VaadinDsl HasComponents).routerLink(icon: IconFactory? = null, text: String? = null, block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    // a RouterLink for which the navigation target is not yet known and is set lazily, perhaps in HasUrlParameter.setParameter()
    val link = RouterLink()
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}
