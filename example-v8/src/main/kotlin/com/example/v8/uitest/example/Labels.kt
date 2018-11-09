package com.example.v8.uitest.example

import com.github.mvysny.karibudsl.v8.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class Labels : VerticalLayout(), View {
    init {
        isSpacing = false; w = 50.perc
        title("Labels")

        verticalLayout {
            isSpacing = false; margin = MarginInfo(false, true, false, false)
            label("Huge type for display text.") {
                w = fillParent; styleName = ValoTheme.LABEL_HUGE
            }
            label("Large type for introductory text. Etiam at risus et justo dignissim congue. Donec congue lacinia dui, a porttitor lectus condimentum laoreet. Nunc eu.") {
                w = fillParent; styleName = ValoTheme.LABEL_LARGE
            }
            label("Subtitle") {
                styleName = ValoTheme.LABEL_H2
            }
            label {
                html("Normal type for plain text, with a <a href=\"https://vaadin.com\">regular link</a>. Etiam at risus et justo dignissim congue. Donec congue lacinia dui, a porttitor lectus condimentum laoreet. Nunc eu.")
                w = fillParent
            }
            label("Small Title") {
                styleName = ValoTheme.LABEL_H3
            }
            label("Small type for additional text. Etiam at risus et justo dignissim congue. Donec congue lacinia dui, a porttitor lectus condimentum laoreet. Nunc eu.") {
                w = fillParent; styleName = ValoTheme.LABEL_SMALL
            }
            label("Tiny type for minor text.") {
                w = fillParent; styleName = ValoTheme.LABEL_TINY
            }
            label("Section Title") {
                styleName = ValoTheme.LABEL_H4
            }
            label("Normal type for plain text. Etiam at risus et justo dignissim congue. Donec congue lacinia dui, a porttitor lectus condimentum laoreet. Nunc eu.") {
                w = fillParent
            }
        }
        panel("Additional Label Styles") {
            verticalLayout {
                label("Bold type for prominent text. Etiam at risus et justo dignissim congue. Donec congue lacinia dui, a porttitor lectus condimentum laoreet. Nunc eu.") {
                    w = fillParent; styleName = ValoTheme.LABEL_BOLD
                }
                label("Light type for subtle text. Etiam at risus et justo dignissim congue. Donec congue lacinia dui, a porttitor lectus condimentum laoreet. Nunc eu.") {
                    w = fillParent; styleName = ValoTheme.LABEL_LIGHT
                }
                label("Colored type for highlighted text. Etiam at risus et justo dignissim congue. Donec congue lacinia dui, a porttitor lectus condimentum laoreet. Nunc eu.") {
                    w = fillParent; styleName = ValoTheme.LABEL_COLORED
                }
                label("A label for success") {
                    w = fillParent; styleName = ValoTheme.LABEL_SUCCESS
                }
                label("A label for failure") {
                    w = fillParent; styleName = ValoTheme.LABEL_FAILURE
                }
            }
        }
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }
}
