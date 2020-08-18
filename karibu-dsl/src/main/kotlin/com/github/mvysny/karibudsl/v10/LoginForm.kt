package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n

/**
 * A simple login form which shows a simple login form;
 * calls a handler provided in the [LoginForm.addLoginListener] when user clicks the "Login" button.
 *
 * This function creates an inline form, intended to be placed in a separate LoginView.
 * This use-case is excellent for cases when the whole app is user-protected
 * (an user must log in to view any view of the app, there are no views that an anonymous user may view).
 * If only parts of the app are protected, you can show a login dialog instead whenever the user
 * tries to access protected parts. In order to do that, please use the
 * [com.vaadin.flow.component.login.LoginOverlay] class.
 *
 * There are two ways to use this form. If the whole app is user-protected
 * (an user must log in to view any view of the app, there are no views that an anonymous user may view), then
 * it is simply possible to show the form as a full-screen in the UI if no user is logged in:
 *
 * ```
 * @BodySize(width = "100vw", height = "100vh")
 * class MainLayout : KComposite(), RouterLayout, BeforeEnterObserver {
 *   // ...
 *   override fun beforeEnter(event: BeforeEnterEvent) {
 *     if (!getCurrentUser().isLoggedIn) {
 *       event.rerouteTo(LoginView::class.java)
 *     }
 *   }
 *   // ...
 * }
 *
 * @BodySize(width = "100vw", height = "100vh")
 * @Route("login")
 * class LoginView : KComposite(), BeforeEnterObserver {
 *
 *   override fun beforeEnter(event: BeforeEnterEvent) {
 *     if (Session.loginManager.isLoggedIn) {
 *       event.rerouteTo("")
 *     }
 *   }
 *
 *   private lateinit var loginForm: LoginForm
 *   private val root = ui {
 *     verticalLayout {
 *       setSizeFull(); isPadding = false; content { center() }
 *
 *       val loginI18n: LoginI18n = loginI18n {
 *         header.title = "VoK Security Demo"
 *         additionalInformation = "Log in as user/user or admin/admin"
 *       }
 *       loginForm = loginForm(loginI18n) {
 *         addLoginListener { e ->
 *           if (!login(e.username, e.password)) {
 *             isError = true
 *           }
 *         }
 *       }
 *     }
 *   }
 * }
 * ```
 */
public fun (@VaadinDsl HasComponents).loginForm(
        loginI18n: LoginI18n = LoginI18n.createDefault(),
        block: (@VaadinDsl LoginForm).() -> Unit = {}
): LoginForm = init(LoginForm(loginI18n), block)

/**
 * Creates the default `LoginI18n` and allows you to override stuff like app title and so on:
 * ```
 * val loginI18n: LoginI18n = loginI18n {
 *   header.title = "VoK Security Demo"
 *   additionalInformation = "Log in as user/user or admin/admin"
 * }
 * ```
 */
public fun loginI18n(block: LoginI18n.()->Unit): LoginI18n = LoginI18n.createDefault().apply {
    header = LoginI18n.Header()
    block()
}

/**
 * Sets the error message with given [title] and [message]. The error message
 * is shown when [LoginForm.isError] is set to true.
 */
public fun LoginI18n.setErrorMessage(title: String, message: String) {
    errorMessage = LoginI18n.ErrorMessage().apply {
        this.title = title
        this.message = message
    }
}
