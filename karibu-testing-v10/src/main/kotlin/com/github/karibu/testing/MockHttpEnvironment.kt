package com.github.karibu.testing

import org.atmosphere.util.FakeHttpSession
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.security.Principal
import java.util.*
import javax.servlet.*
import javax.servlet.descriptor.JspConfigDescriptor
import javax.servlet.http.*

class MockContext : ServletContext {
    override fun getServlet(name: String?): Servlet? = null

    override fun <T : Servlet?> createServlet(clazz: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEffectiveMajorVersion(): Int = 3

    override fun getResource(path: String): URL = File(getRealPath(path)).toURI().toURL()

    override fun addListener(className: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : EventListener?> addListener(t: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addListener(listenerClass: Class<out EventListener>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClassLoader(): ClassLoader = Thread.currentThread().contextClassLoader

    override fun getAttributeNames(): Enumeration<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMajorVersion(): Int = 3

    override fun log(msg: String) {
        log.error(msg)
    }

    override fun log(exception: Exception, msg: String) {
        log.error(msg, exception)
    }

    override fun log(message: String, throwable: Throwable) {
        log.error(message, throwable)
    }

    override fun getFilterRegistration(filterName: String?): FilterRegistration {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSessionTrackingModes(sessionTrackingModes: MutableSet<SessionTrackingMode>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setInitParameter(name: String?, value: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResourceAsStream(path: String): InputStream = getResource(path).openStream()

    override fun getNamedDispatcher(name: String?): RequestDispatcher {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFilterRegistrations(): MutableMap<String, out FilterRegistration> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServletNames(): Enumeration<String> = Collections.emptyEnumeration()

    override fun getDefaultSessionTrackingModes(): MutableSet<SessionTrackingMode> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMimeType(file: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun declareRoles(vararg roleNames: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Filter?> createFilter(clazz: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRealPath(path: String): String = File("src/main/webapp/frontend/$path").absolutePath

    override fun getInitParameter(name: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMinorVersion(): Int = 0

    override fun getJspConfigDescriptor(): JspConfigDescriptor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeAttribute(name: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServletContextName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addFilter(filterName: String?, className: String?): FilterRegistration.Dynamic {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addFilter(filterName: String?, filter: Filter?): FilterRegistration.Dynamic {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addFilter(filterName: String?, filterClass: Class<out Filter>?): FilterRegistration.Dynamic {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContextPath(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSessionCookieConfig(): SessionCookieConfig {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getVirtualServerName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContext(uripath: String?): ServletContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRequestDispatcher(path: String?): RequestDispatcher {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttribute(name: String?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAttribute(name: String?, `object`: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServletRegistration(servletName: String?): ServletRegistration {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : EventListener?> createListener(clazz: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addServlet(servletName: String?, className: String?): ServletRegistration.Dynamic {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addServlet(servletName: String?, servlet: Servlet?): ServletRegistration.Dynamic {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addServlet(servletName: String?, servletClass: Class<out Servlet>?): ServletRegistration.Dynamic {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServlets(): Enumeration<Servlet> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEffectiveMinorVersion(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServletRegistrations(): MutableMap<String, out ServletRegistration> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResourcePaths(path: String?): MutableSet<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInitParameterNames(): Enumeration<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServerInfo(): String = "Mock"

    override fun getEffectiveSessionTrackingModes(): MutableSet<SessionTrackingMode> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(MockContext::class.java)
    }
}

class MockRequest(ctx: ServletContext) : HttpServletRequest {

    private val session = FakeHttpSession("1", ctx, System.currentTimeMillis(), 30)

    override fun getInputStream(): ServletInputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startAsync(): AsyncContext {
        throw UnsupportedOperationException("Unsupported")
    }

    override fun startAsync(servletRequest: ServletRequest?, servletResponse: ServletResponse?): AsyncContext {
        throw UnsupportedOperationException("Unsupported")
    }

    override fun getProtocol(): String = "HTTP/1.1"

    override fun getRequestURL(): StringBuffer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCharacterEncoding(env: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParameterValues(name: String?): Array<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isAsyncStarted(): Boolean = false

    override fun getContentLengthLong(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRealPath(path: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(username: String?, password: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRequestedSessionIdValid(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServerPort(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRequestedSessionId(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServletPath(): String = ""

    override fun getSession(create: Boolean): HttpSession = session

    override fun getSession(): HttpSession = session

    override fun getServerName(): String = "127.0.0.1"

    override fun getLocalAddr(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : HttpUpgradeHandler?> upgrade(handlerClass: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRequestedSessionIdFromCookie(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPart(name: String?): Part {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRequestedSessionIdFromURL(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocalPort(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRequestedSessionIdFromUrl(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getServletContext(): ServletContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQueryString(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDispatcherType(): DispatcherType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParts(): MutableCollection<Part> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScheme(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocalName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isAsyncSupported(): Boolean = false

    override fun getParameterNames(): Enumeration<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun authenticate(response: HttpServletResponse?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPathTranslated(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIntHeader(name: String?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeSessionId(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAsyncContext(): AsyncContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRequestURI(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRequestDispatcher(path: String?): RequestDispatcher {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isUserInRole(role: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPathInfo(): String? = null

    override fun getRemoteUser(): String {
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

    override fun getRemotePort(): Int = 8080

    override fun getDateHeader(name: String?): Long = -1

    override fun getRemoteHost(): String = "127.0.0.1"

    override fun isSecure(): Boolean = false
}
