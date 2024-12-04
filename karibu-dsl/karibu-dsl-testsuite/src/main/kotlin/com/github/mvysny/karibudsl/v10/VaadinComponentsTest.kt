package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.avatar.AvatarGroup
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.listbox.ListBox
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.EmailField
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
            openDialog {}
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

    @Nested inner class CheckBoxTests {
        @Test fun smoke() {
            UI.getCurrent().checkBox()
            UI.getCurrent().checkBox("Foo")
            UI.getCurrent().checkBox("Foo") {
                isEnabled = false
            }
            _expect<Checkbox>(3)
        }
    }

    @Nested inner class CheckBoxGroupTests {
        @Test fun smoke() {
            UI.getCurrent().checkBoxGroup<String>()
            UI.getCurrent().checkBoxGroup<String>("Foo")
            UI.getCurrent().checkBoxGroup<String>("Foo") {
                setItems("a", "b", "c")
            }
            _expect<CheckboxGroup<*>>(3)
        }
    }

    @Nested inner class DatePickerTests {
        @Test fun smoke() {
            UI.getCurrent().datePicker()
            UI.getCurrent().datePicker("Foo")
            UI.getCurrent().datePicker("Foo") {
                open()
            }
            _expect<DatePicker>(3)
        }
    }

    @Nested inner class DateTimePickerTests {
        @Test fun smoke() {
            UI.getCurrent().dateTimePicker()
            UI.getCurrent().dateTimePicker("Foo")
            UI.getCurrent().dateTimePicker("Foo") {
                setAriaLabel("foo")
            }
            _expect<DateTimePicker>(3)
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

    @Nested inner class AvatarGroupTests {
        @Test fun smoke() {
            UI.getCurrent().avatarGroup {
                item("John Foo")
                item("Random Dude")
            }
            _expectOne<AvatarGroup>()
        }
    }

    @Nested inner class dialog {
        @Test fun smoke() {
            openDialog()
            _expectOne<Dialog>()
        }
    }

    @Nested inner class emailField {
        @Test fun smoke() {
            UI.getCurrent().emailField()
            UI.getCurrent().emailField {}
            UI.getCurrent().emailField("Foo")
            UI.getCurrent().emailField("Foo") {}
            _expect<EmailField>(4)
        }
    }

    @Nested inner class listBox {
        @Test fun smoke() {
            UI.getCurrent().listBox<String>()
            UI.getCurrent().listBox<String>{}
            _expect<ListBox<*>>(2)
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
