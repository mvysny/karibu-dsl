package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.mvysny.kaributesting.v8._expect
import com.vaadin.ui.Panel

class CommonElementsViewTest : DynaTest({
    usingApp()

    test("smoke") {
        navigateToView<CommonElementsView>()
        _expect<Panel> { caption = "Loading Indicator" }
    }
})