package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.expect

abstract class AbstractVaadinDslTests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun tearDown() { MockVaadin.tearDown() }

    @Nested inner class initTests {
        @Test fun smoke() {
            UI.getCurrent().init(Button("Hi"), "hi") {}
            UI.getCurrent().init(Button("Hi"), "hi")
            UI.getCurrent().init(Button("Hi")) {}
            UI.getCurrent().init(Button("Hi"))
        }

        @Test fun initLambdaCalled() {
            var called = false
            UI.getCurrent().init(Button("Hi"), "hi") { called = true }
            expect(true) { called }
        }

        @Test fun componentAddedToParent() {
            val b = Button("Hi")
            UI.getCurrent().init(b, "hi")
            _expectOne<Button> { id = "hi" }
            _expectOne<Button> { text = "Hi" }
            expect(b) { UI.getCurrent().children.toList().single() }
            expect(UI.getCurrent()) { b.parent.get() }
        }
    }

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
