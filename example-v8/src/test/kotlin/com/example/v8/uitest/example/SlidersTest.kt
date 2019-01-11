package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.mvysny.kaributesting.v8._expect
import com.vaadin.ui.ProgressBar

class SlidersTest : DynaTest({
    usingApp()

    test("smoke") {
        navigateToView<Sliders>()
        _expect<ProgressBar>(3)
    }
})