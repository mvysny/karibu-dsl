package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import kotlin.test.expect

fun DynaNodeGroup.vaadinUtilsTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("text") {
        val t: Text = UI.getCurrent().text("foo")
        expect("foo") { t.text }
        expect(UI.getCurrent()) { t.parent.get() }
    }
}
