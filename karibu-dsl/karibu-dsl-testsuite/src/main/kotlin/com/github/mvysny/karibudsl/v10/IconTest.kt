package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expect
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class IconTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().icon(VaadinIcon.ABACUS)
        UI.getCurrent().icon(VaadinIcon.ABACUS) {}
        _expect<Icon>(2)
    }

    @Test fun `icon by collection and name`() {
        val icon: Icon = UI.getCurrent().icon("vaadin", "trash")
        expect("vaadin:trash") { icon.icon }
        _expect<Icon>(1)
    }

    @Test fun `icon by collection and name runs the block`() {
        val icon: Icon = UI.getCurrent().icon("lumo", "plus") { setId("the-icon") }
        expect("lumo:plus") { icon.icon }
        expect("the-icon") { icon.id.get() }
    }
}
