package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.*
import com.vaadin.flow.dom.ClassList
import com.vaadin.flow.dom.DomEventListener
import com.vaadin.flow.dom.DomListenerRegistration
import com.vaadin.flow.dom.Element

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
var Component.tooltip: String?
    get() = element.getAttribute("title")
    set(value) { element.setOrRemoveAttribute("title", value) }

/**
 * Adds the right-click (context-menu) [listener] to the component. Also causes the right-click browser
 * menu not to be shown on this component (see [preventDefault]).
 */
fun Component.addContextMenuListener(listener: DomEventListener): DomListenerRegistration =
        element.addEventListener("contextmenu", listener)
                .preventDefault()

/**
 * Makes the client-side listener call [Event.preventDefault()](https://developer.mozilla.org/en-US/docs/Web/API/Event/preventDefault)
 * on the event.
 *
 * @return this
 */
fun DomListenerRegistration.preventDefault(): DomListenerRegistration = addEventData("event.preventDefault()")

/**
 * Removes the component from its parent. Does nothing if the component is not attached to a parent.
 */
fun Component.removeFromParent() {
    (parent.orElse(null) as? HasComponents)?.remove(this)
}

/**
 * Toggles [className] - removes it if it was there, or adds it if it wasn't there.
 * @param className the class name to toggle, cannot contain spaces.
 */
fun ClassList.toggle(className: String) {
    require(!className.containsWhitespace()) { "'$className' cannot contain whitespace" }
    set(className, !contains(className))
}
