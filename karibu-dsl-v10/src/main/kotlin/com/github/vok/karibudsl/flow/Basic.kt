package com.github.vok.karibudsl.flow

import com.vaadin.ui.Component
import com.vaadin.ui.common.HasClickListeners
import com.vaadin.ui.event.ComponentEvent

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@DslMarker
annotation class VaadinDsl

private val fireEventMethod = Component::class.java.getDeclaredMethod("fireEvent", ComponentEvent::class.java)
fun Component.fireEvent(event: ComponentEvent<*>) {
    fireEventMethod.isAccessible = true
    fireEventMethod.invoke(this, event)
}
fun <R> R.serverClick() where R : Component, R : HasClickListeners<R> {
    fireEvent(HasClickListeners.ClickEvent<R>(this, true))
}
