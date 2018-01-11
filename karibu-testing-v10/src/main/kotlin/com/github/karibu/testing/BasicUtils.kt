package com.github.karibu.testing

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import org.atmosphere.util.annotation.AnnotationDetector
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun Any.serializeToBytes(): ByteArray = ByteArrayOutputStream().use { it -> ObjectOutputStream(it).writeObject(this); it }.toByteArray()
inline fun <reified T: Any> ByteArray.deserialize(): T = ObjectInputStream(inputStream()).readObject() as T
inline fun <reified T: Any> T.serializeDeserialize() = serializeToBytes().deserialize<T>()

/**
 * Auto-discovers views and register them to [autoViewProvider]. Can be called multiple times.
 * @param packageName set the package name for the detector to be faster; or provide null to scan the whole classpath, but this is quite slow.
 */
fun autoDiscoverViews(packageName: String? = null): Set<Class<out Component>> {
    val entities = mutableListOf<Class<*>>()
    val detector = AnnotationDetector(object : AnnotationDetector.TypeReporter {
        override fun reportTypeAnnotation(annotation: Class<out Annotation>?, className: String?) {
            entities.add(Class.forName(className))
        }

        override fun annotations(): Array<out Class<out Annotation>> = arrayOf(Route::class.java)
    })
    if (packageName == null) {
        detector.detect()
    } else {
        detector.detect(packageName)
    }

    println("Auto-discovered views: ${entities.joinToString { it.simpleName }}")
    @Suppress("UNCHECKED_CAST")
    return entities.map { it as Class<out Component> }.toSet()
}

/**
 * Allows us to fire any Vaadin event on any Vaadin component.
 * @receiver the component, not null.
 * @param event the event, not null.
 */
fun Component._fireEvent(event: ComponentEvent<*>) {
    // fireEvent() is protected, gotta make it public
    val fireEvent = Component::class.java.getDeclaredMethod("fireEvent", ComponentEvent::class.java)
    fireEvent.isAccessible = true
    fireEvent.invoke(this, event)
}

/**
 * Determines the component's `label` (it's the HTML element's `label` property actually). Intended to be used for fields such as [TextField].
 */
var Component.label: String
    get() = element.getProperty("label") ?: ""
    set(value) {
        element.setProperty("label", if (value.isBlank()) null else value)
    }

/**
 * The Component's caption: [text] for [Button], [label] for fields such as [TextField].
 */
val Component.caption: String get() = when(this) {
    is Button -> text
    else -> label
}
/**
 * Workaround for https://github.com/vaadin/flow/issues/664
 */
var Component.id_: String?
    get() = id.orElse(null)
    set(value) { setId(value) }

val Component.isAttached: Boolean
    get() = ui.orElse(null)?.session != null
