package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.github.mvysny.kaributesting.v8._get
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.navigator.ViewDisplay
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import kotlin.test.expect

@AutoView
class SimpleView : View, VerticalLayout() {
    val params: MutableMap<Int, String> = mutableMapOf()
    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
        params.putAll(event.parameterList)
    }
}

class NavigatorTest : DynaTest({
    beforeEach {
        MockVaadin.setup()
        autoDiscoverViews("com.github.mvysny.karibudsl.v8")
        UI.getCurrent().apply {
            navigator = Navigator(this, this)
            navigator.addProvider(autoViewProvider)
        }
    }
    afterEach { MockVaadin.tearDown() }

    test("parameters are passed properly") {
        navigateToView<SimpleView>("a", "b", "c")
        expect(mapOf(0 to "a", 1 to "b", 2 to "c")) { _get<SimpleView>().params }
    }

    test("parameters are properly urlencoded") {
        navigateToView<SimpleView>(" ", "http://foo")
        expect(mapOf(0 to " ", 1 to "http://foo")) { _get<SimpleView>().params }
    }
})
