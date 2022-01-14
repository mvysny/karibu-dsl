package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

@DynaTestDsl
fun DynaNodeGroup.menuBarTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().menuBar {
            item("save", { _ -> println("saved") })
            item("style") {
                item("bold", { _ -> println("bold") })
                item("italic", { _ -> println("italic") })
            }
            item("clear", { _ -> println("clear") })
        }
    }
}
