package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expect
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.textfield.TextArea
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class AbstractTextFieldTests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Nested inner class textarea {
        @Test fun smoke() {
            UI.getCurrent().textArea()
            UI.getCurrent().textArea{}
            UI.getCurrent().textArea("Foo")
            UI.getCurrent().textArea("Foo"){}
            _expect<TextArea>(4)
        }
        @Test fun dsl() {
            UI.getCurrent().textArea {
                maxLength = 1000
            }
        }
    }
}
