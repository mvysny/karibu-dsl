package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.label
import com.vaadin.flow.component.UI

class AppLayoutTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().appLayout {
            branding { h3("My App") }
            withVaadinMenu {
                item("Item 1")
                item("Item 2")
            }
            content {
                span("Hello world!")
            }
        }
    }
})
