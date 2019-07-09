package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.*
import com.vaadin.flow.dom.Element

// annotating DSL functions with @VaadinDsl will make Intellij mark the DSL functions in a special way
// which makes them stand out apart from the common functions, which is very nice.
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
annotation class VaadinDsl

/**
 * Fires given event on the component.
 */
fun Component.fireEvent(event: ComponentEvent<*>) {
    ComponentUtil.fireEvent(this, event)
}

/**
 * Adds [com.vaadin.flow.component.button.Button.click] functionality to all [ClickNotifier]s. This function directly calls
 * all click listeners, thus it avoids the roundtrip to client and back. It even works with browserless testing.
 */
fun <R> R.serverClick() where R : Component, R : ClickNotifier<R> {
    fireEvent(ClickEvent<R>(this, true, -1, -1, -1, -1, 1, -1, false, false, false, false))
}

/**
 * Appends a text node with given [text] to the component.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).text(text: String, block: (@VaadinDsl Text).() -> Unit = {}) = init(Text(text), block)

/**
 * Sets the alignment of the text in the component. One of `center`, `left`, `right`, `justify`.
 */
var Component.textAlign: String?
    get() = element.style.get("textAlign")
    set(value) { element.style.set("textAlign", value) }

/**
 * Sets the "min-width" style, or removes it.
 */
var HasSize.minWidth: String?
    get() = element.style.get("minWidth")
    set(value) { element.style.set("minWidth", value) }

/**
 * Sets the "max-width" style, or removes it.
 */
var HasSize.maxWidth: String?
    get() = element.style.get("maxWidth")
    set(value) { element.style.set("maxWidth", value) }

/**
 * Either calls [Element.setAttribute] (if the [value] is not null), or
 * [Element.removeAttribute] (if the [value] is null).
 * @param attribute the name of the attribute.
 */
fun Element.setOrRemoveAttribute(attribute: String, value: String?) {
    if (value == null) {
        removeAttribute(attribute)
    } else {
        setAttribute(attribute, value)
    }
}

/**
 * Sets or removes the `title` attribute on component's element.
 */
var Component.title: String?
    get() = element.getAttribute("title")
    set(value) { element.setOrRemoveAttribute("title", value) }
