package com.example.v8.uitest.example

import com.github.mvysny.karibudsl.v8.*
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@AutoView
class Accordions : Composite(), View {
    private val root = verticalLayout {
        isSpacing = false
        title("Accordions")

        horizontalLayout {
            w = 100.perc
            demoAccordion("Normal")
            demoAccordion("Borderless") {
                addStyleName(ValoTheme.ACCORDION_BORDERLESS)
            }
        }
    }
}

private fun HasComponents.demoAccordion(caption: String, block: _Accordion.()->Unit = {}) = accordion {
    this.caption = caption
    verticalLayout {
        tab.caption = "First Caption"; tab.icon = VaadinIcons.ACADEMY_CAP
        isMargin = true
        label("Fabio vel iudice vincam, sunt in culpa qui officia. Ut enim ad minim veniam, quis nostrud exercitation.") {
            w = fillParent
        }
    }
    verticalLayout {
        tab.caption = "Second Caption"; tab.icon = VaadinIcons.AMBULANCE
        isMargin = true
        label("Gallia est omnis divisa in partes tres, quarum.") {
            w = fillParent
        }
    }
    verticalLayout {
        tab.caption = "Third Caption"; tab.icon = VaadinIcons.BROWSER
        isMargin = true
        label("Nihil hic munitissimus habendi senatus locus, nihil horum? Sed haec quis possit intrepidus aestimare tellus.") {
            w = fillParent
        }
    }
    verticalLayout {
        tab.caption = "Fourth Caption"; tab.icon = VaadinIcons.CARET_LEFT
        isMargin = true; styleName = "color1"
        label("Inmensae subtilitatis, obscuris et malesuada fames. Quisque ut dolor gravida, placerat libero vel, euismod.") {
            w = fillParent
        }
    }
    block()
}
