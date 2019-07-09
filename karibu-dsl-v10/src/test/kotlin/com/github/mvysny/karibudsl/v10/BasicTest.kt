package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
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
    }

    test("title") {
        val b = Button()
        expect(null) { b.title }
        b.title = ""
        expect<String?>("") { b.title } // https://youtrack.jetbrains.com/issue/KT-32501
        b.title = "foo"
        expect<String?>("foo") { b.title } // https://youtrack.jetbrains.com/issue/KT-32501
        b.title = null
        expect(null) { b.title }
    }
})
