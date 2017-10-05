package com.github.vok.karibudsl.flow

import com.vaadin.router.RouterLink
import com.vaadin.ui.Component
import com.vaadin.ui.common.HasComponents
import com.vaadin.ui.icon.VaadinIcons

fun (@VaadinDsl HasComponents).routerLink(icon: VaadinIcons? = null, text: String? = null, viewType: Class<out Component>,
                                          block: (@VaadinDsl RouterLink).() -> Unit = {}): RouterLink {
    val link = RouterLink(null, viewType)
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}
