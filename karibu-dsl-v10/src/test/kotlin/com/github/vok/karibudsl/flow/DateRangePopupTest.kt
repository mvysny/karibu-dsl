package com.github.vok.karibudsl.flow

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._click
import com.github.karibu.testing._expectNone
import com.github.karibu.testing._get
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import java.time.LocalDate
import kotlin.test.*

class DateRangePopupTest : DynaTest({
    beforeEach { MockVaadin.setup(setOf()) }
    lateinit var component: DateRangePopup
    beforeEach { component = DateRangePopup() }

    test("Initial value is null") {
        expect(null) { component.value }
    }

    test("setting the value preserves the value") {
        component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
        expect(DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))) { component.value!! }
    }

    group("value change listener tests") {
        test("Setting to the same value does nothing") {
            component.addValueChangeListener {
                fail("should not be fired")
            }
            component.value = null
        }

        test("Setting the value programatically triggers value change listeners") {
            lateinit var newValue: DateInterval
            component.addValueChangeListener {
                expect(false) { it.isFromClient }
                expect(null) { it.oldValue }
                newValue = it.value
            }
            component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
            expect(DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))) { newValue }
        }

        test("value change won't trigger unregistered change listeners") {
            component.addValueChangeListener {
                fail("should not be fired")
            } .remove()
            component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
        }
    }

    group("popup tests") {
        beforeEach {
            // open popup
            (component.content as Button)._click()
            _get<Dialog>() // expect the dialog to pop up
        }

        test("Clear does nothing when the value is already null") {
            component.addValueChangeListener {
                fail("No listener must be fired")
            }
            _get<Button> { caption = "Clear" } ._click()
            expect(null) { component.value }
            _expectNone<Dialog>()  // the Clear button must close the dialog
        }

        test("setting the value while the dialog is opened propagates the values to date fields") {
            component.value = DateInterval(LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1))
            expect(LocalDate.of(2010, 1, 1)) { _get<DatePicker> { caption = "From:" } .value }
            expect(LocalDate.of(2012, 1, 1)) { _get<DatePicker> { caption = "To:" } .value }
        }

        test("Clear properly sets the value to null") {
            component.value = DateInterval.now()
            var wasCalled = false
            component.addValueChangeListener {
                expect(true) { it.isFromClient }
                expect(null) { it.value }
                wasCalled = true
            }
            _get<Button> { caption = "Clear" } ._click()
            expect(true) { wasCalled }
            expect(null) { component.value }
            _expectNone<Dialog>()  // the Clear button must close the dialog
        }

        test("Set properly sets the value to null if nothing is filled in") {
            component.value = DateInterval.now()
            var wasCalled = false
            component.addValueChangeListener {
                expect(true) { it.isFromClient }
                expect(null) { it.value }
                wasCalled = true
            }
            _get<DatePicker> { caption = "From:" } .value = null
            _get<DatePicker> { caption = "To:" } .value = null
            _get<Button> { caption = "Set" } ._click()
            expect(true) { wasCalled }
            expect(null) { component.value }
            _expectNone<Dialog>()  // the Set button must close the dialog
        }

        test("Set properly sets the value in") {
            var wasCalled = false
            component.addValueChangeListener {
                expect(true) { it.isFromClient }
                expect(DateInterval(LocalDate.of(2010, 11, 1), LocalDate.of(2012, 1, 1))) { it.value }
                wasCalled = true
            }
            _get<DatePicker> { caption = "From:" } .value = LocalDate.of(2010, 11, 1)
            _get<DatePicker> { caption = "To:" } .value = LocalDate.of(2012, 1, 1)
            _get<Button> { caption = "Set" } ._click()
            expect(true) { wasCalled }
            expect(DateInterval(LocalDate.of(2010, 11, 1), LocalDate.of(2012, 1, 1))) { component.value }
            _expectNone<Dialog>()  // the Set button must close the dialog
        }
    }
})
