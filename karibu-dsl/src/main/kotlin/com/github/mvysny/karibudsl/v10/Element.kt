package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.dom.Element
import com.vaadin.flow.dom.Node
import com.vaadin.flow.shared.util.SharedUtil
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import java.io.Serializable

/**
 * Reads/writes [Polymer Element property][com.vaadin.flow.dom.Element.getProperty] of this component. Allows you to simply
 * expose String Polymer properties on your Vaadin component as follows:
 * ```kotlin
 * @Tag("my-test-element")
 * class MyTest : Component() {
 *   var greeting: String by ElementStringProperty()
 * }
 * ```
 *
 * No *camelCase* to *dash-separated* conversion is performed.
 * @property defaultValue used in place of `null` value
 */
public class ElementStringProperty(public val defaultValue: String = "") : ReadWriteProperty<Component, String>, Serializable {
    override fun getValue(thisRef: Component, property: KProperty<*>): String = thisRef.element.getProperty(property.name, defaultValue)
    override fun setValue(thisRef: Component, property: KProperty<*>, value: String) {
        thisRef.element.setProperty(property.name, if (value == defaultValue) null else value)
    }
}

/**
 * Reads/writes [Polymer Element property][com.vaadin.flow.dom.Element.getProperty] of this component. Allows you to simply
 * expose boolean Polymer properties on your Vaadin component as follows:
 * ```kotlin
 * @Tag("my-test-element")
 * class MyTest : Component() {
 *   var isDarkThemed: Boolean by ElementBooleanProperty()
 * }
 * ```
 *
 * A value defined as some other type than boolean is converted according to
 * JavaScript semantics:
 * * String values are `true`, except for the empty string.
 * * Numerical values are <code>true</code>, except for 0 and `NaN`.
 * * JSON object and JSON array values are always `true`.
 *
 * No *camelCase* to *dash-separated* conversion is performed.
 * @property defaultValue used in place of `null` value
 */
public class ElementBooleanProperty(public val defaultValue: Boolean) : ReadWriteProperty<Component, Boolean>, Serializable {
    override fun getValue(thisRef: Component, property: KProperty<*>): Boolean = thisRef.element.getProperty(property.name, defaultValue)
    override fun setValue(thisRef: Component, property: KProperty<*>, value: Boolean) {
        thisRef.element.setProperty(property.name, value)
    }
}

/**
 * Reads/writes [DOM Element attribute][com.vaadin.flow.dom.Element.getAttribute] of this component. Allows you to simply
 * expose DOM attributes on your Vaadin component as follows:
 * ```kotlin
 * @Tag("my-test-element")
 * class MyTest : Component() {
 *   var userName: String by ElementAttributeProperty()
 * }
 * ```
 *
 * Setting the value to `null` will remove the attribute from the element.
 *
 * The *camelCase* property name is automatically converted to *dash-separated* style.
 *
 * Note: An empty attribute value (`""`) will be rendered as
 * `<div something>` and not `<div something="">`.
 *
 * Note that attribute changes made on the server are sent to the client but
 * attribute changes made on the client side are not reflected back to the
 * server.
 *
 * For more information see Vaadin [com.vaadin.flow.dom.Element.getAttribute].
 * @property defaultValue used in place of `null` value
 */
public class ElementAttributeProperty(public val defaultValue: String? = null) : ReadWriteProperty<Component, String?>, Serializable {
    override fun getValue(thisRef: Component, property: KProperty<*>): String? = thisRef.element.getAttribute(SharedUtil.camelCaseToDashSeparated(property.name)) ?: defaultValue
    override fun setValue(thisRef: Component, property: KProperty<*>, value: String?) {
        val newValue: String? = if (value == defaultValue) null else value
        val attributeName: String = SharedUtil.camelCaseToDashSeparated(property.name)
        if (newValue == null) {
            thisRef.element.removeAttribute(attributeName)
        } else {
            thisRef.element.setAttribute(attributeName, newValue)
        }
    }
}
