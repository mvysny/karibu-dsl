package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import kotlin.test.expect

fun DynaNodeGroup.treeIteratorTest() {

    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("walk") {
        val expected = mutableSetOf<Component>()
        val root = VerticalLayout().apply {
            expected.add(this)
            button("Foo") {
                expected.add(this)
            }
            horizontalLayout {
                expected.add(this)
                label {
                    expected.add(this)
                }
            }
            verticalLayout {
                expected.add(this)
            }
        }
        expect(expected) { root.walk().toSet() }
        expect(root) { root.walk().toList()[0] }
    }
}
