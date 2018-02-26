package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.vaadin.ui.*
import kotlin.test.expect

class LocatorTest : DynaTest({

    beforeEach { MockVaadin.setup() }

    group("_get") {
        test("FailsOnNoComponents") {
            expectThrows(IllegalArgumentException::class) {
                Button()._get(Label::class.java)
            }
        }

        test("fails when multiple components match") {
            expectThrows(IllegalArgumentException::class) {
                UI.getCurrent().verticalLayout {
                    verticalLayout { }
                }._get(VerticalLayout::class.java)
            }
        }

        test("selects self") {
            val button = Button()
            expect(button) { button._get(Button::class.java) }
        }

        test("ReturnsNested") {
            val button = Button()
            expect(button) { VerticalLayout(button)._get(Button::class.java) }
        }
    }

    group("_find") {
        test("findMatchingId") {
            val button = Button().apply { id = "foo" }
            expect(listOf(button)) { VerticalLayout(button, Button())._find<Button> { id = "foo" } }
        }
    }

    group("_expectNone") {
        test("succeeds on no matched components") {
            Button()._expectNone(Label::class.java)
        }

        test("fails when multiple components match") {
            expectThrows(IllegalArgumentException::class) {
                UI.getCurrent().verticalLayout {
                    verticalLayout { }
                }._expectNone(VerticalLayout::class.java)
            }
        }

        test("selects self") {
            expectThrows(IllegalArgumentException::class) { Button()._expectNone(Button::class.java) }
        }

        test("ReturnsNested") {
            expectThrows(IllegalArgumentException::class) { VerticalLayout(Button())._expectNone(Button::class.java) }
        }
    }

    test("simpleUITest") {
        lateinit var layout: VerticalLayout
        layout = UI.getCurrent().verticalLayout {
            val name = textField {
                caption = "Type your name here:"
            }
            button("Click Me") {
                addClickListener {
                    println("Thanks ${name.value}, it works!")
                    layout.label("Thanks ${name.value}, it works!")
                }
            }
        }

        _get<TextField> { caption = "Type your name here:" }.value = "Baron Vladimir Harkonnen"
        _get<Button> { caption = "Click Me" }._click()
        expect("Thanks Baron Vladimir Harkonnen, it works!") { _get<Label>().value }
        expect("Thanks Baron Vladimir Harkonnen, it works!") { (layout.last() as Label).value }
        expect(3) { layout.componentCount }
    }
})
