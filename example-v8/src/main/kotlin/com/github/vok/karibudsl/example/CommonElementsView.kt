package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.Page
import com.vaadin.shared.Position
import com.vaadin.ui.MenuBar
import com.vaadin.ui.Notification
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class CommonElementsView : VerticalLayout(), View {
    init {
        isSpacing = false
        label("Common UI Elements") {
            addStyleNames(ValoTheme.LABEL_H2, ValoTheme.LABEL_COLORED)
        }
        gridLayout(2, 3) {
            w = fillParent; isSpacing = true

            panel("Loading Indicator") {
                verticalLayout {
                    label("You can test the loading indicator by pressing the buttons.")
                    cssLayout {
                        caption = "Show the loading indicator for…"
                        styleName = ValoTheme.LAYOUT_COMPONENT_GROUP
                        button("0.8", { Thread.sleep(800) })
                        button("3", { Thread.sleep(3000) })
                        button("15", { Thread.sleep(15000) }) {
                            styleName = "last"
                        }
                        label {
                            html("&nbsp;&nbsp; seconds")
                            w = wrapContent
                        }
                    }
                    label("The theme also provides a mixin that you can use to include a spinner anywhere in your application. Below is a Label with a custom style name, for which the spinner mixin is added.") {
                        w = fillParent
                        styleName = ValoTheme.LABEL_SMALL
                        caption = "Spinner"
                    }
                    spinner()
                }
            }

            panel("Notifications") {
                verticalLayout {
                    val notification = Notification("")
                    var typeString: String = ""
                    var styleString: String = ""
                    fun updateTypeStyle() {
                        notification.styleName = "$typeString $styleString".trim()
                    }

                    textField("Title") {
                        w = fillParent; placeholder = "Title for the notification"
                        addValueChangeListener { event ->
                            notification.caption = if (event.value.isNullOrBlank()) null else event.value
                        }
                        value = "Notification Title"
                    }
                    textArea("Description") {
                        w = fillParent; styleName = ValoTheme.TEXTAREA_SMALL
                        addValueChangeListener { event ->
                            notification.description = if (event.value.isNullOrBlank()) null else event.value
                        }
                        value = "A more informative message about what has happened. Nihil hic munitissimus habendi senatus locus, nihil horum? Inmensae subtilitatis, obscuris et malesuada fames. Hi omnes lingua, institutis, legibus inter se differunt."
                    }
                    menuBar {
                        caption = "Type"; styleName = ValoTheme.MENUBAR_SMALL
                        val typeCommand = MenuBar.Command { selectedItem ->
                            typeString = if (selectedItem.text == "Humanized") "" else selectedItem.text.toLowerCase()
                            updateTypeStyle()
                            items.forEach { item -> item.isChecked = false }
                            selectedItem.isChecked = true
                        }
                        addItem("Humanized", typeCommand).apply {
                            isCheckable = true
                            isChecked = true
                        }
                        addItem("Tray", typeCommand).isCheckable = true
                        addItem("Warning", typeCommand).isCheckable = true
                        addItem("Error", typeCommand).isCheckable = true
                        addItem("System", typeCommand).isCheckable = true
                    }
                    menuBar {
                        caption = "Additional style"; styleName = ValoTheme.MENUBAR_SMALL
                        val styleCommand = MenuBar.Command {
                            styleString = items.filter { it.isChecked }.joinToString(" ", transform = { it.text.toLowerCase() })
                            updateTypeStyle()
                        }
                        addItem("Dark", styleCommand).isCheckable = true
                        addItem("Success", styleCommand).isCheckable = true
                        addItem("Failure", styleCommand).isCheckable = true
                        addItem("Bar", styleCommand).isCheckable = true
                        addItem("Small", styleCommand).isCheckable = true
                        addItem("Closable", styleCommand).isCheckable = true
                    }
                    cssLayout {
                        caption = "Fade delay"
                        styleName = ValoTheme.LAYOUT_COMPONENT_GROUP
                        val delay = textField {
                            placeholder = "Infinite"; w = 7.em
                            addStyleNames(ValoTheme.TEXTFIELD_ALIGN_RIGHT, ValoTheme.TEXTFIELD_SMALL)
                            addValueChangeListener { event ->
                                notification.delayMsec = event.value.toIntOrNull() ?: -1
                                if (notification.delayMsec < 0) value = ""
                            }
                            value = "1000"
                        }
                        button { // clear
                            icon = VaadinIcons.CLOSE_CIRCLE
                            addStyleNames("last", ValoTheme.BUTTON_SMALL, ValoTheme.BUTTON_ICON_ONLY)
                            onLeftClick { delay.value = "" }
                        }
                        label { html("&nbsp; msec") }
                    }
                    gridLayout(3, 3) {
                        caption = "Show in position"; isSpacing = true
                        fun buttonWhichShows(position: Position) = button {
                            styleName = ValoTheme.BUTTON_SMALL
                            onLeftClick {
                                notification.position = position
                                notification.show(Page.getCurrent())
                            }
                        }
                        Position.values().sliceArray(0..8).forEach { buttonWhichShows(it) }
                    }
                }
            }
        }
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}