package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class MenuBarTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().menuBar {
            item("save", { _ -> println("saved") })
            item("style") {
                item("bold", { _ -> println("bold") })
                separator()
                item("italic", { _ -> println("italic") })
            }
            item("clear", { _ -> println("clear") })
        }
    }
}
