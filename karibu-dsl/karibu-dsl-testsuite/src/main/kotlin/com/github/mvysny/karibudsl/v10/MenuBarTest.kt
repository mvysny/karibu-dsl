package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._clickItemWithCaption
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.menubar.MenuBar
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class MenuBarTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().menuBar {
            item("save", { _ -> })
            item("style") {
                item("bold", { _ -> })
                separator()
                item("italic", { _ -> })
            }
            item("clear", { _ -> })
        }
    }

    @Test fun `top-level click handler fires`() {
        var clicked = 0
        UI.getCurrent().menuBar {
            item("save", { _ -> clicked++ })
        }
        _get<MenuBar>()._clickItemWithCaption("save")
        expect(1) { clicked }
    }

    @Test fun `nested click handler fires`() {
        var bold = 0
        UI.getCurrent().menuBar {
            item("style") {
                item("bold", { _ -> bold++ })
                item("italic", { _ -> })
            }
        }
        _get<MenuBar>()._clickItemWithCaption("bold")
        expect(1) { bold }
    }
}
