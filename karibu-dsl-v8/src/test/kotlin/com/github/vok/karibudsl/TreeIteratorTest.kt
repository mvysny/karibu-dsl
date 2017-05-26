package com.github.vok.karibudsl

import com.vaadin.ui.Component
import com.vaadin.ui.VerticalLayout
import org.junit.Before
import org.junit.Test
import kotlin.test.expect

class TreeIteratorTest {

    @Before
    fun setupMockVaadin() = MockVaadin.setup()

    @Test
    fun walkTest() {
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
