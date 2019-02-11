package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

class ContextMenuTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().button {
            contextMenu {
                item("save", { e -> println("saved") })
                item("style") {
                    item("bold", { e -> println("bold") })
                    item("italic", { e -> println("italic") })
                }
                item("clear", { e -> println("clear") })
            }
        }
    }
})
