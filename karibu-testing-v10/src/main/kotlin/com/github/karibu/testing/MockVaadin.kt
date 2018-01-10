package com.github.karibu.testing

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.*
import com.vaadin.flow.server.startup.RouteRegistry
import java.io.BufferedReader
import java.io.InputStream
import java.security.Principal
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.servlet.http.Cookie

object MockVaadin {
    // prevent GC on Vaadin Session and Vaadin UI as they are only soft-referenced from the Vaadin itself.
    private val strongRefSession = ThreadLocal<VaadinSession>()
    private val strongRefUI = ThreadLocal<UI>()

    /**
     * Mocks Vaadin for the current test method.
     * @param routes all classes annotated with [com.vaadin.flow.router.Route]; use [autoDiscoverViews] to auto-discover all such classes.
     */
    fun setup(routes: Set<Class<out Component>> = setOf(), uiFactory: ()->UI = { UI() }) {
        val service = object : VaadinServletService(null, DefaultDeploymentConfiguration(MockVaadin::class.java, Properties(), { _, _ -> })) {
            private val registry = object : RouteRegistry() {
                init {
                    setNavigationTargets(routes)
                }
            }
            override fun isAtmosphereAvailable(): Boolean {
                // returning true here would access our null servlet, and we don't want that :)
                return false
            }
            override fun getRouteRegistry(): RouteRegistry = registry
        }
        service.init()
        VaadinService.setCurrent(service)
        val session = object : VaadinSession(service) {
            private val lock = ReentrantLock().apply { lock() }
            override fun getLockInstance(): Lock = lock
        }
        VaadinSession.setCurrent(session)
        strongRefSession.set(session)
        val ui = uiFactory()
        ui.internals.session = session
        ui.doInit(MockRequest(), -1)
        UI.setCurrent(ui)
        strongRefUI.set(ui)
    }
}

class MockRequest : VaadinRequest {
    override fun isUserInRole(role: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPathInfo(): String? = null

    override fun getRemoteUser(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWrappedSession(): WrappedSession {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWrappedSession(allowSessionCreation: Boolean): WrappedSession {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCookies(): Array<Cookie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocale(): Locale {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMethod(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParameterMap(): MutableMap<String, Array<String>> = mutableMapOf()

    override fun getAttributeNames(): Enumeration<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRemoteAddr(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHeaders(name: String?): Enumeration<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserPrincipal(): Principal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getReader(): BufferedReader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocales(): Enumeration<Locale> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInputStream(): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthType(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCharacterEncoding(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeAttribute(name: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContentLength(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHeader(headerName: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContextPath(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContentType(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getService(): VaadinService {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHeaderNames(): Enumeration<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttribute(name: String?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAttribute(name: String?, value: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParameter(parameter: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRemotePort(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDateHeader(name: String?): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRemoteHost(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isSecure(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
