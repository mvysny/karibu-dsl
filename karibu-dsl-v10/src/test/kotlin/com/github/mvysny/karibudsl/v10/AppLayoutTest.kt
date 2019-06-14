package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.label
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon

class AppLayoutTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().appLayout {
            navbar {
                drawerToggle()
                h3("My App")
            }
            drawer {
                routerLink(VaadinIcon.ARCHIVE, "Foo")
                routerLink(VaadinIcon.ACADEMY_CAP, "Bar")
            }
            content {
                span("Hello world!")
            }
        }
    }
})
