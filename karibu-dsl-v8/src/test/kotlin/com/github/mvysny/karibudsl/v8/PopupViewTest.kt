package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import kotlin.test.expect
import kotlin.test.fail

class PopupViewTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("initially the popup view contains an empty label") {
        val pv = UI.getCurrent().popupView()
        expect("") { (pv.popupComponent as Label).value }
    }

    group("lazy") {
        test("not called initially") {
            val pv = UI.getCurrent().popupView {
                lazy { fail("Shouldn't be called") }
            }
            pv.isPopupVisible = false
        }
        test("set to PopupView when popped up visible") {
            val pv = UI.getCurrent().popupView {
                lazy {
                    button("Okay!")
                }
            }
            pv.isPopupVisible = true
            expect("Okay!") { (pv.popupComponent as Button).caption }
        }
    }
})