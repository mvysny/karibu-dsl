package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.button.Button
import kotlin.test.expect

class BasicTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("serverClick()") {
        val b = Button()
        var clicked = 0
        b.onLeftClick { clicked++ }
        b.serverClick()
        expect(1) { clicked }
        b.cloneBySerialization()
    }

    test("title") {
        val b = Button()
        expect(null) { b.tooltip }
        b.tooltip = ""
        expect<String?>("") { b.tooltip } // https://youtrack.jetbrains.com/issue/KT-32501
        b.tooltip = "foo"
        expect<String?>("foo") { b.tooltip } // https://youtrack.jetbrains.com/issue/KT-32501
        b.tooltip = null
        expect(null) { b.tooltip }
    }
})
