package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@AutoView
class SplitPanels : VerticalLayout(), View {
    init {
        isSpacing = false

        label("Split Panels") {
            addStyleNames(ValoTheme.LABEL_H2, ValoTheme.LABEL_COLORED)
        }
        label("Outlines are just to show the areas of the SplitPanels. They are not part of the actual component style.") {
            w = 100.perc
        }
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING; margin = MarginInfo(true, false, false, false)
            panel {
                caption = "Default style"; w = 400.px; h = wrapContent
                horizontalSplitPanel {
                    setSizeFull()
                    demoContent()
                    demoContent()
                }
            }
            panel {
                caption = "Default style"; w = 300.px; h = 200.px
                verticalSplitPanel {
                    setSizeFull()
                    demoContent()
                    demoContent()
                }
            }
            panel {
                caption = "Large style"; w = 300.px; h = 200.px
                horizontalSplitPanel {
                    setSizeFull(); styleName = ValoTheme.SPLITPANEL_LARGE
                    demoContent()
                    demoContent()
                }
            }
            panel {
                caption = "Large style"; w = 300.px; h = 200.px
                horizontalSplitPanel {
                    setSizeFull(); styleName = ValoTheme.SPLITPANEL_LARGE
                    demoContent()
                    demoContent()
                }
            }
        }
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }
}

private fun HasComponents.demoContent() = init(VerticalLayout()) {
    label("Fictum,  deserunt mollit anim laborum astutumque!") {
        w = fillParent
    }
}