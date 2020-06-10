package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v10.ModifierKey.Alt
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Key.KEY_N
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

/**
 * A toolbar with a search box and a "Create New" button. Don't forget to provide proper listeners
 * for [onSearch] and [onCreate].
 * @param createCaption the caption of the "Create New" button.
 */
class Toolbar(createCaption: String) : KComposite() {
    /**
     * Fired when the text in the search text field changes.
     */
    var onSearch: (String)->Unit = {}
    /**
     * Fired when the "Create new" button is clicked.
     */
    var onCreate: ()->Unit = {}
    private lateinit var searchField: TextField
    /**
     * Current search text. Never null, trimmed, may be blank.
     */
    val searchText: String get() = searchField.value.trim()

    private val root = ui {
        div("view-toolbar") {
            searchField = textField {
                prefixComponent = Icon(VaadinIcon.SEARCH)
                addClassName("view-toolbar__search-field")
                placeholder = "Search"
                addValueChangeListener { onSearch(searchText) }
                valueChangeMode = ValueChangeMode.EAGER
            }
            button("$createCaption (Alt+N)", Icon(VaadinIcon.PLUS)) {
                setPrimary()
                addClassName("view-toolbar__button")
                addClickListener { onCreate() }
                addClickShortcut(Alt + KEY_N)
            }
        }
    }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).toolbarView(createCaption: String, block: (@VaadinDsl Toolbar).() -> Unit = {})
        = init(Toolbar(createCaption), block)
