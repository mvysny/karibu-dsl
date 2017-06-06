package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.github.vok.karibudsl.example.form.FormView
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.annotations.Viewport
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.ViewDisplay
import com.vaadin.server.*
import com.vaadin.shared.Position
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler
import javax.servlet.annotation.WebServlet

/**
 * The Vaadin UI which demoes all the features of the Karibu-DSL framework.
 * If not familiar with Vaadin, please check out the Vaadin tutorial first, or the tutorial at
 * [http://www.vaadinonkotlin.eu/].
 * @author mvy
 */
@Theme("valo")
@Title("Karibu-DSL Demo")
@Viewport("width=device-width, initial-scale=1.0")
class MyUI : UI() {

    override fun init(request: VaadinRequest?) {
        val content = valoMenu {
            appTitle = "<h3>Karibu-DSL <strong>Sample App</strong></h3>"
            userMenu {
                item("John Doe", ClassResource("profilepic300px.jpg")) {
                    item("Edit Profile")
                    item("Preferences")
                    addSeparator()
                    item("Sign Out")
                }
            }
            menuButton("Welcome", VaadinIcons.MENU, "3", WelcomeView::class.java)
            menuButton("Common UI Elements", VaadinIcons.NOTEBOOK, view = CommonElementsView::class.java)
            section("Components", "2")
            menuButton("Combo Boxes", VaadinIcons.DROP, view = ComboBoxes::class.java)
            menuButton("Menu Bars", VaadinIcons.MENU, view = MenuBars::class.java)
            section("Forms", "1")
            menuButton("Form Demo", VaadinIcons.FORM, view = FormView::class.java)
        }

        // Read more about navigators here: https://github.com/mvysny/karibu-dsl
        navigator = Navigator(this, content as ViewDisplay)
        navigator.addProvider(autoViewProvider)
        setErrorHandler { e ->
            log.error("Vaadin UI uncaught exception $e", e)
            // when the exception occurs, show a nice notification
            Notification("Oops", "An error occurred, and we are really sorry about that. Already working on the fix!", Notification.Type.ERROR_MESSAGE).apply {
                styleName = ValoTheme.NOTIFICATION_CLOSABLE
                position = Position.TOP_CENTER
                show(Page.getCurrent())
            }
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(MyUI::class.java)
    }
}

@WebServlet(urlPatterns = arrayOf("/*"), name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI::class, productionMode = false)
class MyUIServlet : VaadinServlet() {
    companion object {
        init {
            // Vaadin logs into java.util.logging. Redirect that, so that all logging goes through slf4j.
            SLF4JBridgeHandler.removeHandlersForRootLogger()
            SLF4JBridgeHandler.install()
        }
    }
}
