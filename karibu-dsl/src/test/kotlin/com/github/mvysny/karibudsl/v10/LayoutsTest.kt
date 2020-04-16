package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import kotlin.test.expect

class LayoutsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().flexLayout {
            verticalLayout()
            horizontalLayout()
        }
    }

    group("flexGrow") {
        test("flexGrow works even when the component is not yet attached to parent") {
            Button().flexGrow = 1.0
        }
        test("setting flexGrow works") {
            val button = Button()
            button.flexGrow = 1.0
            expect("1.0") { button.element.style.get("flexGrow") }
        }
        test("getting flexGrow works") {
            val button = Button()
            button.element.style.set("flexGrow", "25")
            expect(25.0) { button.flexGrow }
        }
        test("setting flexGrow to 0 removes the style") {
            val button = Button()
            button.element.style.set("flexGrow", "25")
            button.flexGrow = 0.0
            expect(null) { button.element.style.get("flexGrow") }
        }
        test("by default component has flexGrow of 0") {
            expect(0.0) { Button().flexGrow }
        }
        test("setting negative flexGrow fails") {
            expectThrows(IllegalArgumentException::class) {
                Button().flexGrow = -1.0
            }
        }
    }

    test("flexShrink") {
        val button = Button()
        expect(1.0) { button.flexShrink }
        button.flexShrink = 0.0
        expect(0.0) { button.flexShrink }
        button.flexShrink = 1.0
        expect(1.0) { button.flexShrink }
        expect(null) { button.element.style.get("flexShrink") }
    }

    test("flexBasis") {
        val button = Button()
        expect(null) { button.flexBasis }
        button.flexBasis = "auto"
        expect<String?>("auto") { button.flexBasis }
        button.flexBasis = null
        expect(null) { button.flexBasis }
        expect(null) { button.element.style.get("flexBasis") }
    }

    test("alignSelf") {
        lateinit var button: Button
        UI.getCurrent().verticalLayout {
            button = button()
        }
        expect(FlexComponent.Alignment.AUTO) { button.alignSelf }
        button.alignSelf = FlexComponent.Alignment.END
        expect<FlexComponent.Alignment?>(FlexComponent.Alignment.END) { button.alignSelf }
        button.alignSelf = null
        expect(FlexComponent.Alignment.AUTO) { button.alignSelf }
        expect(null) { button.element.style.get("alignSelf") }
    }
})
