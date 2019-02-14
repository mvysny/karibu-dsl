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
package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v10.ModifierKey.*
import com.vaadin.flow.component.Key.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.backend.Review
import com.vaadin.starter.beveragebuddy.backend.ReviewService

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "categories", layout = MainLayout::class)
@PageTitle("Categories List")
class CategoriesList : KComposite() {

    private lateinit var searchField: TextField
    private lateinit var grid: Grid<Category>
    // currently there is no way to retrieve GridContextMenu from Grid: https://github.com/vaadin/vaadin-grid-flow/issues/523
    lateinit var gridContextMenu: GridContextMenu<Category>

    private val form = CategoryEditorDialog(
            { category, operation -> saveCategory(category, operation) },
            { deleteCategory(it) })

    private val root = ui {
        verticalLayout {
            content { align(stretch, middle) }
            addClassName("categories-list")

            div {
                // view toolbar
                addClassName("view-toolbar")
                searchField = textField {
                    prefixComponent = Icon(VaadinIcon.SEARCH)
                    addClassName("view-toolbar__search-field")
                    placeholder = "Search"
                    addValueChangeListener { updateView() }
                    valueChangeMode = ValueChangeMode.EAGER
                }
                button("New category (Alt+N)", Icon("lumo", "plus")) {
                    setPrimary()
                    addClassName("view-toolbar__button")
                    addClickListener { form.open(Category(null, ""), AbstractEditorDialog.Operation.ADD) }
                    addClickShortcut(Alt + KEY_N)
                }
            }
            grid = grid {
                addColumnFor(Category::name) {
                    setHeader("Category")
                }
                addColumn({ it.getReviewCount() }).setHeader("Beverages")
                addColumn(ComponentRenderer<Button, Category>({ cat -> createEditButton(cat) })).apply {
                    flexGrow = 0
                    key = "edit"
                }

                // Grid does not yet implement HasStyle
                element.classList.add("categories")
                element.setAttribute("theme", "row-dividers")

                gridContextMenu = gridContextMenu {
                    item("Edit (Alt+E)", { cat -> if (cat != null) edit(cat) })
                }
            }

            addShortcut(Alt + KEY_E) {
                val cat = grid.asSingleSelect().value
                if (cat != null) {
                    edit(cat)
                }
            }
        }
    }

    init {
        updateView()
    }

    private fun createEditButton(category: Category): Button =
        Button("Edit").apply {
            icon = Icon("lumo", "edit")
            addClassName("review__edit")
            element.setAttribute("theme", "tertiary")
            addClickListener  { _ -> edit(category) }
        }

    private fun edit(category: Category) {
        form.open(category, AbstractEditorDialog.Operation.EDIT)
    }

    private fun updateView() {
        val categories: List<Category> = CategoryService.findCategories(searchField.value ?: "")
        grid.setItems(categories)
    }

    private fun saveCategory(category: Category, operation: AbstractEditorDialog.Operation) {
        CategoryService.saveCategory(category)
        Notification.show("Category successfully ${operation.nameInText}ed.", 3000, Notification.Position.BOTTOM_START)
        updateView()
    }

    private fun deleteCategory(category: Category) {
        val reviewsInCategory = ReviewService.findReviews(category.name)
        reviewsInCategory.forEach { review ->
            review.category = null
            ReviewService.saveReview(review)
        }
        CategoryService.deleteCategory(category)
        Notification.show("Category successfully deleted.", 3000, Notification.Position.BOTTOM_START)
        updateView()
    }
}

fun Category.getReviewCount(): String {
    val reviewsInCategory: List<Review> = ReviewService.findReviews(name)
    val totalCount: Int = reviewsInCategory.sumBy { it.count }
    return totalCount.toString()
}
