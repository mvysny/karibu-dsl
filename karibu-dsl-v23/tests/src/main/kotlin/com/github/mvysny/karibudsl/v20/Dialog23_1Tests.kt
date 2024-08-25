package com.github.mvysny.karibudsl.v20

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.karibudsl.v23.header
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H3
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class Dialog23_1Tests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun `dsl syntax test`() {
        Dialog().apply {
            header { h3("Header") }
            footer { button("Save") }
        }
    }

    @Test fun smoke() {
        Dialog().apply {
            header { h3("Header") }
            footer { button("Save") }
        }.open()
        _expectOne<H3> { text = "Header" }
        _expectOne<Button> { text = "Save" }
    }
}
