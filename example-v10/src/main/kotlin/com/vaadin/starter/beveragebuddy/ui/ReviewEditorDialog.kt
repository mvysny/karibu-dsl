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

import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.HasComponents
import java.time.LocalDate

import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.backend.Review
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.textfield.TextField

/**
 * A dialog for editing [Review] objects.
 */
class ReviewEditorDialog(saveHandler: (Review, AbstractEditorDialog.Operation) -> Unit, deleteHandler: (Review) -> Unit)
    : AbstractEditorDialog<Review>("Review", saveHandler, deleteHandler, Review::class.java) {

    private val categoryBox: ComboBox<Category>
    private val scoreBox: ComboBox<String>
    private val lastTasted: DatePicker
    private val beverageName: TextField
    private val timesTasted: TextField

    init {
        formLayout.apply {
            beverageName = textField("Beverage name") {
                // no need to have validators here: they are automatically picked up from the bean field.
                bind(binder).trimmingConverter().bind(Review::name)
            }
            timesTasted = textField("Times tasted") {
                pattern = "[0-9]*"
                isPreventInvalidInput = true
                bind(binder).toInt().bind(Review::count)
            }
            categoryBox = comboBox("Choose a category") {
                setItemLabelGenerator { it.name }
                isAllowCustomValue = false
                setItems(CategoryService.findAll())
                bind(binder).bind(Review::category)
            }
            lastTasted = datePicker("Choose the date") {
                max = LocalDate.now()
                min = LocalDate.of(1, 1, 1)
                value = LocalDate.now()
                bind(binder).bind(Review::date)
            }
            scoreBox = comboBox("Mark a score") {
                isAllowCustomValue = false
                setItems("1", "2", "3", "4", "5")
                bind(binder).toInt().bind(Review::score)
            }
        }
    }

    override fun confirmDelete() {
        openConfirmationDialog("""Delete beverage "${currentItem!!.name}"?""")
    }
}
