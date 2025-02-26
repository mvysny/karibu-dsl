package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.expect

abstract class AbstractVaadinDslTests {
    @Nested
    inner class provideSingleComponentOrNull {
        @Test
        fun smoke() {
            val vl = provideSingleComponentOrNull {
                verticalLayout {
                    button("Hi!")
                }
            }
            expect(true) { vl is VerticalLayout }
        }

        @Test
        fun returnsNullWhenNothingWasAdded() {
            val vl = provideSingleComponentOrNull {}
            expect(null) { vl }
        }

        @Test
        fun failsWhenTwoComponentsAreAdded() {
            val ex = assertThrows<IllegalStateException> {
                provideSingleComponentOrNull {
                    button("1")
                    button("2")
                }
            }
            expect("Too many components to add - this component can only host one!") { ex.message }
        }
    }

    @Nested
    inner class provideSingleComponent {
        @Test
        fun smoke() {
            val vl = provideSingleComponent {
                verticalLayout {
                    button("Hi!")
                }
            }
            expect(true) { vl is VerticalLayout }
        }

        @Test
        fun failsWhenNothingWasAdded() {
            val ex = assertThrows<IllegalStateException> {
                provideSingleComponent {}
            }
            expect("`block` must add exactly one component") { ex.message!! }
        }

        @Test
        fun failsWhenTwoComponentsAreAdded() {
            val ex = assertThrows<IllegalStateException> {
                provideSingleComponent {
                    button("1")
                    button("2")
                }
            }
            expect("Too many components to add - this component can only host one!") { ex.message }
        }
    }
}
