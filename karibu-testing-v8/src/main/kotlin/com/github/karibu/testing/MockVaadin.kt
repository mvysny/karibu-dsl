package com.github.karibu.testing

import com.vaadin.server.*
import com.vaadin.ui.UI
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
    fun setup(uiFactory: ()->UI = { MockUI() }) {
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
        val ui = uiFactory()
        strongRefUI.set(ui)
        UI.setCurrent(ui)
        ui.session = session
        Page::class.java.getDeclaredField("location").apply {
            isAccessible = true
            set(ui.page, URI("http://localhost:8080"))
        }
        UI::class.java.getDeclaredMethod("init", VaadinRequest::class.java).apply {
            isAccessible = true
            invoke(ui, null)
        }
    }
}

internal class MockUI : UI() {
    override fun init(request: VaadinRequest?) {
    }
}
