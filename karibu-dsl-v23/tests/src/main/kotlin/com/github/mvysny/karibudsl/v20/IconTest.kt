package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.karibudsl.v23.fontIcon
import com.github.mvysny.karibudsl.v23.svgIcon
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

@DynaTestDsl
fun DynaNodeGroup.iconTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().apply {
            svgIcon {}
            svgIcon()
            svgIcon("foo") {}
            fontIcon()
            fontIcon {}
            fontIcon("fa-solid", "fa-user")
            fontIcon("fa-solid", "fa-user") {}
        }
    }
}
