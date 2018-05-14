package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.*
import com.vaadin.flow.router.HasUrlParameter

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
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
fun (@VaadinDsl HasComponents).text(text: String, block: (@VaadinDsl Text).() -> Unit = {}) = init(Text(text), block)

/**
 * Navigates to given view: `navigateToView<AdminView>()`
 */
inline fun <reified T: Component> navigateToView() {
    UI.getCurrent().apply {
        navigate(router.getUrl(T::class.java))
    }
}

/**
 * Navigates to given view with parameters: `navigateToView<DocumentView>(25L)`
 */
inline fun <C, reified T> navigateToView(vararg params: C) where T: Component, T: HasUrlParameter<C> {
    require(params.isNotEmpty()) { "No parameters passed in" }
    UI.getCurrent().apply {
        val url: String = if (params.size == 1) router.getUrl(T::class.java, params[0]) else router.getUrl(T::class.java, params.toList())
        navigate(url)
    }
}

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
