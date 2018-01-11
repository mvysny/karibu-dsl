package com.github.vok.karibudsl.example

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._get
import com.github.karibu.testing.autoDiscoverViews
import com.github.vok.karibudsl.navigateToView
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
        MockVaadin.setup({ MyUI() })
    }

    @Test
    fun testWelcomeViewCaption() {
        navigateToView(WelcomeView::class.java)
        expect("Welcome To The Karibu-DSL Demo") { _get<Label> { styles = ValoTheme.LABEL_H1 }.value }
    }
}
