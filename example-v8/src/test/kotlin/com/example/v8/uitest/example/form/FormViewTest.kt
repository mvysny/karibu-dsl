package com.example.v8.uitest.example.form

import com.example.v8.uitest.example.usingApp
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8._expectOne
import com.vaadin.ui.FormLayout

class FormViewTest : DynaTest({
    usingApp()

    test("smoke") {
        FormView.navigateTo()
        _expectOne<FormLayout>()
    }
})