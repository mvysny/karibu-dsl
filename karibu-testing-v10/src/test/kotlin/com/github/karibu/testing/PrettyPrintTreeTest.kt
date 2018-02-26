package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
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

    test("toPrettyString()") {
        expect("Text[text='foo']") { Text("foo").toPrettyString() }
        expect("Div[INVIS]") { Div().apply { isVisible = false } .toPrettyString() }
        expect("TextField[#25, value='']") { TextField().apply { id_ = "25" } .toPrettyString() }
        expect("Button[text='click me']") { Button("click me").toPrettyString() }
        expect("TextArea[label='label', value='some text']") { TextArea("label").apply { value = "some text" } .toPrettyString() }
        expect("Grid[]") { Grid<Any>().toPrettyString() }
        expect("Column[header='My Header']") { Grid<Any>().run { addColumn { it } .apply { header2 = "My Header" } }.toPrettyString() }
    }
})
