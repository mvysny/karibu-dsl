package com.github.vok.karibudsl.flow

import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.VaadinIcons

fun (@VaadinDsl HasComponents).routerLink(icon: VaadinIcons? = null, text: String? = null, viewType: Class<out Component>,
                                          block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    val link = RouterLink(null, viewType)
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}
