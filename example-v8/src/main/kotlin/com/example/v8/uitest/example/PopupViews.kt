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
                caption = "Hide on mouse-out"; isHideOnMouseOut = true
                minimizedValueAsHTML = "Click to view"
                verticalLayout {
                    isSpacing = false; w = 300.px
                    label("Fictum,  deserunt mollit anim laborum astutumque! Magna pars studiorum, prodita quaerimus.") {
                        w = fillParent
                    }
                }
            }
            popupView {
                isHideOnMouseOut = false; caption = "Hide on click-outside"
                // we can't use DSL directly since this example sleeps 500ms before providing the popup view contents.
                content = object : PopupView.Content {
                    private var count = 0
                    override fun getPopupComponent(): Component {
                        Thread.sleep(500)
                        return VerticalLayout().apply {
                            isSpacing = false
                            label {
                                html("<h3>Thanks for waiting!</h3><p>You've opened this popup <b>${++count} time${if (count > 1) "s" else " only"}</b>.</p>")
                            }
                        }
                    }

                    override fun getMinimizedValueAsHTML() = "Show slow loading content"
                }
            }
        }
    }
}
