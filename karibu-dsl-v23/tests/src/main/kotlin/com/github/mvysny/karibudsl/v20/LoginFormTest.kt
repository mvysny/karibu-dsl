package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v23.customFormArea
import com.github.mvysny.karibudsl.v23.fontIcon
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.karibudsl.v23.svgIcon
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.login.LoginOverlay

@DynaTestDsl
fun DynaNodeGroup.loginFormTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().apply {
            LoginOverlay().apply {
                footer { button("Hi!") }
                customFormArea { button("Foo") }
            }
        }
    }

    test("karibu-testing lookup finds components from footer") {
        UI.getCurrent().apply {
            LoginOverlay().apply {
                footer { button("Hi!") }
            }._get<Button> { text = "Hi!" }
        }
    }

    test("karibu-testing lookup finds components from customFormArea") {
        UI.getCurrent().apply {
            LoginOverlay().apply {
                customFormArea { button("Hi!") }
            }._get<Button> { text = "Hi!" }
        }
    }
}
