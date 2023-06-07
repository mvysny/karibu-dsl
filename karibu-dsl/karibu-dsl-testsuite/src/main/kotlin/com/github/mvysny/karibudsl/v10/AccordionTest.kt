package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

@DynaTestDsl
fun DynaNodeGroup.accordionTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().accordion {
            panel("lorem ipsum") {
                content {
                    nativeLabel("dolor sit amet")
                }
            }
            panel {
                summary { checkBox("More Lorem Ipsum?") }
                content {
                    nativeLabel("dolor sit amet")
                }
            }
        }
    }
}
