package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.details.Details
import com.vaadin.flow.component.html.Span
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class DetailsTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().details("Hello") {
            content {
                button("hi!")
            }
        }
        _expectOne<Span> { text = "Hello" }
        _expectOne<Button> { text = "hi!" }
        expect("hi!") { _get<Details>().content.toList().joinToString { it._text!! } }
    }

    @Test fun `summary as component`() {
        UI.getCurrent().details {
            summary {
                button("hi!")
            }
        }
        _expectOne<Button> { text = "hi!" }
        expect("hi!") { _get<Details>().summary._text }
    }

    @Test fun `clear content`() {
        val d = UI.getCurrent().details()
        expectList() { d.content.toList() }
        d.content {
            button("hi!")
        }
        expect(1) { d.content.count() }
        d.clearContent()
        expectList() { d.content.toList() }
    }
}
