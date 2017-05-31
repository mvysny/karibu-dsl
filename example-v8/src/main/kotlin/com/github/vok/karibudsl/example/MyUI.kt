package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.github.vok.karibudsl.example.form.FormView
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewDisplay
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.*
import org.slf4j.bridge.SLF4JBridgeHandler
import javax.servlet.annotation.WebServlet

/**
 * The Vaadin UI which demoes all the features. If not familiar with Vaadin, please check out the Vaadin tutorial first.
 * @author mvy
 */
@Theme("valo")
@Title("Karibu-DSL Demo")
class MyUI : UI() {

    private val content = Content()

    override fun init(request: VaadinRequest?) {
        setContent(content)
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

private class Content: VerticalLayout(), ViewDisplay {
    private val viewPlaceholder: CssLayout
    private lateinit var currentTimeLabel: Label
    init {
        setSizeFull()
        // the Vaadin DSL demo - build your UI, builder-style!
        horizontalLayout {
            w = fillParent
            menuBar {
                expandRatio = 1f
                addItem("Welcome") { WelcomeView.navigateTo() }
                addItem("Form Demo") { FormView.navigateTo() }
            }
            currentTimeLabel = label {
                setWidthUndefined()
            }
        }
        viewPlaceholder = cssLayout {
            setSizeFull(); expandRatio = 1f
        }
    }

    override fun showView(view: View?) {
        viewPlaceholder.removeAllComponents()
        viewPlaceholder.addComponent(view as Component)
    }
}
