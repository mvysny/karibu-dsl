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
import com.vaadin.ui.*
import org.slf4j.bridge.SLF4JBridgeHandler
import javax.servlet.annotation.WebServlet
import com.vaadin.ui.themes.ValoTheme
import com.vaadin.ui.CssLayout

/**
 * The Vaadin UI which demoes all the features. If not familiar with Vaadin, please check out the Vaadin tutorial first.
 * @author mvy
 */
@Theme("valo")
@Title("Karibu-DSL Demo")
class MyUI : UI() {

    private val content = ValoMenuLayout()

    override fun init(request: VaadinRequest?) {
        setContent(content)
        Responsive.makeResponsive(this)
        navigator = Navigator(this, content as ViewDisplay)
        navigator.addProvider(autoViewProvider)
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

private class ValoMenuLayout: HorizontalLayout(), ViewDisplay {
    private val menuArea: CssLayout
    private val viewPlaceholder: CssLayout
    init {
        setSizeFull()
        isSpacing = false

        menuArea = cssLayout {
            primaryStyleName = ValoTheme.MENU_ROOT
            cssLayout {
                addStyleNames(ValoTheme.MENU_PART, ValoTheme.MENU_PART_LARGE_ICONS)
                label("Va") {
                    w = wrapContent; primaryStyleName = ValoTheme.MENU_LOGO
                }
                menuButton(VaadinIcons.MENU, "Welcome", "3") {
                    onLeftClick { WelcomeView.navigateTo() }
                }
                menuButton(VaadinIcons.FORM, "Form Demo") {
                    onLeftClick { FormView.navigateTo() }
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

    private fun CssLayout.menuButton(icon: Resource, caption: String, badge: String? = null, block: Button.()->Unit) {
        val b = button {
            primaryStyleName = ValoTheme.MENU_ITEM
            this.icon = icon
            if (badge != null) {
                isCaptionAsHtml = true
                this.caption = """$caption <span class="valo-menu-badge">$badge</span>"""
            } else {
                this.caption = caption
            }
        }
        // @todo b.addStyleName("selected")
        b.block()
    }

    override fun showView(view: View?) {
        viewPlaceholder.removeAllComponents()
        viewPlaceholder.addComponent(view as Component)
    }
}
