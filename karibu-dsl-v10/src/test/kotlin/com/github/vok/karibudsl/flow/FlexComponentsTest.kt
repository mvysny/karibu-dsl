package com.github.vok.karibudsl.flow

import com.github.karibu.testing.v10.MockVaadin
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.vaadin.flow.component.button.Button
import kotlin.test.expect

class FlexComponentsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

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
})
