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
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.backend.Review
import com.vaadin.starter.beveragebuddy.ui.ConfirmationDialog
import com.vaadin.starter.beveragebuddy.ui.EditorDialogFrame
import com.vaadin.starter.beveragebuddy.ui.EditorForm
import java.time.LocalDate

/**
 * A dialog for editing [Review] objects.
 */
class ReviewEditorForm :  FormLayout(), EditorForm<Review> {
    override val itemType: String get() = "Review"
    // to propagate the changes made in the fields by the user, we will use binder to bind the field to the Review property.
    override val binder: Binder<Review> = beanValidationBinder()

    init {
        responsiveSteps {
            "0"(1); "50em"(2)
        }

        textField("Beverage name") {
            // no need to have validators here: they are automatically picked up from the bean field.
            bind(binder).trimmingConverter().bind(Review::name)
        }
        integerField("Times tasted") {
            bind(binder).bind(Review::count)
        }
        comboBox<Category>("Choose a category") {
            // we need to show a list of options for the user to choose from. For every option we need to retain at least:
            // 1. the category ID (to bind it to Review::category)
            // 2. the category name (to show it to the user when the combobox's option list is expanded)
            // since the Category class already provides these values, we will simply use that as the data source for the options.
            //
            // now we need to configure the item label generator so that we can extract the name out of Category and display it to the user:
            setItemLabelGenerator { it.name }

            // can't create new Categories here
            isAllowCustomValue = false

            // provide the list of options as a DataProvider, providing instances of Category
            setItems({ item: Category, filterText: String -> CategoryService.categoryMatchesFilter(item, filterText) }, CategoryService.findAll())

            // bind the combo box to the Review::category field so that changes done by the user are stored.
            bind(binder).bind(Review::category)
        }
        datePicker("Choose the date") {
            max = LocalDate.now()
            min = LocalDate.of(1, 1, 1)
            value = LocalDate.now()
            bind(binder).bind(Review::date)
        }
        comboBox<String>("Mark a score") {
            isAllowCustomValue = false
            setItems("1", "2", "3", "4", "5")
            bind(binder).toInt().bind(Review::score)
        }
    }
}

/**
 * A dialog for editing [Review] objects.
 * @property onSaveItem Callback to save the edited item
 * @property onDeleteItem Callback to delete the edited item
 */
class ReviewEditorDialog(private val onSaveItem: (Review) -> Unit, private val onDeleteItem: (Review) -> Unit) {
    fun createNew() {
        edit(Review())
    }

    private fun maybeDelete(frame: EditorDialogFrame<Review>, item: Review) {
        ConfirmationDialog().open(
            "Delete beverage",
            "Are you sure you want to delete beverage '${item.name}'?",
            "",
            "Delete",
            true
        ) {
            frame.close()
            onDeleteItem(item)
        }
    }

    fun edit(review: Review) {
        val frame: EditorDialogFrame<Review> = EditorDialogFrame(ReviewEditorForm())
        frame.onSaveItem = onSaveItem
        frame.onDeleteItem = { item -> maybeDelete(frame, item) }
        frame.open(review, review.id == null)
    }
}
