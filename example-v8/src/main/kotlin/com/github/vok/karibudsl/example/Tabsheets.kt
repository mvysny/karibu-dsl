package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.data.HasValue
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
        label("Tabs") {
            addStyleNames(ValoTheme.LABEL_H2, ValoTheme.LABEL_COLORED)
        }

        val update = { event: HasValue.ValueChangeEvent<Boolean>? ->
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
            tabs = getTabSheet(captions.value!!, style.trim(),
                    closable.value!!, overflow.value!!, icons.value!!,
                    disable.value!!)
            addComponent(tabs)
        }

        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            closable = checkBox("Closable") {
                addValueChangeListener(update)
            }
            overflow = checkBox("Overflow") {
                addValueChangeListener(update)
            }
            captions = checkBox("Captions", true) {
                addValueChangeListener(update)
            }
            icons = checkBox("Icons") {
                addValueChangeListener(update)
            }
            disable = checkBox("Disable tabs") {
                addValueChangeListener(update)
            }
        }
        label("Additional Styles") {
            styleName = ValoTheme.LABEL_H3
        }
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            margin = MarginInfo(false, false, true, false)
            framed = checkBox("Framed", true) {
                addValueChangeListener(update)
            }
            centered = checkBox("Centered tabs") {
                addValueChangeListener(update)
            }
            rightAlign = checkBox("Right-aligned tabs") {
                addValueChangeListener(update)
            }
            equal = checkBox("Equal-width tabs") {
                addValueChangeListener(update)
            }
            padded = checkBox("Padded tabbar") {
                addValueChangeListener(update)
            }
            compact = checkBox("Compact") {
                addValueChangeListener(update)
            }
            iconsOnTop = checkBox("Icons on top") {
                addValueChangeListener(update)
            }
            selectedOnly = checkBox("Selected tab closable") {
                addValueChangeListener(update)
            }
        }

        // Generate initial view
        update(null)
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }

    companion object {

        internal fun getTabSheet(caption: Boolean, style: String, closable: Boolean,
                                 scrolling: Boolean, icon: Boolean, disable: Boolean): TabSheet {

            val ts = TabSheet().apply {
                styleName = style
                val sg = StringGenerator.iterator()

                for (i in 1..if (scrolling) 20 else 3) {
                    verticalLayout {
                        lastTab.caption = if (caption)
                            "${sg.next().capitalize()} ${sg.next()}"
                        else
                            null
                        lastTab.isClosable = closable
                        lastTab.isEnabled = !disable

                        // First tab is always enabled
                        if (i == 1) {
                            lastTab.isEnabled = true
                        }

                        if (icon) {
                            lastTab.icon = VaadinIcons.values()[i % VaadinIcons.values().size]
                        }
                        label("Content for tab " + i)
                        if (i == 2) {
                            label("Excepteur sint obcaecat cupiditat non proident culpa. Magna pars studiorum, prodita quaerimus.")
                        }
                    }
                    addSelectedTabChangeListener { Thread.sleep(500) }
                }
            }
            return ts
        }
    }
}
