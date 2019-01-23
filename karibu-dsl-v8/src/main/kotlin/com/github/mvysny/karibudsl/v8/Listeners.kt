package com.github.mvysny.karibudsl.v8

import com.vaadin.event.LayoutEvents
import com.vaadin.event.MouseEvents
import com.vaadin.server.AbstractClientConnector
import com.vaadin.shared.MouseEventDetails
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.Image

private fun Component.getListenersHandling(eventType: Class<*>): List<*> =
        if (this is AbstractClientConnector) this.getListeners(eventType).toList() else listOf<Any>()

private fun Component.hasListenersHandling(eventType: Class<*>) = !getListenersHandling(eventType).isEmpty()

private fun Component.hasClickListeners() = hasListenersHandling(MouseEvents.ClickEvent::class.java) || hasListenersHandling(Button.ClickEvent::class.java)

private fun Component.getAscendantLayoutWithLayoutClickNotifier(): LayoutEvents.LayoutClickNotifier? {
    var component: Component? = this
    while (component != null) {
        if (component is LayoutEvents.LayoutClickNotifier && component.hasListenersHandling(LayoutEvents.LayoutClickEvent::class.java)) {
            return component
        }
        component = component.parent
    }
    return null
}

/**
 * Sets a click listener to a layout. The click listener will be called when the layout and any descendant component is clicked,
 * except for descendants which have their own click listeners attached. Note that Vaadin does not fire this event e.g. when clicking
 * on children's captions, so this is not 100% perfect.
 *
 * Removes any previously attached layout click listeners
 * @param listener the click listener.
 */
fun (@VaadinDsl LayoutEvents.LayoutClickNotifier).onLayoutClick(listener: (LayoutEvents.LayoutClickEvent)->Unit) {
    (this as AbstractClientConnector).getListeners(LayoutEvents.LayoutClickEvent::class.java).toList().forEach {
        @Suppress("DEPRECATION")
        removeLayoutClickListener(it as LayoutEvents.LayoutClickListener)
    }
    addChildClickListener(listener)
}

/**
 * Adds a click listener to a layout. The click listener will be called when the layout and any descendant component is clicked,
 * except for descendants which have their own click listeners attached.
 *
 * Only left mouse button clicks are reported; double-clicks are ignored.
 * @param listener the click listener.
 */
fun (@VaadinDsl LayoutEvents.LayoutClickNotifier).addChildClickListener(listener: (LayoutEvents.LayoutClickEvent) -> Unit) {
    (this as Component).addStyleName("clickable")
    addLayoutClickListener { event ->
        if (event.button != MouseEventDetails.MouseButton.LEFT) {
            // only handle left mouse clicks
        } else if (event.isDoubleClick) {
            // ignore double-clicks
        } else if (event.clickedComponent != null && event.clickedComponent.hasClickListeners()) {
            // the component has its own click listeners, do nothing
        } else
        // what if some child layout listens for the layout click event as well?
            if (event.clickedComponent != null && event.clickedComponent.getAscendantLayoutWithLayoutClickNotifier() !== this@addChildClickListener) {
                // do nothing
            } else {
                listener(event)
            }
    }
}

/**
 * Replaces any click listeners with this one.
 * @param listener the listener to set. Only called on left-click.
 */
fun (@VaadinDsl Button).onLeftClick(listener: (Button.ClickEvent)->Unit) {
    getListeners(Button.ClickEvent::class.java).toList().forEach {
        @Suppress("DEPRECATION")
        removeClickListener(it as Button.ClickListener)
    }
    // Button only fires left-click events.
    addClickListener(listener)
}

/**
 * Replaces any click listeners with this one.
 * @param listener the listener to set. Only called on left-click.
 */
fun (@VaadinDsl Image).onLeftClick(listener: (MouseEvents.ClickEvent)->Unit): Unit {
    // warning, here we may receive listeners for ContextClickEvents!
    getListeners(MouseEvents.ClickEvent::class.java).filterIsInstance<MouseEvents.ClickListener>().forEach {
        @Suppress("DEPRECATION")
        removeClickListener(it)
    }
    addClickListener {
        if (it.button == MouseEventDetails.MouseButton.LEFT) listener(it)
    }
}
