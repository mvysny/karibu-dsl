package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon

@DynaTestDsl
fun DynaNodeGroup.appLayoutTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        val a = UI.getCurrent().appLayout {
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
        a.cloneBySerialization()
    }
}
