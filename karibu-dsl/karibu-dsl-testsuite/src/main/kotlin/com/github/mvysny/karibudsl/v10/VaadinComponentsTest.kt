package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.vaadinComponentsTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().apply {
            button("Foo") {}
            iconButton(VaadinIcon.LEVEL_LEFT.create()) {
                onLeftClick {  }
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

    test("scroller") {
        val scroller: Scroller = UI.getCurrent().scroller {  }
        expect(null) { scroller.content }
        val span: Span = scroller.content { span("Foo") }
        expect(span) { scroller.content }
        scroller._expectOne<Span>()

        // test the API - compiling the following shouldn't fail
        scroller.content { span("Bar") }
    }

    group("button") {
        test("text") {
            val button = UI.getCurrent().button("Hello world")
            expect("Hello world") { button.text }
        }
        test("id") {
            val button = UI.getCurrent().button("Hello world", id = "helloWorld")
            expect("helloWorld") { button.id_ }
        }
    }

    group("flex") {
        test("setting flexGrow on component sets it to the parent VerticalLayout correctly") {
            val vl = VerticalLayout().apply {
                button("foo") {
                    flexGrow = 1.0
                }
            }
            expect(1.0) { vl.getFlexGrow(vl.getComponentAt(0)) }
        }
        test("setting flexGrow on component sets it to the parent HorizontalLayout correctly") {
            val vl = HorizontalLayout().apply {
                button("foo") {
                    flexGrow = 1.0
                }
            }
            expect(1.0) { vl.getFlexGrow(vl.getComponentAt(0)) }
        }
        test("setting flexGrow on component sets it to the parent FlexLayout correctly") {
            val vl = FlexLayout().apply {
                button("foo") {
                    flexGrow = 1.0
                }
            }
            expect(1.0) { vl.getFlexGrow(vl.getComponentAt(0)) }
        }
    }

    test("text") {
        val t: Text = UI.getCurrent().text("foo")
        expect("foo") { t.text }
        expect(UI.getCurrent()) { t.parent.get() }
    }
}
