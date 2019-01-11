package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.mvysny.kaributesting.v8._expect
import com.vaadin.ui.HorizontalSplitPanel
import com.vaadin.ui.VerticalSplitPanel

class SplitPanelsTest : DynaTest({
    usingApp()

    test("smoke") {
        navigateToView<SplitPanels>()
        _expect<HorizontalSplitPanel>(2)
        _expect<VerticalSplitPanel>(2)
    }
})
