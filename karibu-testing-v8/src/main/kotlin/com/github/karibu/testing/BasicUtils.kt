package com.github.karibu.testing

import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.AutoViewProvider
import com.vaadin.server.AbstractClientConnector
import org.atmosphere.util.annotation.AnnotationDetector
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*

fun Serializable.serializeToBytes(): ByteArray = ByteArrayOutputStream().use { it -> ObjectOutputStream(it).writeObject(this); it }.toByteArray()
inline fun <reified T: Serializable> ByteArray.deserialize(): T = ObjectInputStream(inputStream()).readObject() as T
inline fun <reified T: Serializable> T.serializeDeserialize() = serializeToBytes().deserialize<T>()

/**
 * Auto-discovers views and register them to [autoViewProvider]. Can be called multiple times.
 * @param packageName set the package name for the detector to be faster; or provide null to scan the whole classpath, but this is quite slow.
 */
fun autoDiscoverViews(packageName: String? = null) {
    val entities = mutableListOf<Class<*>>()
    val detector = AnnotationDetector(object : AnnotationDetector.TypeReporter {
        override fun reportTypeAnnotation(annotation: Class<out Annotation>?, className: String?) {
            entities.add(Class.forName(className))
        }

        override fun annotations(): Array<out Class<out Annotation>> = arrayOf(AutoView::class.java)
    })
    if (packageName == null) {
        detector.detect()
    } else {
        detector.detect(packageName)
    }

    println("Auto-discovered views: ${entities.joinToString { it.simpleName }}")
    AutoViewProvider().onStartup(entities.toMutableSet(), null)
}

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
