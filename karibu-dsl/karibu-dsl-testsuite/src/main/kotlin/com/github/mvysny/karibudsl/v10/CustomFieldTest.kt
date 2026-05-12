package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.html.Span
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

private class DummyCustomField : CustomField<String>() {
    override fun generateModelValue(): String = ""
    override fun setPresentationValue(newPresentationValue: String?) {}
}

abstract class CustomFieldTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun `content adds child component`() {
        val field = DummyCustomField()
        val button = field.content { button("Click") }
        expect(field) { button.parent.get() }
        expect("Click") { button.text }
    }

    @Test fun `content returns the produced component`() {
        val field = DummyCustomField()
        val span: Span = field.content { span("hello") }
        expect("hello") { span.text }
    }

    @Test fun `content can be invoked multiple times`() {
        val field = DummyCustomField()
        val first: Span = field.content { span("a") }
        val second: Button = field.content { button("b") }
        expect(field) { first.parent.get() }
        expect(field) { second.parent.get() }
    }
}
