package com.example.v8.uitest.example

import com.github.vok.karibudsl.*
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@AutoView
class Tabsheets : VerticalLayout(), View {

    private lateinit var closable: CheckBox
    private lateinit var overflow: CheckBox
    private lateinit var captions: CheckBox
    private lateinit var icons: CheckBox
    private lateinit var disable: CheckBox
    private lateinit var framed: CheckBox
    private lateinit var centered: CheckBox
    private lateinit var rightAlign: CheckBox
    private lateinit var equal: CheckBox
    private lateinit var padded: CheckBox
    private lateinit var compact: CheckBox
    private lateinit var iconsOnTop: CheckBox
    private lateinit var selectedOnly: CheckBox

    internal var tabs: TabSheet? = null

    init {
        isSpacing = false
        title("Tabs")

        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            closable = checkBox("Closable")
            overflow = checkBox("Overflow")
            captions = checkBox("Captions", true)
            icons = checkBox("Icons", true)
            disable = checkBox("Disable tabs")
        }
        label("Additional Styles") {
            styleName = ValoTheme.LABEL_H3
        }
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            margin = MarginInfo(false, false, true, false)
            framed = checkBox("Framed", true)
            centered = checkBox("Centered tabs")
            rightAlign = checkBox("Right-aligned tabs")
            equal = checkBox("Equal-width tabs")
            padded = checkBox("Padded tabbar")
            compact = checkBox("Compact")
            iconsOnTop = checkBox("Icons on top")
            selectedOnly = checkBox("Selected tab closable")
        }

        // Generate initial view
        update()

        // register change listeners
        walk().filterIsInstance<CheckBox>().forEach { it.addValueChangeListener { update() } }
    }

    private fun update() {
        var style = if (framed.value) "framed " else ""
        style += if (centered.value) " centered-tabs" else ""
        style += if (rightAlign.value) " right-aligned-tabs" else ""
        style += if (equal.value) " equal-width-tabs" else ""
        style += if (padded.value) " padded-tabbar" else ""
        style += if (compact.value) " compact-tabbar" else ""
        style += if (iconsOnTop.value) " icons-on-top" else ""
        style += if (selectedOnly.value) " only-selected-closable" else ""

        if (tabs != null) {
            removeComponent(tabs)
        }
        tabs = tabSheet {
            styleName = style.trim()
            val sg = StringGenerator.iterator()
            for (i in 1..if (overflow.value!!) 20 else 3) {
                verticalLayout {
                    tab.caption = if (captions.value!!) "${sg.next().capitalize()} ${sg.next()}" else null
                    tab.isClosable = closable.value!!
                    tab.isEnabled = !disable.value!!

                    if (icons.value!!) {
                        tab.icon = VaadinIcons.values()[i % VaadinIcons.values().size]
                    }
                    label("Content for tab $i")
                    if (i == 2) {
                        label("Excepteur sint obcaecat cupiditat non proident culpa. Magna pars studiorum, prodita quaerimus.")
                    }
                }
                // demo the loading progress bar in the tabsheet
                addSelectedTabChangeListener { Thread.sleep(500) }
            }
            // First tab is always enabled
            getTab(0).isEnabled = true
        }
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }
}
