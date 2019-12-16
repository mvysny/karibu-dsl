package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

class AccordionTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().accordion {
            panel("lorem ipsum") {
                content {
                    label("dolor sit amet")
                }
            }
            panel {
                summary { checkBox("More Lorem Ipsum?") }
                content {
                    label("dolor sit amet")
                }
            }
        }
    }
})
