package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v8.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.autoDiscoverViews
import com.github.mvysny.karibudsl.v8.navigateToView
import com.vaadin.ui.Label
import com.vaadin.ui.themes.ValoTheme
import kotlin.test.expect

fun DynaNodeGroup.usingApp() {
    beforeGroup {
        // auto-discover views and register them to autoViewProvider
        autoDiscoverViews("com.example")
    }

    beforeEach { MockVaadin.setup({ MyUI() }) }
    afterEach { MockVaadin.tearDown() }
}

class WelcomeViewTest : DynaTest({
    usingApp()

    test("verify the welcome view caption") {
        navigateToView<WelcomeView>()
        expect("Welcome To The Karibu-DSL Demo") { _get<Label> { styles = ValoTheme.LABEL_H1 }.value }
    }
})
