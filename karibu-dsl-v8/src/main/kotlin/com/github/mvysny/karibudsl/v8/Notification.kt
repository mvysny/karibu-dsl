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

var Notification.isDark: Boolean
    get() = styleNames.contains("dark")
    set(value) {
        styleNames.set("dark", value)
    }

var Notification.isSuccess: Boolean
    get() = styleNames.contains("success")
    set(value) {
        styleNames.set("success", value)
    }

var Notification.isFailure: Boolean
    get() = styleNames.contains("failure")
    set(value) {
        styleNames.set("failure", value)
    }

var Notification.isBar: Boolean
    get() = styleNames.contains("bar")
    set(value) {
        styleNames.set("bar", value)
    }

var Notification.isSmall: Boolean
    get() = styleNames.contains("small")
    set(value) {
        styleNames.set("small", value)
    }

var Notification.isClosable: Boolean
    get() = styleNames.contains("closable")
    set(value) {
        styleNames.set("closable", value)
    }
