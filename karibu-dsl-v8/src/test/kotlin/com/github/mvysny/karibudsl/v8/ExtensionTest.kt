package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.github.mvysny.kaributesting.v8.expectList
import com.vaadin.ui.UI
import kotlin.test.expect

class ExtensionTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    group("ExtensionPropertyDelegate") {
        test("setting property to null doesn't introduce the extension") {
            val tf = UI.getCurrent().textField()
            tf.type = null
            expectList() { tf.extensions.toList() }
            tf.type = "text"
            expectList() { tf.extensions.toList() }
        }

        test("by default the type is 'text'") {
            val tf = UI.getCurrent().textField()
            expect("text") { tf.type }
            tf.type = null
            expect("text") { tf.type }
        }

        test("setting to multiple values doesn't pile up extension instances") {
            val tf = UI.getCurrent().textField()
            tf.type = "email"
            tf.type = "submit"
            expect(1) { tf.extensions.size }
            expect("submit") { tf.type!! }
        }

        test("setting the value to null removes the extension") {
            val tf = UI.getCurrent().textField()
            tf.type = "email"
            expect(1) { tf.extensions.size }
            tf.type = null
            expectList() { tf.extensions.toList() }
        }

        test("setting the value to 'text' removes the extension") {
            val tf = UI.getCurrent().textField()
            tf.type = "email"
            expect(1) { tf.extensions.size }
            tf.type = "text"
            expectList() { tf.extensions.toList() }
        }
    }
})
