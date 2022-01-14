package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon

@DynaTestDsl
fun DynaNodeGroup.iconTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().icon(VaadinIcon.ABACUS)
        UI.getCurrent().icon(VaadinIcon.ABACUS) {}
        UI.getCurrent().ironIcon("foo", "bar")
        UI.getCurrent().ironIcon("foo", "bar") {}
    }
}
