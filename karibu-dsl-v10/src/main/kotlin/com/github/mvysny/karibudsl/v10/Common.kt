package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.dom.ClassList
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.reflect.Method

/**
 * When introducing extensions for your custom components, just call this in your method. For example:
 *
 * `fun HasComponents.shinyComponent(caption: String? = null, block: ShinyComponent.()->Unit = {}) = init(ShinyComponent(caption), block)`
 *
 * Adds [component] to receiver, see [HasComponents.add] for details.
 *
 * @param component the component to attach
 * @param block optional block to run over the component, allowing you to add children to the [component]
 */
@VaadinDsl
fun <T : Component> (@VaadinDsl HasComponents).init(component: T, block: T.() -> Unit = {}): T {
    add(component)
    component.block()
    return component
}

/**
 * Returns the getter method for given property name; fails if there is no such getter.
 */
fun Class<*>.getGetter(propertyName: String): Method {
    val descriptors: Array<out PropertyDescriptor> = Introspector.getBeanInfo(this).propertyDescriptors
    val descriptor: PropertyDescriptor? = descriptors.firstOrNull { it.name == propertyName }
    requireNotNull(descriptor) { "No such field '$propertyName' in $this; available properties: ${descriptors.joinToString { it.name }}" }
    val getter: Method = requireNotNull(descriptor.readMethod) { "The $this.$propertyName property does not have a getter: $descriptor" }
    return getter
}

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
