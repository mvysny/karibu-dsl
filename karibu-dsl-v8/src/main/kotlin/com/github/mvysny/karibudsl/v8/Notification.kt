package com.github.mvysny.karibudsl.v8

import com.vaadin.server.Page
import com.vaadin.ui.Notification

/**
 * Clones the notification so that it could be shown multiple times.
 */
fun Notification.clone(): Notification = Notification(caption, description).also {
    it.isHtmlContentAllowed = isHtmlContentAllowed
    it.delayMsec = delayMsec
    it.icon = icon
    it.position = position
    it.styleName = styleName
}

/**
 * Shows this notification on the current page.
 */
fun Notification.show() {
    show(Page.getCurrent())
}
