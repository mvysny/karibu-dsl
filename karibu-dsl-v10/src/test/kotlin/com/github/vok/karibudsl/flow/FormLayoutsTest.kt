package com.github.vok.karibudsl.flow

import com.github.karibu.testing.v10.MockVaadin
import com.github.karibu.testing.v10.toPrettyTree
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.formlayout.FormLayout
import kotlin.streams.toList
import kotlin.test.expect

class FormLayoutsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    test("responsive steps") {
        val layout = FormLayout().apply {
            responsiveSteps { "0px"(1); "30em"(2, top) }
        }
        expect("""[{"minWidth":"0px","columns":1},{"minWidth":"30em","columns":2,"labelsPosition":"top"}]""") {
            layout.element.getProperty("responsiveSteps")
        }
    }
    test("form items") {
        val layout = FormLayout().apply {
            formItem("Name:") { textField() }
        }
        expect("""└── FormLayout[]
    └── KFormItem[]
        ├── Label[text='Name:']
        └── TextField[value='']""") { layout.toPrettyTree().trim() }
    }
})
