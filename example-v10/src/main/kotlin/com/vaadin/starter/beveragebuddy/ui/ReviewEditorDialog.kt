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
import java.time.LocalDate
import java.util.function.BiConsumer
import java.util.function.Consumer

import com.vaadin.data.validator.DateRangeValidator
import com.vaadin.data.validator.IntegerRangeValidator
import com.vaadin.data.validator.StringLengthValidator
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.backend.Review
import com.vaadin.ui.combobox.ComboBox
import com.vaadin.ui.datepicker.DatePicker
import com.vaadin.ui.textfield.TextField

/**
 * A dialog for editing [Review] objects.
 */
class ReviewEditorDialog(saveHandler: BiConsumer<Review, AbstractEditorDialog.Operation>,
                         deleteHandler: Consumer<Review>) : AbstractEditorDialog<Review>("Review", saveHandler, deleteHandler,
        Review::class.java) {

    @Transient private val categoryService = CategoryService

    private lateinit var categoryBox: ComboBox<Category>
    private lateinit var scoreBox: ComboBox<String>
    private lateinit var lastTasted: DatePicker
    private lateinit var beverageName: TextField
    private lateinit var timesTasted: TextField

    init {
        formLayout.apply {
            beverageName = textField("Beverage name") {
                bind(binder)
                        .trimmingConverter()
                        .withValidator(StringLengthValidator(
                                "Beverage name must contain at least 3 printable characters",
                                3, null))
                        .bindN(Review::name)
            }
            timesTasted = textField("Times tasted") {
                pattern = "[0-9]*"
                isPreventInvalidInput = true
                bind(binder)
                        .toInt()
                        .withValidator(IntegerRangeValidator(
                                "The tasting count must be between 1 and 99.", 1, 99))
                        .bindN(Review::count)
            }
            categoryBox = comboBox("Choose a category") {
                setItemLabelGenerator { it.name }
                isAllowCustomValue = false
                setItems(categoryService.findAll())
                bind(binder).bindN(Review::category)
            }
            lastTasted = datePicker("Choose the date") {
                max = LocalDate.now()
                min = LocalDate.of(1, 1, 1)
                value = LocalDate.now()
                bind(binder)
                        .withValidator({ it != null },
                                "The date should be in MM/dd/yyyy format.")
                        .withValidator(DateRangeValidator(
                                "The date should be neither Before Christ nor in the future.",
                                LocalDate.of(1, 1, 1), LocalDate.now()))
                        .bindN(Review::date)
            }
            scoreBox = comboBox("Mark a score") {
                isAllowCustomValue = false
                setItems("1", "2", "3", "4", "5")
                bind(binder)
                        .toInt()
                        .withValidator(IntegerRangeValidator(
                                "The tasting count must be between 1 and 5.", 1, 5))
                        .bindN(Review::score)
            }
        }
    }

    override fun confirmDelete() {
        openConfirmationDialog("""Delete beverage "${currentItem!!.name}"?""")
    }
}
