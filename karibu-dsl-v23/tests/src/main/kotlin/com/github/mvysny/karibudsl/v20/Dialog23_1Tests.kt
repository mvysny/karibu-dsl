package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.karibudsl.v23.header
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H3

@DynaTestDsl
fun DynaNodeGroup.dialog23_1Tests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("dsl syntax test") {
        Dialog().apply {
            header { h3("Header") }
            footer { button("Save") }
        }
    }

    test("smoke") {
        Dialog().apply {
            header { h3("Header") }
            footer { button("Save") }
        }.open()
        _expectOne<H3> { text = "Header" }
        _expectOne<Button> { text = "Save" }
    }
}
