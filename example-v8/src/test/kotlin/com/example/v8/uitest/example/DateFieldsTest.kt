package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.mvysny.kaributesting.v8._expect
import com.vaadin.ui.DateField

class DateFieldsTest : DynaTest({
    usingApp()

    test("smoke") {
        navigateToView<DateFields>()
        _expect<DateField>(18)
    }
})