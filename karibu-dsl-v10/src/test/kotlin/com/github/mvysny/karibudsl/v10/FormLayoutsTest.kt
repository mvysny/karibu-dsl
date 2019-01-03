package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.formlayout.FormLayout
import kotlin.test.expect

class FormLayoutsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

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
