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
fun <R> R.serverClick() where R : Component, R : HasClickListeners<R> {
    fireEvent(HasClickListeners.ClickEvent<R>(this, true))
}

/**
 * Appends a text node with given [text] to the component.
 */
fun (@VaadinDsl HasComponents).text(text: String, block: (@VaadinDsl Text).() -> Unit = {}) = init(Text(text), block)

/**
 * Navigates to given view: `navigateTo<AdminView>()`
 */
inline fun <reified T: Component> navigateTo() {
    UI.getCurrent().apply {
        navigate(router.getUrl(T::class.java))
    }
}

/**
 * Navigates to given view with parameters: `navigateTo<DocumentView>(25L)`
 */
inline fun <C, reified T> navigateTo(vararg params: C) where T: Component, T: HasUrlParameter<C> {
    require(params.isNotEmpty()) { "No parameters passed in" }
    UI.getCurrent().apply {
        val url: String = if (params.size == 1) router.getUrl(T::class.java, params[0]) else router.getUrl(T::class.java, params.toList())
        navigate(url)
    }
}
