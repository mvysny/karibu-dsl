package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
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
        }
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}