package com.github.vok.karibudsl

import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents
import com.vaadin.ui.Label
import com.vaadin.ui.PopupView

@VaadinDsl
fun (@VaadinDsl HasComponents).popupView(small: String? = null, block: (@VaadinDsl PopupView).()->Unit = {}): PopupView {
    val result = init(PopupView(SimpleContent.EMPTY), block)
    if (small != null) result.minimizedValueAsHTML = small
    return result
}

data class SimpleContent(val small: String, val large: Component) : PopupView.Content {
    companion object {
        val EMPTY = SimpleContent("", Label(""))
    }

    constructor(content: PopupView.Content) : this(content.minimizedValueAsHTML, content.popupComponent)

    override fun getPopupComponent(): Component? = large

    override fun getMinimizedValueAsHTML() = small
}

private var (@VaadinDsl PopupView).simpleContent: SimpleContent
    get() {
        val content = content
        return content as? SimpleContent ?: SimpleContent(content)
    }
    set(value) {
        content = value
    }

/**
 * Allows you to set the popup component directly, without changing [minimizedValueAsHTML]
 */
var (@VaadinDsl PopupView).popupComponent: Component
    get() = content.popupComponent
    set(value) {
        content = simpleContent.copy(large = value)
    }

/**
 * Allows you to set the minimized text directly, without changing [popupComponent]
 */
var (@VaadinDsl PopupView).minimizedValueAsHTML: String
    get() = content.minimizedValueAsHTML
    set(value) {
        content = simpleContent.copy(small = value)
    }

