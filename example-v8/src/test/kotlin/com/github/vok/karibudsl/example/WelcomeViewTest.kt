package com.github.vok.karibudsl.example

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._get
import com.github.karibu.testing.autoDiscoverViews
import com.github.vok.karibudsl.autoViewProvider
import com.github.vok.karibudsl.navigateToView
import com.vaadin.navigator.Navigator
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.themes.ValoTheme
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.expect

class WelcomeViewTest {

    companion object {
        @BeforeClass @JvmStatic
        fun registerViews() {
            // auto-discover views and register them to autoViewProvider
            autoDiscoverViews("com.github")
        }
    }

    @Before
    fun mockVaadin() {
        MockVaadin.setup()
        val ui = UI.getCurrent()
        ui.navigator = Navigator(ui, ui)
        ui.navigator.addProvider(autoViewProvider)
    }

    @Test
    fun testWelcomeViewCaption() {
        navigateToView(WelcomeView::class.java)
        expect("Welcome To The Karibu-DSL Demo") { UI.getCurrent()._get(Label::class.java, styles = ValoTheme.LABEL_H1).value }
    }
}
