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

import com.vaadin.data.Setter
import com.vaadin.data.ValueProvider
import java.time.LocalDate
import java.util.Objects
import java.util.function.BiConsumer
import java.util.function.Consumer

import com.vaadin.data.converter.StringToIntegerConverter
import com.vaadin.data.validator.DateRangeValidator
import com.vaadin.data.validator.IntegerRangeValidator
import com.vaadin.data.validator.StringLengthValidator
import com.vaadin.function.SerializableFunction
import com.vaadin.function.SerializablePredicate
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.backend.Review
import com.vaadin.ui.ItemLabelGenerator
import com.vaadin.ui.combobox.ComboBox
import com.vaadin.ui.datepicker.DatePicker
import com.vaadin.ui.textfield.TextField

/**
 * A dialog for editing [Review] objects.
 */
class ReviewEditorDialog(saveHandler: BiConsumer<Review, AbstractEditorDialog.Operation>,
                         deleteHandler: Consumer<Review>) : AbstractEditorDialog<Review>("Review", saveHandler, deleteHandler) {

    @Transient private val categoryService = CategoryService

    private val categoryBox = ComboBox<Category>()
    private val scoreBox = ComboBox<String>()
    private val lastTasted = DatePicker()
    private val beverageName = TextField()
    private val timesTasted = TextField()

    init {

        createNameField()
        createTimesField()
        createCategoryBox()
        createDatePicker()
        createScoreBox()
    }

    private fun createScoreBox() {
        scoreBox.label = "Mark a score"
        scoreBox.isAllowCustomValue = false
        scoreBox.setItems("1", "2", "3", "4", "5")
        formLayout.add(scoreBox)

        binder.forField(scoreBox)
                .withConverter(StringToIntegerConverter(0,
                        "The score should be a number."))
                .withValidator(IntegerRangeValidator(
                        "The tasting count must be between 1 and 5.", 1, 5))
                .bind(ValueProvider<Review, Int> { it.score }, Setter<Review, Int> { obj, i -> obj.score = i })
    }

    private fun createDatePicker() {
        lastTasted.label = "Choose the date"
        lastTasted.max = LocalDate.now()
        lastTasted.min = LocalDate.of(1, 1, 1)
        lastTasted.value = LocalDate.now()
        formLayout.add(lastTasted)

        binder.forField(lastTasted)
                .withValidator(SerializablePredicate<LocalDate> { Objects.nonNull(it) },
                        "The date should be in MM/dd/yyyy format.")
                .withValidator(DateRangeValidator(
                        "The date should be neither Before Christ nor in the future.",
                        LocalDate.of(1, 1, 1), LocalDate.now()))
                .bind(ValueProvider<Review, LocalDate> { it.date }, Setter<Review, LocalDate> { obj, localDate -> obj.date = localDate })

    }

    private fun createCategoryBox() {
        categoryBox.label = "Choose a category"
        categoryBox.setItemLabelGenerator(ItemLabelGenerator<Category> { it.name })
        categoryBox.isAllowCustomValue = false
        categoryBox.setItems(categoryService.findCategories(""))
        formLayout.add(categoryBox)

        binder.forField(categoryBox).bind(ValueProvider<Review, Category> { it.category },
                Setter<Review, Category> { obj, category -> obj.category = category })
    }

    private fun createTimesField() {
        timesTasted.label = "Times tasted"
        timesTasted.pattern = "[0-9]*"
        timesTasted.isPreventInvalidInput = true
        formLayout.add(timesTasted)

        binder.forField(timesTasted)
                .withConverter(
                        StringToIntegerConverter(0, "Must enter a number."))
                .withValidator(IntegerRangeValidator(
                        "The tasting count must be between 1 and 99.", 1, 99))
                .bind(ValueProvider<Review, Int> { it.count }, Setter<Review, Int> { obj, i -> obj.count = i })
    }

    private fun createNameField() {
        beverageName.label = "Beverage name"
        formLayout.add(beverageName)

        binder.forField(beverageName)
                .withConverter<String>(SerializableFunction<String, String> { it.trim() }, SerializableFunction<String, String> { it.trim() })
                .withValidator(StringLengthValidator(
                        "Beverage name must contain at least 3 printable characters",
                        3, null))
                .bind(ValueProvider<Review, String> { it.name }, Setter<Review, String> { obj, s -> obj.name = s })
    }

    override fun confirmDelete() {
        openConfirmationDialog(
                "Delete beverage \"" + currentItem!!.name + "\"?", "",
                "")
    }

}
