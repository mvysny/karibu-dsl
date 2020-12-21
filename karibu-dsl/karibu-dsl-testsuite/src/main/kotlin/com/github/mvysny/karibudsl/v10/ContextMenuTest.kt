package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

fun DynaNodeGroup.contextMenuTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().button {
            contextMenu {
                item("save", { _ -> println("saved") })
                separator()
                item("style") {
                    item("bold", { _ -> println("bold") })
                    item("italic", { _ -> println("italic") })
                    separator()
                }
                item("clear", { _ -> println("clear") })
            }
        }
    }

    group("grid") {
        test("smoke") {
            UI.getCurrent().grid<String> {
                gridContextMenu {
                    item("save", { _ -> println("saved") })
                    item("style") {
                        item("bold", { _ -> println("bold") })
                        item("italic", { _ -> println("italic") })
                    }
                    item("clear", { _ -> println("clear") })
                }
            }
        }
    }
}
