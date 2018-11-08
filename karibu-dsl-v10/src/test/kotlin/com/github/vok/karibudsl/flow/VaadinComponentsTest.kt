package com.github.vok.karibudsl.flow

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import kotlin.test.expect

class VaadinComponentsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

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
