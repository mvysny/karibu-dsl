package com.github.mvysny.karibudsl.v8

import com.vaadin.server.AbstractClientConnector
import com.vaadin.server.Extension
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty

/**
 * Utility class that allows you to easily create a property on a component that delegates to a property of an [Extension]. Only applicable to a specific case when:
 * * The [Extension] only has one property
 * * At most one extension instance makes sense on particular component
 *
 * For example, imagine a [TypeExtension] that sets the `type` attribute
 * of an `<input>` element produced by `TextField`. When the extension is present, it should alter the `type` attribute to whatever type necessary;
 * if the extension is absent, `type` should revert to `"text"`. You'd like to have a `type` extension property on `TextField`
 * which controls the extension. When `TextField.type` is set to null, we need to remove all extension instances; when `TextField.type` is set to any string
 * we need to either create and attach extension to the field, or modify an existing extension.
 *
 * You can use this class to handle the vagaries of managing extension instances on a component. You can simply write the following,
 * to employ this class by the mechanism of Kotlin delegated properties:
 * ```
 * var AbstractTextField.type: String? by ExtensionPropertyDelegate(TypeExtension::class.java, TypeExtension::type, { TypeExtension(it) }, "text")
 * ```
 * @property extensionClass the class of the extension governed by this class, e.g. [TypeExtension]. Related generic parameter: [E]
 * @property E see [extensionClass]
 * @property extensionProperty the property on extension used to read or write the value, e.g. [TypeExtension.type]. The value type is [T]
 * @property T the type of the value of the property. See [extensionProperty].
 * @property extensionConstructor constructs new extension and attaches it to the owner component of type [C].
 * @property C the component type to which the extension is attached.
 * @property emptyValue optional additional empty value that causes this class to remove the extension from the owner component. Defaults to `null`.
 */
class ExtensionPropertyDelegate<C : AbstractClientConnector, E : Extension, T>(val extensionClass: Class<E>,
                                                                               val extensionProperty: KMutableProperty1<E, T>,
                                                                               val extensionConstructor: (C) -> E,
                                                                               val emptyValue: T? = null) : ReadWriteProperty<C, T?> {
    override fun getValue(thisRef: C, property: KProperty<*>): T? {
        val extensionInstance: E = thisRef.extensions.filterIsInstance(extensionClass).firstOrNull() ?: return emptyValue
        return extensionProperty.get(extensionInstance)
    }

    override fun setValue(thisRef: C, property: KProperty<*>, value: T?) {
        if (value == emptyValue || value == null) {
            thisRef.extensions.filterIsInstance(extensionClass).forEach { it.remove() }
        } else {
            val extensionInstance: E = thisRef.extensions.filterIsInstance(extensionClass).firstOrNull()
                    ?: extensionConstructor(thisRef)
            extensionProperty.set(extensionInstance, value)
        }
    }
}
