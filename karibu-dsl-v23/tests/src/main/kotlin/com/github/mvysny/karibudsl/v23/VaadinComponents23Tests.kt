package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.span
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.combobox.MultiSelectComboBox
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.popover.Popover
import com.vaadin.flow.component.virtuallist.VirtualList
import com.vaadin.flow.data.provider.ListDataProvider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class VaadinComponents23Tests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Nested inner class VirtualListTest {
        @Test fun `dsl syntax test`() {
            UI.getCurrent().virtualList<String>()
            UI.getCurrent().virtualList<String> {
                setItems("a", "b", "c")
            }
            UI.getCurrent().virtualList<String>(ListDataProvider(listOf()))
            UI.getCurrent().virtualList<String>(ListDataProvider(listOf())) { }
        }

        @Test fun smoke() {
            UI.getCurrent().virtualList<String>()
            _expectOne<VirtualList<*>>()
        }
    }

    @Nested inner class MultiselectComboBoxTest {
        @Test fun smoke() {
            UI.getCurrent().multiSelectComboBox<String>()
            _expectOne<MultiSelectComboBox<*>>()
        }
        @Test fun `dsl syntax`() {
            UI.getCurrent().multiSelectComboBox<String>()
            UI.getCurrent().multiSelectComboBox<String> {}
            UI.getCurrent().multiSelectComboBox<String> {
                setItems("a", "b", "c")
            }
            UI.getCurrent().multiSelectComboBox<String>("Foo")
            UI.getCurrent().multiSelectComboBox<String>("Foo") {
                pageSize = 60
            }
        }
    }

    @Nested inner class popoverTests {
        @Test fun smoke() {
            UI.getCurrent().button("hello") {
                popover {
                    verticalLayout {
                        span("Hi!")
                    }
                }
            }
            _expectOne<Popover>()
            _expectOne<Span> { text = "Hi!" }

            val popover = _get<Popover>()
            popover.open()
            popover.close()
        }
    }
}
