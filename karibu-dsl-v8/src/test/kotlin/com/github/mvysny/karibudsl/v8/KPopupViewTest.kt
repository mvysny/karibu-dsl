package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.github.mvysny.kaributesting.v8._expect
import com.github.mvysny.kaributesting.v8._expectOne
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.UI
import kotlin.test.fail

class KPopupViewTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("initially the popup view contains no children") {
        val pv = UI.getCurrent().popupView()
        pv._expect<Component>(count = 2)  // KPopupView with one child, PopupView
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
            pv._expect<Component>(count = 2)  // KPopupView with one child, PopupView
            pv.isPopupVisible = true
            _expectOne<Button> { caption = "Okay!" }
        }
        test("lazy{} ran only once") {
            var runCount = 0
            val pv = UI.getCurrent().popupView {
                lazy {
                    if (++runCount > 1) {
                        fail("Ran $runCount times")
                    }
                    button("Okay!")
                }
            }
            pv._expect<Component>(count = 2)  // KPopupView with one child, PopupView
            pv.isPopupVisible = true
            _expectOne<Button> { caption = "Okay!" }
            pv.isPopupVisible = false
            pv._expect<Component>(count = 2)  // KPopupView with one child, PopupView
            pv.isPopupVisible = true
            _expectOne<Button> { caption = "Okay!" }
        }
    }
})
