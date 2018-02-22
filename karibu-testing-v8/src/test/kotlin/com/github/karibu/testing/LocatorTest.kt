package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.vaadin.ui.*
import kotlin.test.expect

class LocatorTest : DynaTest({

    beforeEach { MockVaadin.setup() }

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

    test("findMatchingId") {
        val button = Button().apply { id = "foo" }
        expect(listOf(button)) { VerticalLayout(button, Button())._find<Button> { id = "foo" } }
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
