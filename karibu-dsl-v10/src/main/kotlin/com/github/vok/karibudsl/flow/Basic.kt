package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.*
import com.vaadin.flow.router.HasUrlParameter

// annotating DSL functions with @VaadinDsl will make Intellij mark the DSL functions in a special way
// which makes them stand out apart from the common functions, which is very nice.
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
annotation class VaadinDsl

private val fireEventMethod = Component::class.java.getDeclaredMethod("fireEvent", ComponentEvent::class.java)
/**
 * Fires given event on the component.
 */
fun Component.fireEvent(event: ComponentEvent<*>) {
    fireEventMethod.isAccessible = true
    fireEventMethod.invoke(this, event)
}

/**
 * The [Button.click] function sends the click request to the browser, which will then send the click event
 * to the server. This function avoids the roundtrip and works even for browserless testing.
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
