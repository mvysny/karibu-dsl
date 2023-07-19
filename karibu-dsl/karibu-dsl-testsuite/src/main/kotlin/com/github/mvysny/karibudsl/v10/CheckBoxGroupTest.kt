package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.checkBoxGroupTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().checkBoxGroup<String>()
        UI.getCurrent().checkBoxGroup<String>("Foo")
        UI.getCurrent().checkBoxGroup<String> {}
        UI.getCurrent().checkBoxGroup<String>("Foo") {}
    }

    test("label") {
        expect("") { UI.getCurrent().checkBoxGroup<String>().label }
        expect("") { UI.getCurrent().checkBoxGroup<String>("").label }
        expect("foo") { UI.getCurrent().checkBoxGroup<String>("foo").label }
    }
}
