/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.beveragebuddy.ui.categories

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.ModifierKey.Alt
import com.github.mvysny.kaributools.addShortcut
import com.vaadin.flow.component.Key.KEY_E
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.backend.ReviewService
import com.vaadin.starter.beveragebuddy.ui.MainLayout
import com.vaadin.starter.beveragebuddy.ui.Toolbar
import com.vaadin.starter.beveragebuddy.ui.toolbarView
import kotlinx.css.Align
import kotlinx.css.alignItems
import kotlin.invoke

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "categories-lit", layout = MainLayout::class)
@PageTitle("Categories List")
class CategoriesListLit : KComposite() {

    private lateinit var header: H3
    private lateinit var toolbar: Toolbar
    private lateinit var grid: Grid<Category>

    // can't retrieve GridContextMenu from Grid: https://github.com/vaadin/vaadin-grid-flow/issues/523
    lateinit var gridContextMenu: GridContextMenu<Category>
        private set

    private val editorDialog = CategoryEditorDialog(
        { category -> saveCategory(category) },
        { deleteCategory(it) })

    private val root = ui {
        verticalLayout(false) {
            content { align(stretch, top) }
            toolbar = toolbarView("New category") {
                onSearch = { updateView() }
                onCreate = { editorDialog.createNew() }
            }
            header = h3()
            grid = grid {
                isExpand = true

                columnFor(Category::name) {
                    setHeader("Category")
                }
                column({ it.getReviewCount() }) {
                    setHeader("Beverages")
                }
                componentColumn({ cat -> createEditButton(cat) }) {
                    flexGrow = 0; key = "edit"
                }

                column(buildLitRenderer<Category> {

                    val onButtonClick = function("onButtonClick") { category ->
                        edit(category)
                    }

                    templateExpression {

                        /**
                         * <vaadin-button class="category__edit" theme="tertiary" tabindex="0" role="button">Edit<vaadin-icon icon="vaadin:edit" slot="prefix"></vaadin-icon></vaadin-button>
                         */
                        button(cssClass("category__edit"), themeVariant(ButtonVariant.LUMO_TERTIARY), click(onButtonClick)) {
                            icon(icon(VaadinIcon.EDIT.create()))
                            +"Edit"
                        }
                    }
                })

                gridContextMenu = gridContextMenu {
                    item("New", { editorDialog.createNew() })
                    item("Edit (Alt+E)", { cat -> if (cat != null) edit(cat) })
                    item("Delete", { cat -> if (cat != null) deleteCategory(cat) })
                }
            }

            addShortcut(Alt + KEY_E) {
                val category: Category? = grid.asSingleSelect().value
                if (category != null) {
                    edit(category)
                }
            }
        }
    }

    init {
        updateView()
    }

    private fun createEditButton(category: Category): Button =
        Button("Edit").apply {
            icon = Icon(VaadinIcon.EDIT)
            addClassName("category__edit")
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            onClick { edit(category) }
        }

    private fun edit(category: Category) {
        editorDialog.edit(category)
    }

    private fun Category.getReviewCount(): String = ReviewService.getTotalCountForReviewsInCategory(id!!).toString()

    private fun updateView() {
        val categories: List<Category> = CategoryService.findCategories(toolbar.searchText)
        if (!toolbar.searchText.isBlank()) {
            header.text = "Search for “${toolbar.searchText}”"
        } else {
            header.text = "Categories"
        }
        grid.setItems(categories)
    }

    private fun saveCategory(category: Category) {
        val creating = category.id == null
        CategoryService.saveCategory(category)
        val op = if (creating) "added" else "saved"
        Notification.show("Category successfully ${op}.", 3000, Notification.Position.BOTTOM_START)
        updateView()
    }

    private fun deleteCategory(category: Category) {
        CategoryService.deleteCategory(category)
        Notification.show("Category successfully deleted.", 3000, Notification.Position.BOTTOM_START)
        updateView()
    }
}
