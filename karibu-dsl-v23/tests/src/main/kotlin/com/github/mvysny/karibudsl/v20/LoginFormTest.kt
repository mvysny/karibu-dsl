package com.github.mvysny.karibudsl.v20

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v23.customFormArea
import com.github.mvysny.karibudsl.v23.fontIcon
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.karibudsl.v23.svgIcon
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.login.LoginOverlay
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class LoginFormTest {
    init {
        Assumptions.assumeTrue(VaadinVersion.get.isAtLeast(24, 2))
    }
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().apply {
            LoginOverlay().apply {
                footer { button("Hi!") }
                customFormArea { button("Foo") }
            }
        }
    }

    @Test fun `karibu-testing lookup finds components from footer`() {
        UI.getCurrent().apply {
            LoginOverlay().apply {
                footer { button("Hi!") }
            }._get<Button> { text = "Hi!" }
        }
    }

    @Test fun `karibu-testing lookup finds components from customFormArea`() {
        UI.getCurrent().apply {
            LoginOverlay().apply {
                customFormArea { button("Hi!") }
            }._get<Button> { text = "Hi!" }
        }
    }
}
