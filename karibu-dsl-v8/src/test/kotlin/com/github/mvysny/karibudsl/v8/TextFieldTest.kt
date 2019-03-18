package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.github.mvysny.kaributesting.v8._expectOne
import com.github.mvysny.kaributesting.v8._get
import com.vaadin.ui.TextArea
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import kotlin.test.expect

class TextFieldTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke test") {
        UI.getCurrent().apply {
            textField()
            _expectOne<TextField>()

            removeAllComponents()
            textField("foo", "bar")
            _expectOne<TextField>()

            removeAllComponents()
            textArea()
            _expectOne<TextArea>()

            removeAllComponents()
            textArea("bar")
            _expectOne<TextArea>()

            removeAllComponents()
            emailField()
            _expectOne<TextField>()

            removeAllComponents()
            emailField("foo", "bar")
            expect("email") { _get<TextField>().type }
        }
    }

    test("TypeExtension") {
        val tf = UI.getCurrent().textField()
        val e = TypeExtension(tf)
        expect("") { e.type }
        e.type = "text"
        expect("text") { e.type }
        e.type = "email"
        expect("email") { e.type }
        e.remove()
    }
})
