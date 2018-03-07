package com.github.vok.karibudsl.flow

import com.github.mvysny.dynatest.DynaTest
import java.time.LocalDate
import kotlin.test.expect

class DateRangePopupTest : DynaTest({
    lateinit var component: DateRangePopup
    beforeEach { component = DateRangePopup() }

    test("Initial value is null") {
        expect(null) { component.value }
    }

    group("value change listener tests") {
        test("Setting to the same value does nothing") {
            component.addValueChangeListener {
                kotlin.test.fail("should not be fired")
            }
            component.value = null
        }

        test("Setting the value programatically triggers value change listeners") {
            lateinit var newValue: ClosedRange<LocalDate>
            component.addValueChangeListener {
                expect(false) { it.isFromClient }
                expect(null) { it.oldValue }
                newValue = it.value
            }
            component.value = LocalDate.of(2010, 1, 1)..LocalDate.of(2012, 1, 1)
            expect(LocalDate.of(2010, 1, 1)..LocalDate.of(2012, 1, 1)) { newValue }
        }

        test("unregistering the value change listener will not fire it") {
            component.addValueChangeListener {
                kotlin.test.fail("should not be fired")
            } .remove()
            component.value = LocalDate.of(2010, 1, 1)..LocalDate.of(2012, 1, 1)
        }
    }
})
