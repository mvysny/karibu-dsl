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
package com.vaadin.starter.beveragebuddy.ui.reviews

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.starter.beveragebuddy.backend.Review
import com.vaadin.starter.beveragebuddy.backend.ReviewService
import com.vaadin.starter.beveragebuddy.ui.*

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "", layout = MainLayout::class)
@PageTitle("Review List")
class ReviewsList : KComposite() {

    private lateinit var toolbar: Toolbar
    private lateinit var header: H3
    private lateinit var reviewsGrid: Grid<Review>
    private val editDialog = ReviewEditorDialog(
        { review-> save(review) },
        { this.delete(it) })

    private val root = ui {
        verticalLayout {
            isPadding = false; content { align(stretch, top) }
            toolbar = toolbarView("New review") {
                onSearch = { updateList() }
                onCreate = { editDialog.createNew() }
            }
            header = h3 {
                setId("header")
            }
            reviewsGrid = grid {
                isExpand = true
                addClassName("reviews")
                addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_NO_BORDER)
                addColumn(ComponentRenderer<ReviewItem, Review>({ review ->
                    val item = ReviewItem(review)
                    item.onEdit = { editDialog.edit(ReviewService.get(review.id!!)) }
                    item
                }))
            }
        }
    }

    init {
        updateList()
    }

    private fun save(review: Review) {
        val creating = review.id == null
        ReviewService.saveReview(review)
        val op = if (creating) "added" else "saved"
        updateList()
        Notification.show("Beverage successfully ${op}.", 3000, Notification.Position.BOTTOM_START)
    }

    private fun delete(review: Review) {
        ReviewService.deleteReview(review)
        updateList()
        Notification.show("Beverage successfully deleted.", 3000, Notification.Position.BOTTOM_START)
    }

    private fun updateList() {
        val reviews: List<Review> = ReviewService.findReviews(toolbar.searchText)
        if (toolbar.searchText.isBlank()) {
            header.text = "Reviews"
            header.add(Span("${reviews.size} in total"))
        } else {
            header.text = "Search for “${toolbar.searchText}”"
            header.add(Span("${reviews.size} results"))
        }
        reviewsGrid.setItems(reviews)
    }
}

/**
 * Shows a single row stripe with information about a single [ReviewWithCategory].
 */
class ReviewItem(val review: Review) : KComposite() {
    /**
     * Fired when this item is to be edited (the "Edit" button is pressed by the User).
     */
    var onEdit: () -> Unit = {}

    private val root = ui {
        div {
            addClassName("review")
            div {
                addClassName("review__rating")
                p(review.score.toString()) {
                    className = "review__score"
                    element.setAttribute("data-score", review.score.toString())
                }
                p(review.count.toString()) {
                    className = "review__count"
                    span("times tasted")
                }
            }
            div {
                addClassName("review__details")
                h4(review.name) {
                    addClassName("review__name")
                }
                p {
                    className = "review__category"
                    if (review.category != null) {
                        element.themeList.add("badge small")
                        element.style.set("--category", review.category.toString())
                        text = review.category!!.name
                    } else {
                        element.style.set("--category", "-1")
                        text = "Undefined"
                    }
                }
            }
            div {
                className = "review__date"
                h5("Last tasted")
                p(review.date.toString())
            }
            button("Edit") {
                icon = VaadinIcon.EDIT.create()
                className = "review__edit"
                addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                onLeftClick { onEdit() }
            }
        }
    }
}
