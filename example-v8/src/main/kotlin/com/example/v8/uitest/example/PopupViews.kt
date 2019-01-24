package com.example.v8.uitest.example

import com.github.mvysny.karibudsl.v8.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@AutoView
class PopupViews : Composite(), View {
    private val root = verticalLayout {
        isSpacing = false
        title("Popup Views")

        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            popupView {
                caption = "Eager Contents"; isHideOnMouseOut = false; minimizedValueAsHTML = "Click to view"
                verticalLayout {
                    isSpacing = false; w = 300.px
                    label("Fictum,  deserunt mollit anim laborum astutumque! Magna pars studiorum, prodita quaerimus.") {
                        w = fillParent
                    }
                }
            }
            popupView {
                caption = "Hide on mouse-out"; isHideOnMouseOut = true; minimizedValueAsHTML = "Click to view"
                lazy {
                    verticalLayout {
                        isSpacing = false; w = 300.px
                        label("Fictum,  deserunt mollit anim laborum astutumque! Magna pars studiorum, prodita quaerimus.") {
                            w = fillParent
                        }
                    }
                }
            }
            popupView {
                isHideOnMouseOut = false; caption = "Hide on click-outside"; minimizedValueAsHTML = "Show slow loading content"
                // we can't use DSL directly since this example sleeps 500ms before providing the popup view contents.
                lazy {
                    Thread.sleep(500)
                    verticalLayout {
                        isSpacing = false
                        label {
                            html("<h3>Thanks for waiting!</h3><p>You've opened this popup</p>")
                        }
                    }
                }
            }
        }
    }
}
