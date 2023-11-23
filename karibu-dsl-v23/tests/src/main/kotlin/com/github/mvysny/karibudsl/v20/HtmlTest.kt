package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.karibudsl.v23.rangeInput
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

@DynaTestDsl
fun DynaNodeGroup.htmlTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().apply {
            rangeInput()
        }
    }
}
