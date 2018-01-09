package com.github.karibu.testing

import com.github.vok.karibudsl.button
import com.github.vok.karibudsl.label
import com.github.vok.karibudsl.textField
import com.github.vok.karibudsl.verticalLayout
import com.vaadin.ui.*
import org.junit.Before
import org.junit.Test
import kotlin.test.expect

class LocatorTest {
    @Before
    fun mockVaadin() {
        MockVaadin.setup()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getFailsOnNoComponents() {
        Button()._get(Label::class.java)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getFailsOnMoreComponents() {
        UI.getCurrent().verticalLayout {
            verticalLayout {  }
        }._get(VerticalLayout::class.java)
    }

    @Test
    fun getReturnsSelf() {
        val button = Button()
        expect(button) { button._get(Button::class.java) }
    }

    @Test
    fun getReturnsNested() {
        val button = Button()
        expect(button) { VerticalLayout(button)._get(Button::class.java) }
    }

    @Test
    fun findMatchingId() {
        val button = Button().apply { id = "foo" }
        expect(listOf(button)) { VerticalLayout(button, Button())._find(Button::class.java, id = "foo") }
    }

    @Test
    fun simpleUITest() {
        var layout: VerticalLayout? = null
        layout = UI.getCurrent().verticalLayout {
            val name = textField {
                caption = "Type your name here:"
            }
            button("Click Me", {
                println("Thanks ${name.value}, it works!")
                layout!!.label("Thanks ${name.value}, it works!")
            })
        }

        _get<TextField>(caption = "Type your name here:").value = "Baron Vladimir Harkonnen"
        _get<Button>(caption = "Click Me")._click()
        expect(3) { layout.componentCount }
        expect("Thanks Baron Vladimir Harkonnen, it works!") { (layout.last() as Label).value }
        expect("Thanks Baron Vladimir Harkonnen, it works!") { _get<Label>().value }
    }
}
