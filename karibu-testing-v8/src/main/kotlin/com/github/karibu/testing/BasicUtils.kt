package com.github.karibu.testing

import com.vaadin.server.AbstractClientConnector
import java.util.*

/**
 * Allows us to fire any Vaadin event on any Vaadin component.
 * @receiver the component, not null.
 * @param event the event, not null.
 */
fun AbstractClientConnector._fireEvent(event: EventObject) {
    // fireEvent() is protected, gotta make it public
    val fireEvent = AbstractClientConnector::class.java.getDeclaredMethod("fireEvent", EventObject::class.java)
    fireEvent.isAccessible = true
    fireEvent.invoke(this, event)
}

val IntRange.size: Int get() = (endInclusive + 1 - start).coerceAtLeast(0)
