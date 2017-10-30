package com.github.karibu.testing

import com.github.vok.karibudsl.verticalLayout
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
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
}
