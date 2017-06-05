package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.github.vok.karibudsl.example.form.FormView
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
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
class MyUI : UI() {

    private val content = ValoMenuLayout()

    override fun init(request: VaadinRequest?) {
        setContent(content)
        Responsive.makeResponsive(this)

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

/**
 * The main screen with the menu and a view placeholder, where the view contents will go.
 */
private class ValoMenuLayout: HorizontalLayout(), ViewDisplay {
    /**
     * Tracks the registered menu items associated with view; when a view is shown, highlight appropriate menu item button.
     */
    private val views = mutableMapOf<Class<out View>, Button>()

    private val menuArea: CssLayout
    private lateinit var menu: CssLayout
    private val viewPlaceholder: CssLayout
    init {
        setSizeFull(); isSpacing = false

        menuArea = cssLayout {
            primaryStyleName = ValoTheme.MENU_ROOT
            menu = cssLayout { // menu
                horizontalLayout {
                    w = fillParent; isSpacing = false; defaultComponentAlignment = Alignment.MIDDLE_LEFT
                    styleName = ValoTheme.MENU_TITLE
                    label {
                        html("<h3>Karibu-DSL <strong>Sample App</strong></h3>")
                        w = wrapContent
                        expandRatio = 1f
                    }

                }
                button("Menu") { // only visible when the top bar is shown
                    onLeftClick {
                        menu.toggleStyleName("valo-menu-visible", !menu.hasStyleName("valo-menu-visible"))
                    }
                    addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL, "valo-menu-toggle")
                    icon = VaadinIcons.MENU
                }
                menuBar { // the user menu, settings
                    styleName = "user-menu"
                    addItem("John Doe", ClassResource("profilepic300px.jpg"), null).apply {
                        item("Edit Profile")
                        item("Preferences")
                        addSeparator()
                        item("Sign Out")
                    }
                }
                // the navigation buttons
                cssLayout {
                    primaryStyleName = "valo-menuitems"
                    menuButton(VaadinIcons.MENU, "Welcome", "3", WelcomeView::class.java)
                    menuButton(VaadinIcons.NOTEBOOK, "Common UI Elements", view = CommonElementsView::class.java)
                    section("Components", "1")
                    menuButton(VaadinIcons.MENU, "Menu Bars", view = MenuBars::class.java)
                    section("Forms", "1")
                    menuButton(VaadinIcons.FORM, "Form Demo", view = FormView::class.java)
                }
            }
        }

        viewPlaceholder = cssLayout {
            primaryStyleName = "valo-content"
            addStyleName("v-scrollable")
            setSizeFull()
            expandRatio = 1f
        }
    }

    private fun CssLayout.section(caption: String, badge: String? = null, block: Label.()->Unit = {}) {
        label {
            if (badge == null) {
                this.caption = caption
            } else {
                html("""$caption <span class="valo-menu-badge">$badge</span>""")
            }
            primaryStyleName = ValoTheme.MENU_SUBTITLE
            w = wrapContent
            addStyleName(ValoTheme.LABEL_H4)
            block()
        }
    }

    /**
     * Registers a button to a menu with given [icon] and [caption], which launches given [view].
     * @param badge optional badge which is displayed in the button's top-right corner. Usually this is a number, showing number of notifications or such.
     * @param view optional view; if not null, clicking this menu button will launch this view with no parameters; also the button will be marked selected
     * when the view is shown.
     */
    private fun CssLayout.menuButton(icon: Resource, caption: String, badge: String? = null, view: Class<out View>? = null, block: Button.()->Unit = {}) {
        val b = button {
            primaryStyleName = ValoTheme.MENU_ITEM
            this.icon = icon
            if (badge != null) {
                isCaptionAsHtml = true
                this.caption = """$caption <span class="valo-menu-badge">$badge</span>"""
            } else {
                this.caption = caption
            }
            if (view != null) {
                onLeftClick { navigateToView(view) }
                views[view] = this
            }
        }
        b.block()
    }

    override fun showView(view: View) {
        // show the view itself
        viewPlaceholder.removeAllComponents()
        viewPlaceholder.addComponent(view as Component)

        // make the appropriate menu button selected, to show the current view
        views.values.forEach { it.removeStyleName("selected") }
        views[view.javaClass as Class<*>]?.addStyleName("selected")
    }
}
