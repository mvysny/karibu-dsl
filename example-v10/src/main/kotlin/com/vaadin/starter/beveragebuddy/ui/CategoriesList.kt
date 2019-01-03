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
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
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
class CategoriesList : VerticalLayout() {

    private lateinit var searchField: TextField
    private val grid: Grid<Category>

    private val form = CategoryEditorDialog(
            { category, operation -> saveCategory(category, operation) },
            { deleteCategory(it) })

    init {
        content { align(stretch, middle) }
        addClassName("categories-list")

        div { // view toolbar
            addClassName("view-toolbar")
            searchField = textField {
                prefixComponent = Icon(VaadinIcon.SEARCH)
                addClassName("view-toolbar__search-field")
                placeholder = "Search"
                addValueChangeListener { updateView() }
                valueChangeMode = ValueChangeMode.EAGER
            }
            button("New category", Icon("lumo", "plus")) {
                setPrimary()
                addClassName("view-toolbar__button")
                addClickListener { form.open(Category(null, ""), AbstractEditorDialog.Operation.ADD) }
            }
        }
        grid = grid {
            addColumnFor(Category::name) {
                setHeader("Category")
            }
            addColumn({ it.getReviewCount() }).setHeader("Beverages")
            addColumn(ComponentRenderer<Button, Category>({ cat -> createEditButton(cat) })).flexGrow = 0

            // Grid does not yet implement HasStyle
            element.classList.add("categories")
            element.setAttribute("theme", "row-dividers")
            asSingleSelect().addValueChangeListener {
                if (it.value != null) {  // deselect fires yet another selection event, this time with null Category.
                    selectionChanged(it.value.id!!)
                    selectionModel.deselect(it.value)
                }
            }
        }

        updateView()
    }

    private fun createEditButton(category: Category): Button =
        Button("Edit").apply {
            icon = Icon("lumo", "edit")
            addClassName("review__edit")
            element.setAttribute("theme", "tertiary")
            addClickListener  { _ -> form.open(category, AbstractEditorDialog.Operation.EDIT) }
        }

    private fun selectionChanged(categoryId: Long) {
        form.open(CategoryService.getById(categoryId), AbstractEditorDialog.Operation.EDIT)
    }

    private fun Category.getReviewCount(): String {
        val reviewsInCategory: List<Review> = ReviewService.findReviews(name)
        val totalCount: Int = reviewsInCategory.sumBy { it.count }
        return totalCount.toString()
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
