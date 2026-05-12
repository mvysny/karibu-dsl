package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._clickItemWithCaption
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class ContextMenuTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().button {
            contextMenu {
                item("save", { _ -> })
                separator()
                item("style") {
                    item("bold", { _ -> })
                    item("italic", { _ -> })
                    separator()
                }
                item("clear", { _ -> })
            }
        }
    }

    @Test fun `click handler fires for top-level item`() {
        var saved = 0
        lateinit var menu: ContextMenu
        UI.getCurrent().button {
            menu = contextMenu {
                item("save", { _ -> saved++ })
            }
        }
        menu._clickItemWithCaption("save")
        expect(1) { saved }
    }

    @Test fun `click handler fires for nested item`() {
        var bold = 0
        lateinit var menu: ContextMenu
        UI.getCurrent().button {
            menu = contextMenu {
                item("style") {
                    item("bold", { _ -> bold++ })
                }
            }
        }
        menu._clickItemWithCaption("bold")
        expect(1) { bold }
    }

    @Nested inner class GridTests {
        @Test fun smoke() {
            UI.getCurrent().grid<String> {
                gridContextMenu {
                    item("save", { _ -> })
                    item("style") {
                        item("bold", { _ -> })
                        separator()
                        item("italic", { _ -> })
                    }
                    separator()
                    item("clear", { _ -> })
                }
            }
        }

        @Test fun `click handler fires with grid item`() {
            var clickedWith: String? = null
            lateinit var menu: GridContextMenu<String>
            UI.getCurrent().grid<String> {
                menu = gridContextMenu {
                    item("save", { row -> clickedWith = row })
                }
            }
            menu._clickItemWithCaption("save", "row-1")
            expect("row-1") { clickedWith }
        }
    }
}
