package com.github.vok.karibudsl.example

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._get
import com.github.mvysny.dynatest.DynaTest
import com.github.vok.karibudsl.autoDiscoverViews
import com.github.vok.karibudsl.navigateToView
import com.vaadin.ui.Label
import com.vaadin.ui.themes.ValoTheme
import kotlin.test.expect

class WelcomeViewTest : DynaTest({

    beforeGroup {
        // auto-discover views and register them to autoViewProvider
        autoDiscoverViews("com.github")
    }

    beforeEach { MockVaadin.setup({ MyUI() }) }

    test("verify the welcome view caption") {
        navigateToView(WelcomeView::class.java)
        expect("Welcome To The Karibu-DSL Demo") { _get<Label> { styles = ValoTheme.LABEL_H1 }.value }
    }
})
