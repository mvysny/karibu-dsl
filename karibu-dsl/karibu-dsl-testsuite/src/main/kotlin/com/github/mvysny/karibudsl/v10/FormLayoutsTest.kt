package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.formlayout.FormLayout
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.formLayoutsTest() {
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
    test("class names") {
        var layout: KFormLayout = UI.getCurrent().formLayout()
        expect(null) { layout.className }
        layout = UI.getCurrent().formLayout("foo bar foo")
        expect("foo bar") { layout.className }
    }
    test("form items") {
        val layout = FormLayout().apply {
            formItem("Name:") { textField() }
        }
        expect("""└── FormLayout[]
    └── KFormItem[label='Name:']
        ├── NativeLabel[text='Name:', @slot='label']
        └── TextField[value='']""") { layout.toPrettyTree().trim() }
    }
    test("colspan") {
        lateinit var button: Button
        val layout = UI.getCurrent().formLayout {
            button = button("foo") {
                colspan = 5
            }
        }
        expect(5) { layout.getColspan(button) }
    }
}
