package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v10.allTests
import com.github.mvysny.kaributesting.v10.VaadinMeta
import kotlin.test.expect

class AllTest : DynaTest({
    test("vaadin version") {
        expect(20) { VaadinMeta.version }
    }

    allTests()
})