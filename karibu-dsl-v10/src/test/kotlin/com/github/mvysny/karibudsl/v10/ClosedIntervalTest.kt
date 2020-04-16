package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import java.time.LocalDate
import kotlin.test.expect

class ClosedIntervalTest : DynaTest({
    test("empty") {
        expect(true) { DateInterval.EMPTY.isEmpty() }
        expect(false) { DateInterval.UNIVERSAL.isEmpty() }
        expect(false) { DateInterval.of(LocalDate.of(2000, 1, 1)).isEmpty() }
    }

    test("isBound") {
        expect(true) { DateInterval.EMPTY.isBound }
        expect(false) { DateInterval.UNIVERSAL.isBound }
        expect(true) { DateInterval.of(LocalDate.of(2000, 1, 1)).isBound }
    }

    test("isSingleItem") {
        expect(false) { DateInterval.EMPTY.isSingleItem }
        expect(false) { DateInterval.UNIVERSAL.isSingleItem }
        expect(true) { DateInterval.of(LocalDate.of(2000, 1, 1)).isSingleItem }
    }

    test("isUniversalSet") {
        expect(false) { DateInterval.EMPTY.isUniversalSet }
        expect(true) { DateInterval.UNIVERSAL.isUniversalSet }
        expect(false) { DateInterval.of(LocalDate.of(2000, 1, 1)).isUniversalSet }
    }

    test("contains") {
        val probe = LocalDate.of(2000, 1, 1)
        expect(false) { DateInterval.EMPTY.contains(probe) }
        expect(false) { DateInterval.EMPTY.contains(DateInterval.EMPTY.endInclusive!!) }
        expect(true) { DateInterval.UNIVERSAL.contains(probe) }
        expect(true) { DateInterval.of(probe).contains(probe) }
        expect(true) { DateInterval(LocalDate.of(1999, 1, 1), null).contains(probe) }
        expect(true) { DateInterval(null, LocalDate.of(2001, 1, 1)).contains(probe) }
    }
})
