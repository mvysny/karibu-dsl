package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.data.binder.BeanValidationBinder

class ReviewsListTest : DynaTest({

    beforeEach { MockVaadin.setup(Routes().autoDiscoverViews("com.vaadin.starter")) }
    afterEach { MockVaadin.tearDown() }

    test("'new review' smoke test") {
        UI.getCurrent().navigate("")
        // this doesn't work, read more here: https://github.com/mvysny/karibu-testing/tree/master/karibu-testing-v10#polymer-templates
        //  _get<Button> { caption = "New review" } ._click()
        _get<ReviewsList>().addReview._click()

        // the dialog should have been opened
        _get<ReviewEditorDialog>()

        // this is just a smoke test, so let's close the dialog
        _get<Button> { caption = "Cancel" } ._click()

        _expectNone<ReviewEditorDialog>()
    }

    test("'new review' validation test") {
        UI.getCurrent().navigate("")
        // this doesn't work, read more here: https://github.com/mvysny/karibu-testing/tree/master/karibu-testing-v10#polymer-templates
        //  _get<Button> { caption = "New review" } ._click()
        _get<ReviewsList>().addReview._click()

        // the dialog should have been opened
        _get<ReviewEditorDialog>()

        // should mark everything red and invalid
        _get<Button> { caption = "Save" } ._click()

        // the dialog should have stayed opened
        _get<ReviewEditorDialog>()
    }
})
