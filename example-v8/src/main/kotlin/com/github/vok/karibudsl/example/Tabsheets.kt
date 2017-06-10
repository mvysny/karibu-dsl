package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.AutoView
import com.vaadin.data.HasValue
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@AutoView
class Tabsheets : VerticalLayout(), View {

    internal var tabs: TabSheet? = null

    init {
        isSpacing = false

        val h1 = Label("Tabs")
        h1.addStyleName(ValoTheme.LABEL_H1)
        addComponent(h1)

        var wrap = HorizontalLayout()
        wrap.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
        addComponent(wrap)

        val closable = CheckBox("Closable")
        wrap.addComponent(closable)

        val overflow = CheckBox("Overflow")
        wrap.addComponent(overflow)

        val caption = CheckBox("Captions", true)
        wrap.addComponent(caption)

        val icon = CheckBox("Icons")
        wrap.addComponent(icon)

        val disable = CheckBox("Disable tabs")
        wrap.addComponent(disable)

        val h3 = Label("Additional Styles")
        h3.addStyleName(ValoTheme.LABEL_H3)
        addComponent(h3)

        wrap = HorizontalLayout()
        wrap.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
        wrap.margin = MarginInfo(false, false, true, false)
        addComponent(wrap)

        val framed = CheckBox("Framed", true)
        wrap.addComponent(framed)

        val centered = CheckBox("Centered tabs")
        wrap.addComponent(centered)

        val rightAlign = CheckBox("Right-aligned tabs")
        wrap.addComponent(rightAlign)

        val equal = CheckBox("Equal-width tabs")
        wrap.addComponent(equal)

        val padded = CheckBox("Padded tabbar")
        wrap.addComponent(padded)

        val compact = CheckBox("Compact")
        wrap.addComponent(compact)

        val iconsOnTop = CheckBox("Icons on top")
        wrap.addComponent(iconsOnTop)

        val selectedOnly = CheckBox("Selected tab closable")
        wrap.addComponent(selectedOnly)

        val update = { event: HasValue.ValueChangeEvent<Boolean> ->
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
            tabs = getTabSheet(caption.value!!, style.trim { it <= ' ' },
                    closable.value!!, overflow.value!!, icon.value!!,
                    disable.value!!)
            addComponent(tabs)
        }
        closable.addValueChangeListener(update)
        overflow.addValueChangeListener(update)
        caption.addValueChangeListener(update)
        icon.addValueChangeListener(update)
        disable.addValueChangeListener(update)
        framed.addValueChangeListener(update)
        centered.addValueChangeListener(update)
        rightAlign.addValueChangeListener(update)
        equal.addValueChangeListener(update)
        padded.addValueChangeListener(update)
        compact.addValueChangeListener(update)
        iconsOnTop.addValueChangeListener(update)
        selectedOnly.addValueChangeListener(update)

        // Generate initial view
        icon.value = true
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }

    companion object {

        internal fun getTabSheet(caption: Boolean, style: String, closable: Boolean,
                                 scrolling: Boolean, icon: Boolean, disable: Boolean): TabSheet {

            val ts = TabSheet()
            ts.addStyleName(style)
            val sg = StringGenerator.iterator()

            for (i in 1..if (scrolling) 10 else 3) {
                val tabcaption = if (caption)
                    sg.next().capitalize() + " " + sg.next()
                else
                    null

                val content = VerticalLayout()
                content.addComponent(Label("Content for tab " + i))
                if (i == 2) {
                    content.addComponent(Label(
                            "Excepteur sint obcaecat cupiditat non proident culpa. Magna pars studiorum, prodita quaerimus."))
                }
                val t = ts.addTab(content, tabcaption)
                t.isClosable = closable
                t.isEnabled = !disable

                // First tab is always enabled
                if (i == 1) {
                    t.isEnabled = true
                }

                if (icon) {
                    t.icon = VaadinIcons.values()[i % VaadinIcons.values().size]
                }
            }

            ts.addSelectedTabChangeListener {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            return ts
        }
    }

}
