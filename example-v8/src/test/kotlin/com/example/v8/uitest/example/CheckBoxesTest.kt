package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.mvysny.kaributesting.v8._expect
import com.vaadin.ui.CheckBox
import com.vaadin.ui.RadioButtonGroup

class CheckBoxesTest : DynaTest({
    usingApp()

    test("smoke") {
        navigateToView<CheckBoxes>()
        _expect<CheckBox>(12)
        _expect<RadioButtonGroup<*>>(6)
    }
})