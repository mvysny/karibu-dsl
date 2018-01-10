package com.github.karibu.testing

import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import org.junit.Before
import org.junit.Test
import kotlin.streams.asSequence
import kotlin.test.expect

class LocatorTest {
    @Before
    fun mockVaadin() {
        MockVaadin.setup()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getFailsOnNoComponents() {
        Button()._get(TextField::class.java)
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
        val button = Button().apply { id_ = "foo" }
        expect(listOf(button)) { VerticalLayout(button, Button())._find(Button::class.java) { id = "foo" } }
    }

    @Test
    fun simpleUITest() {
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
}
