package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.github.vok.karibudsl.example.form.FormView
import com.vaadin.annotations.*
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.ViewDisplay
import com.vaadin.server.ClassResource
import com.vaadin.server.Page
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.shared.Position
import com.vaadin.ui.HasComponents
import com.vaadin.ui.ItemCaptionGenerator
import com.vaadin.ui.Notification
import com.vaadin.ui.UI
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
@Theme("tests-valo")
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
            section("Components", "5")
            menuButton("Labels", VaadinIcons.TEXT_LABEL, view = Labels::class.java)
            menuButton("Buttons & Links", VaadinIcons.BUTTON, view = ButtonsAndLinks::class.java)
            menuButton("Text Fields", VaadinIcons.TEXT_INPUT, view = TextFields::class.java)
            menuButton("Date Fields", VaadinIcons.DATE_INPUT, view = DateFields::class.java)
            menuButton("Combo Boxes", VaadinIcons.DROP, view = ComboBoxes::class.java)
            menuButton("Selects", VaadinIcons.SELECT, view = NativeSelects::class.java)
            menuButton("Check Boxes & Option Groups", VaadinIcons.CHECK, view = CheckBoxes::class.java)
            menuButton("Sliders", VaadinIcons.SLIDER, view = Sliders::class.java)
            menuButton("Color Pickers", VaadinIcons.PAINTBRUSH, view = ColorPickers::class.java)
            menuButton("Menu Bars", VaadinIcons.MENU, view = MenuBars::class.java)
            menuButton("Trees", VaadinIcons.FILE_TREE, view = Trees::class.java)
            section("Containers", "4")
            menuButton("Split Panels", VaadinIcons.PANEL, view = SplitPanels::class.java)
            menuButton("Tabs", VaadinIcons.TAB, "123", Tabsheets::class.java)
            menuButton("Accordions", VaadinIcons.ACCORDION_MENU, view = Accordions::class.java)
            menuButton("Popup Views", VaadinIcons.MODAL, view = PopupViews::class.java)
            section("Other", "1")
            menuButton("Form Demo", VaadinIcons.FORM, view = FormView::class.java)
        }

        // Read more about navigators here: https://github.com/mvysny/karibu-dsl
        navigator = Navigator(this, content as ViewDisplay)
        navigator.addProvider(autoViewProvider)
        setErrorHandler { e ->
            log.error("Vaadin UI uncaught exception ${e.throwable}", e.throwable)
            // when the exception occurs, show a nice notification
            Notification("Oops", "An error occurred, and we are really sorry about that. Already working on the fix!", Notification.Type.ERROR_MESSAGE).apply {
                styleName += " " + ValoTheme.NOTIFICATION_CLOSABLE
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

/**
 * An endless sequence of short lower-case strings.
 */
object StringGenerator: Sequence<String>, ItemCaptionGenerator<Int> {
    private val strings = arrayOf("lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "quid", "securi", "etiam", "tamquam", "eu", "fugiat", "nulla", "pariatur")
    override fun iterator(): Iterator<String> = object : Iterator<String> {
        private var i = 0
        override fun hasNext() = true
        override fun next() = get(i++)
    }
    override fun apply(item: Int) = get(item)
    operator fun get(index: Int) = strings[index % strings.size]
}

/**
 * An example of a DSL definition which simply wraps a label and adds styles necessary to display a nice [title].
 */
inline fun HasComponents.title(title: String) = label(title) {
    w = fillParent
    addStyleNames(ValoTheme.LABEL_H1, ValoTheme.LABEL_COLORED)
}
