package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import kotlin.streams.asSequence
import kotlin.test.expect

class LocatorTest : DynaTest({

    beforeEach { MockVaadin.setup() }

    test("fails when no component match") {
        expectThrows(IllegalArgumentException::class) {
            Button()._get(TextField::class.java)
        }
    }

    test("fail when multiple component match") {
        expectThrows(IllegalArgumentException::class) {
            UI.getCurrent().verticalLayout {
                verticalLayout { }
            }._get(VerticalLayout::class.java)
        }
    }

    test("ReturnsSelf") {
        val button = Button()
        expect(button) { button._get(Button::class.java) }
    }

    test("ReturnsNested") {
        val button = Button()
        expect(button) { VerticalLayout(button)._get(Button::class.java) }
    }

    test("findMatchingId") {
        val button = Button().apply { id_ = "foo" }
        expect(listOf(button)) { VerticalLayout(button, Button())._find<Button> { id = "foo" } }
    }

    test("simpleUITest") {
        lateinit var layout: VerticalLayout
        layout = UI.getCurrent().verticalLayout {
            val name = textField("Type your name here:")
            button("Click Me") {
                onLeftClick {
                    println("Thanks ${name.value}, it works!")
                    layout.text("Thanks ${name.value}, it works!")
                }
            }
        }

        _get<TextField> { caption = "Type your name here:" }.value = "Baron Vladimir Harkonnen"
        _get<Button> { caption = "Click Me" }._click()
        expect("Thanks Baron Vladimir Harkonnen, it works!") { _get<Text>().text }
        expect("Thanks Baron Vladimir Harkonnen, it works!") { (layout.children.asSequence().last() as Text).text }
        expect(3) { layout.componentCount }
    }
})
