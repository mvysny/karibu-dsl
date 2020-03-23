package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import kotlin.test.expect

class VaadinComponentsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().apply {
            button("Foo") {
                setPrimary()
            }
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
        }
    }

    test("scroller") {
        val scroller: Scroller = UI.getCurrent().scroller {  }
        expect(null) { scroller.content }
        val span = scroller.content { span("Foo") }
        expect(span) { scroller.content }
        scroller._expectOne<Span>()
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
})
