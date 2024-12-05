package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expect
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
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

    @Nested inner class textfield {
        @Test fun smoke() {
            UI.getCurrent().textField()
            UI.getCurrent().textField{}
            UI.getCurrent().textField("Foo")
            UI.getCurrent().textField("Foo"){}
            _expect<TextField>(4)
        }
        @Test fun dsl() {
            UI.getCurrent().textField("Filter") {
                addThemeVariants(TextFieldVariant.LUMO_SMALL)
                placeholder = "Enter the search query"
            }
        }
    }
}
