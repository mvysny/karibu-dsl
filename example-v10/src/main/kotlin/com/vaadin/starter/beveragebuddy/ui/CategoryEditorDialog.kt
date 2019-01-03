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
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.validator.StringLengthValidator
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.backend.ReviewService

/**
 * A dialog for editing [Category] objects.
 */
class CategoryEditorDialog(itemSaver: (Category, AbstractEditorDialog.Operation)->Unit, itemDeleter: (Category)->Unit)
    : AbstractEditorDialog<Category>("Category", itemSaver, itemDeleter, Category::class.java) {

    private val categoryNameField: TextField

    init {
        formLayout.apply {
            categoryNameField = textField("Category Name") {
                bind(binder)
                        .trimmingConverter()
                        .withValidator(StringLengthValidator(
                                "Category name must contain at least 3 printable characters",
                                3, null))
                        .withValidator(
                                { name -> CategoryService.findCategories(name ?: "").isEmpty() },
                                "Category name must be unique")
                        .bind(Category::name)
            }
        }
    }

    override fun confirmDelete() {
        val reviewCount = ReviewService.findReviews(currentItem!!.name).size
        if (reviewCount == 0) {
            deleteConfirmed(currentItem!!)
        } else {
            val additionalMessage = "Deleting the category will mark the associated reviews as “undefined”. You may link the reviews to other categories on the edit page."
            openConfirmationDialog(
                "Delete Category “${currentItem!!.name}”?",
                "There are $reviewCount reviews associated with this category.",
                additionalMessage)
        }
    }
}
