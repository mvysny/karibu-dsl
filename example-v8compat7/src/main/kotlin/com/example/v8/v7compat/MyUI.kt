package com.example.v8.v7compat

import javax.servlet.annotation.WebServlet

import com.vaadin.annotations.Theme
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.annotations.Widgetset
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.*
import com.vaadin.v7.data.Item
import com.vaadin.v7.ui.Table

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 *
 * The UI is initialized using [init]. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("valo")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
class MyUI : UI() {

    override fun init(vaadinRequest: VaadinRequest) {
        val layout = VerticalLayout()

        val table = Table("The Brightest Stars")

        // Define two columns for the built-in container
        table.addContainerProperty("Name", String::class.java, null)
        table.addContainerProperty("Mag", java.lang.Float::class.java, null)

        // Add a row the hard way
        val newItemId = table.addItem()
        val row1 = table.getItem(newItemId)
        row1.getItemProperty("Name").value = "Sirius"
        row1.getItemProperty("Mag").value = -1.46f

        // Add a few other rows using shorthand addItem()
        table.addItem(arrayOf<Any>("Canopus", -0.72f), 2)
        table.addItem(arrayOf<Any>("Arcturus", -0.04f), 3)
        table.addItem(arrayOf<Any>("Alpha Centauri", -0.01f), 4)

        // Show exactly the currently contained rows (items)
        table.pageLength = table.size()


        table.isSelectable = true

        layout.addComponents(table)
        layout.setMargin(true)
        layout.isSpacing = true

        content = layout
    }
}

@WebServlet(urlPatterns = ["/*"], name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI::class, productionMode = false)
class MyUIServlet : VaadinServlet()
