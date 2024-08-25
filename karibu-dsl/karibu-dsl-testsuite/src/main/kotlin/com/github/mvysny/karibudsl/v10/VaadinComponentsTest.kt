package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class VaadinComponentsTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun tearDown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().apply {
            button("Foo") {}
            iconButton(VaadinIcon.LEVEL_LEFT.create()) {
                onClick {  }
            }
            checkBox()
            comboBox<String>()
            select<String>()
            datePicker()
            dialog()
            icon(VaadinIcon.TRASH)
            passwordField()
            splitLayout()
            textField()
            emailField()
            numberField()
            textArea()
            tabs {
                tab()
            }
            checkBoxGroup<Int>()
            timePicker()
            integerField()
            bigDecimalField()
            dateTimePicker()
            listBox<String>()
            multiSelectListBox<String>()
            progressBar {}
            progressBar(indeterminate = true)
            radioButtonGroup<String>()
        }
    }

    @Test fun scroller() {
        val scroller: Scroller = UI.getCurrent().scroller {  }
        expect(null) { scroller.content }
        val span: Span = scroller.content { span("Foo") }
        expect(span) { scroller.content }
        scroller._expectOne<Span>()

        // test the API - compiling the following shouldn't fail
        scroller.content { span("Bar") }
    }

    @Nested inner class ButtonTests {
        @Test fun text() {
            val button = UI.getCurrent().button("Hello world")
            expect("Hello world") { button.text }
        }
        @Test fun id() {
            val button = UI.getCurrent().button("Hello world", id = "helloWorld")
            expect("helloWorld") { button.id_ }
        }
    }

    @Nested inner class Flex {
        @Test fun `setting flexGrow on component sets it to the parent VerticalLayout correctly`() {
            val vl = VerticalLayout().apply {
                button("foo") {
                    flexGrow = 1.0
                }
            }
            expect(1.0) { vl.getFlexGrow(vl.getComponentAt(0)) }
        }
        @Test fun `setting flexGrow on component sets it to the parent HorizontalLayout correctly`() {
            val vl = HorizontalLayout().apply {
                button("foo") {
                    flexGrow = 1.0
                }
            }
            expect(1.0) { vl.getFlexGrow(vl.getComponentAt(0)) }
        }
        @Test fun `setting flexGrow on component sets it to the parent FlexLayout correctly`() {
            val vl = FlexLayout().apply {
                button("foo") {
                    flexGrow = 1.0
                }
            }
            expect(1.0) { vl.getFlexGrow(vl.getComponentAt(0)) }
        }
    }

    @Test fun text() {
        val t: Text = UI.getCurrent().text("foo")
        expect("foo") { t.text }
        expect(UI.getCurrent()) { t.parent.get() }
    }

    @Nested inner class AvatarTests() {
        @Test fun smoke() {
            UI.getCurrent().avatar()
            UI.getCurrent().avatar("Hello")
            UI.getCurrent().avatar("Hello", "http://foo.com")
            _expect<Avatar>(3)
        }
    }

    @Test fun onClick() {
        var i = 0
        UI.getCurrent().button("foo") {
            onClick { i++ }
        } ._click()
        expect(1) { i }
    }
}
