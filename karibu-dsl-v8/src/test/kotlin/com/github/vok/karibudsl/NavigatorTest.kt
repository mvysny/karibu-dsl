package com.github.vok.karibudsl

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._get
import com.github.mvysny.dynatest.DynaTest
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
        autoDiscoverViews("com.github.vok.karibudsl")
        UI.getCurrent().apply {
            navigator = Navigator(this, this)
            navigator.addProvider(autoViewProvider)
        }
    }

    test("parameters are passed properly") {
        navigateToView<SimpleView>("a", "b", "c")
        expect(mapOf(0 to "a", 1 to "b", 2 to "c")) { _get<SimpleView>().params }
    }
})
