package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.formlayout.FormLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class FormLayoutsTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun responsiveSteps() {
        val layout = FormLayout().apply {
            responsiveSteps { "0px"(1); "30em"(2, top) }
        }
        expect("""[{"minWidth":"0px","columns":1},{"minWidth":"30em","columns":2,"labelsPosition":"top"}]""") {
            layout.element.getProperty("responsiveSteps")
        }
    }
    @Test fun classNames() {
        var layout: KFormLayout = UI.getCurrent().formLayout()
        expect(null) { layout.className }
        layout = UI.getCurrent().formLayout("foo bar foo")
        expect("foo bar") { layout.className }
    }
    @Test fun formItems() {
        val layout = FormLayout().apply {
            formItem("Name:") { textField() }
        }
        expect("""└── FormLayout[]
    └── KFormItem[label='Name:']
        ├── NativeLabel[text='Name:', @slot='label']
        └── TextField[value='']""") { layout.toPrettyTree().trim() }
    }
    @Test fun colspan() {
        lateinit var button: Button
        val layout = UI.getCurrent().formLayout {
            button = button("foo") {
                colspan = 5
            }
        }
        expect(5) { layout.getColspan(button) }
    }
}
