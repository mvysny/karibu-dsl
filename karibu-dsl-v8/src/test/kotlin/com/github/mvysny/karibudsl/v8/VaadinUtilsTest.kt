package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.vaadin.server.ErrorMessage
import com.vaadin.server.UserError
import com.vaadin.ui.Button
import com.vaadin.ui.UI
import kotlin.test.expect

class VaadinUtilsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("error message") {
        expect("foo bar baz") { (UserError("foo bar baz") as ErrorMessage).message }
    }

    group("findAncestor") {
        test("null on no parent") {
            expect(null) { Button().findAncestor { false } }
        }
        test("null on no acceptance") {
            expect(null) { UI.getCurrent().button().findAncestor { false } }
        }
        test("finds UI") {
            expect(UI.getCurrent()) { UI.getCurrent().button().findAncestor { it is UI } }
        }
        test("doesn't find self") {
            expect(UI.getCurrent()) { UI.getCurrent().button().findAncestor { true } }
        }
    }

    group("findAncestorOrSelf") {
        test("null on no parent") {
            expect(null) { Button().findAncestorOrSelf { false } }
        }
        test("null on no acceptance") {
            expect(null) { UI.getCurrent().button().findAncestorOrSelf { false } }
        }
        test("finds self") {
            val button = UI.getCurrent().button()
            expect(button) { button.findAncestorOrSelf { true } }
        }
    }

    test("isNestedIn") {
        expect(false) { Button().isNestedIn(UI.getCurrent()) }
        expect(true) { UI.getCurrent().button().isNestedIn(UI.getCurrent()) }
    }
})
