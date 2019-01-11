package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.mvysny.kaributesting.v8._expectOne
import com.vaadin.ui.Tree

class TreesTest : DynaTest({
    usingApp()

    test("smoke") {
        navigateToView<Trees>()
        _expectOne<Tree<*>>()
    }
})