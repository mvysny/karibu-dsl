package com.example.v8.uitest.example

import com.github.vok.karibudsl.*
import com.vaadin.event.ShortcutAction
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.AbstractErrorMessage
import com.vaadin.server.Page
import com.vaadin.server.UserError
import com.vaadin.shared.Position
import com.vaadin.shared.ui.ErrorLevel
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@AutoView
class CommonElementsView : VerticalLayout(), View {
    init {
        isSpacing = false
        title("Common UI Elements")

        gridLayout(2, 3) {
            w = fillParent; isSpacing = true

            panel("Loading Indicator") {
                verticalLayout {
                    label("You can test the loading indicator by pressing the buttons.")
                    cssLayout {
                        caption = "Show the loading indicator for…"
                        styleName = ValoTheme.LAYOUT_COMPONENT_GROUP
                        button("0.8") {
                            onLeftClick { Thread.sleep(800) }
                        }
                        button("3") {
                            onLeftClick { Thread.sleep(3000) }
                        }
                        button("15") {
                            onLeftClick { Thread.sleep(15000) }
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

            addComponent(Panel("Notifications").apply {
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
                        val typeCommand = { selectedItem: MenuBar.MenuItem ->
                            typeString = if (selectedItem.text == "Humanized") "" else selectedItem.text.toLowerCase()
                            updateTypeStyle()
                            items.forEach { item -> item.isChecked = false }
                            selectedItem.isChecked = true
                        }
                        item("Humanized", menuSelected = typeCommand) {
                            isCheckable = true
                            isChecked = true
                        }
                        item("Tray", menuSelected = typeCommand) { isCheckable = true }
                        item("Warning", menuSelected = typeCommand) { isCheckable = true }
                        item("Error", menuSelected = typeCommand) { isCheckable = true }
                        item("System", menuSelected = typeCommand) { isCheckable = true }
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
                        button {
                            // clear
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
            }, 1, 0, 1, 2)

            panel("Dialogs") {
                verticalLayout {
                    val win = DemoDialog()
                    win.footerVisible = true
                    isSpacing = true; isMargin = true
                    menuBar {
                        caption = "Content"; styleName = ValoTheme.MENUBAR_SMALL
                        item("Auto Height", { win.autoHeight = it.isChecked }).isCheckable = true
                        item("Tabs", { win.tabsVisible = it.isChecked }).isCheckable = true
                        item("Footer", { win.footerVisible = it.isChecked }) {
                            isCheckable = true
                            isChecked = true
                        }
                    }
                    menuBar {
                        caption = "Toolbars"; styleName = ValoTheme.MENUBAR_SMALL
                        item("Footer Toolbar", { win.footerToolbar = it.isChecked }).isCheckable = true
                        item("Top Toolbar", { win.toolbarVisible = it.isChecked }).isCheckable = true
                        item("Borderless Toolbars", { win.toolbarStyle = if (it.isChecked) ValoTheme.MENUBAR_BORDERLESS else null }).isCheckable = true
                    }
                    menuBar {
                        caption = "Options"; styleName = ValoTheme.MENUBAR_SMALL
                        item("Caption", { win.win.caption = if (it.isChecked) "Window Caption" else null }) {
                            isCheckable = true
                            isChecked = true
                        }
                        item("Closable", { win.win.isClosable = it.isChecked }).isCheckable = true
                        item("Resizable", { win.win.isResizable = it.isChecked }).isCheckable = true
                        item("Modal", { win.win.isModal = it.isChecked }).isCheckable = true
                    }
                    val show = button("Open Window") {
                        onLeftClick { event ->
                            win.show()
                            event.button.isEnabled = false
                        }
                        setPrimary()
                    }
                    checkBox("Hidden") {
                        addValueChangeListener { event -> win.win.isVisible = !event.value }
                    }
                    win.win.addCloseListener { show.isEnabled = true }
                }
            }

            panel("Tooltips") {
                horizontalLayout {
                    isMargin = true; styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
                    label("Try out different tooltips/descriptions by hovering over the labels.")
                    label("Simple") {
                        styleName = ValoTheme.LABEL_BOLD
                        description = "Simple tooltip message"
                    }
                    label("Long") {
                        styleName = ValoTheme.LABEL_BOLD
                        description = "Long tooltip message. Inmensae subtilitatis, obscuris et malesuada fames. Salutantibus vitae elit libero, a pharetra augue."
                    }
                    label("HTML tooltip") {
                        styleName = ValoTheme.LABEL_BOLD
                        description = "<div><h1>Ut enim ad minim veniam, quis nostrud exercitation</h1><p><span>Morbi fringilla convallis sapien, id pulvinar odio volutpat.</span> <span>Vivamus sagittis lacus vel augue laoreet rutrum faucibus.</span> <span>Donec sed odio operae, eu vulputate felis rhoncus.</span> <span>At nos hinc posthac, sitientis piros Afros.</span> <span>Tu quoque, Brute, fili mi, nihil timor populi, nihil!</span></p><p><span>Gallia est omnis divisa in partes tres, quarum.</span> <span>Praeterea iter est quasdam res quas ex communi.</span> <span>Cum ceteris in veneratione tui montes, nascetur mus.</span> <span>Quam temere in vitiis, legem sancimus haerentia.</span> <span>Idque Caesaris facere voluntate liceret: sese habere.</span></p></div>"
                    }
                    label("With an error message") {
                        styleName = ValoTheme.LABEL_BOLD
                        description = "Simple tooltip message"
                        componentError = UserError("Something terrible has happened")
                    }
                    label("With a long error message") {
                        styleName = ValoTheme.LABEL_BOLD
                        description = "Simple tooltip message"
                        componentError = UserError(
                                "<h2>Contra legem facit qui id facit quod lex prohibet <span>Tityre, tu patulae recubans sub tegmine fagi  dolor.</span> <span>Tityre, tu patulae recubans sub tegmine fagi  dolor.</span> <span>Prima luce, cum quibus mons aliud  consensu ab eo.</span> <span>Quid securi etiam tamquam eu fugiat nulla pariatur.</span> <span>Fabio vel iudice vincam, sunt in culpa qui officia.</span> <span>Nihil hic munitissimus habendi senatus locus, nihil horum?</span></p><p><span>Plura mihi bona sunt, inclinet, amari petere vellent.</span> <span>Integer legentibus erat a ante historiarum dapibus.</span> <span>Quam diu etiam furor iste tuus nos eludet?</span> <span>Nec dubitamus multa iter quae et nos invenerat.</span> <span>Quisque ut dolor gravida, placerat libero vel, euismod.</span> <span>Quae vero auctorem tractata ab fiducia dicuntur.</span></h2>",
                                AbstractErrorMessage.ContentMode.HTML,
                                ErrorLevel.CRITICAL)
                    }
                    label("Error message only") {
                        styleName = ValoTheme.LABEL_BOLD
                        componentError = UserError("Something terrible has happened")
                    }
                }
            }
        }
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}

private class DemoDialog {
    val win = Window("Window Caption").apply {
        w = 380.px
        h = 300.px
        isClosable = false
        isResizable = false
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE)
    }
    var footerVisible: Boolean = false
        set(value) { field = value; updateContent() }
    var autoHeight: Boolean = false
        set(value) {
            field = value
            win.h = if (value) wrapContent else 300.px
            updateContent()
        }
    var tabsVisible: Boolean = false
        set(value) { field = value; updateContent() }
    var toolbarVisible: Boolean = false
        set(value) { field = value; updateContent() }
    var footerToolbar: Boolean = false
        set(value) { field = value; updateContent() }
    var toolbarStyle: String? = null
        set(value) { field = value; updateContent() }

    private fun updateContent() {
        win.content = windowContent()
    }

    private fun windowContent() = VerticalLayout().apply {
        val content: Component
        if (toolbarVisible) {
            sampleToolBar {
                w = wrapContent
                addStyleNames(toolbarStyle ?: "", ValoTheme.WINDOW_TOP_TOOLBAR)
            }
        }
        if (tabsVisible) {
            content = tabSheet {
                setSizeFull(); styleName = ValoTheme.TABSHEET_PADDED_TABBAR
                verticalLayout {
                    isMargin = true
                    label {
                        w = fillParent
                        html("<h2>Subtitle</h2><p>Normal type for plain text. Etiam at risus et justo dignissim congue. Phasellus laoreet lorem vel dolor tempus vehicula.</p><p>Quisque ut dolor gravida, placerat libero vel, euismod. Etiam habebis sem dicantur magna mollis euismod. Nihil hic munitissimus habendi senatus locus, nihil horum? Curabitur est gravida et libero vitae dictum. Ullamco laboris nisi ut aliquid ex ea commodi consequat. Morbi odio eros, volutpat ut pharetra vitae, lobortis sed nibh.</p>")
                    }
                }
                lastTab.caption = "Selected"
                addTab(Label(), "Another")
                addTab(Label(), "One more")
            }
        } else if (!autoHeight) {
            content = panel {
                setSizeFull()
                addStyleNames(ValoTheme.PANEL_BORDERLESS, ValoTheme.PANEL_SCROLL_INDICATOR)
                verticalLayout {
                    isMargin = true
                    label {
                        w = fillParent
                        html("<h2>Subtitle</h2><p>Normal type for plain text. Etiam at risus et justo dignissim congue. Phasellus laoreet lorem vel dolor tempus vehicula.</p><p>Quisque ut dolor gravida, placerat libero vel, euismod. Etiam habebis sem dicantur magna mollis euismod. Nihil hic munitissimus habendi senatus locus, nihil horum? Curabitur est gravida et libero vitae dictum. Ullamco laboris nisi ut aliquid ex ea commodi consequat. Morbi odio eros, volutpat ut pharetra vitae, lobortis sed nibh.</p>")
                    }
                }
            }
        } else {
            content = label {
                w = fillParent
                html("<h2>Subtitle</h2><p>Normal type for plain text. Etiam at risus et justo dignissim congue. Phasellus laoreet lorem vel dolor tempus vehicula.</p><p>Quisque ut dolor gravida, placerat libero vel, euismod. Etiam habebis sem dicantur magna mollis euismod. Nihil hic munitissimus habendi senatus locus, nihil horum? Curabitur est gravida et libero vitae dictum. Ullamco laboris nisi ut aliquid ex ea commodi consequat. Morbi odio eros, volutpat ut pharetra vitae, lobortis sed nibh.</p>")
            }
            isMargin = true
        }
        if (footerVisible) {
            if (!footerToolbar) {
                horizontalLayout {
                    w = fillParent; isSpacing = true; styleName = ValoTheme.WINDOW_BOTTOM_TOOLBAR
                    label("Footer text") { w = wrapContent; isExpanded = true }
                    button("OK") { setPrimary() }
                    button("Cancel")
                }
            } else {
                sampleToolBar {
                    styleName = toolbarStyle; w = wrapContent
                }
            }
        }
        if (!autoHeight) {
            setSizeFull()
            setExpandRatio(content, 1f)
        }
    }

    fun show() {
        updateContent()
        UI.getCurrent().addWindow(win)
        win.center()
        win.focus()
    }
}