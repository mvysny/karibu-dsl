package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button

class ReviewsListTest : DynaTest({

    usingApp()

    test("'new review' smoke test") {
        UI.getCurrent().navigate("")
        _get<Button> { text = "New review (Alt+N)" } ._click()

        // the dialog should have been opened
        _get<EditorDialogFrame<*>>()

        // this is just a smoke test, so let's close the dialog
        _get<Button> { text = "Cancel" } ._click()

        _expectNone<EditorDialogFrame<*>>()
    }
})
