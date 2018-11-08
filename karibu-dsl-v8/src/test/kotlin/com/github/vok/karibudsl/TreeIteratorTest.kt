package com.github.vok.karibudsl

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.vaadin.ui.Component
import com.vaadin.ui.VerticalLayout
import kotlin.test.expect

class TreeIteratorTest : DynaTest({

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
})
