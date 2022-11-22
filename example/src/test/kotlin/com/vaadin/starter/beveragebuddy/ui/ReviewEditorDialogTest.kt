package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10._click
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.starter.beveragebuddy.ui.reviews.ReviewEditorDialog
import kotlin.test.expect

class ReviewEditorDialogTest : DynaTest({
    usingApp()

    test("smoke") {
        ReviewEditorDialog({}, {}).createNew()
    }

    test("simple validation failure") {
        ReviewEditorDialog({}, {}).createNew()
        _expectOne<EditorDialogFrame<*>>()
        _get<Button> { text = "Save" } ._click()
        _expectOne<EditorDialogFrame<*>>()
        expect("must not be blank") {
            _get<TextField> { label = "Beverage name"} .errorMessage
        }
    }
})