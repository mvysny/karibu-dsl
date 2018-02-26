package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.html.Div
import kotlin.test.expect

class PrettyPrintTreeTest : DynaTest({
    test("Simple dump") {
        val div = Div().apply {
            text("Foo")
        }
        expect("""└── Div[text='Foo']
    └── Text[text='Foo']
""") { div.toPrettyTree() }
    }
})
