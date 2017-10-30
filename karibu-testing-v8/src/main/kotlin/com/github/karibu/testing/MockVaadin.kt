package com.github.karibu.testing

import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.AutoViewProvider
import com.vaadin.server.*
import com.vaadin.ui.UI
import org.atmosphere.util.annotation.AnnotationDetector
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.URI
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

object MockVaadin {
    // prevent GC on Vaadin Session and Vaadin UI as they are only soft-referenced from the Vaadin itself.
    private val strongRefSession = ThreadLocal<VaadinSession>()
    private val strongRefUI = ThreadLocal<UI>()
    /**
     * Creates new mock session and UI for a test. Just call this in your @Before
     */
    fun setup() {
        val config = DefaultDeploymentConfiguration(MockVaadin::class.java, Properties())
        val service = object : VaadinServletService(VaadinServlet(), config) {
            override fun isAtmosphereAvailable() = false
        }
        service.init()
        val session = object : VaadinSession(service) {
            private val lock = ReentrantLock()
            init {
                lock.lock()
            }
            override fun getLockInstance(): Lock = lock
        }
        VaadinSession.setCurrent(session)
        strongRefSession.set(session)
        val ui = object : UI() {
            override fun init(request: VaadinRequest?) {
            }
        }
        strongRefUI.set(ui)
        UI.setCurrent(ui)
        ui.session = session
        Page::class.java.getDeclaredField("location").apply {
            isAccessible = true
            set(ui.page, URI("http://localhost:8080"))
        }
    }
}

fun Any.serializeToBytes(): ByteArray = ByteArrayOutputStream().use { it -> ObjectOutputStream(it).writeObject(this); it }.toByteArray()
inline fun <reified T: Any> ByteArray.deserialize(): T = ObjectInputStream(inputStream()).readObject() as T
inline fun <reified T: Any> T.serializeDeserialize() = serializeToBytes().deserialize<T>()

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
