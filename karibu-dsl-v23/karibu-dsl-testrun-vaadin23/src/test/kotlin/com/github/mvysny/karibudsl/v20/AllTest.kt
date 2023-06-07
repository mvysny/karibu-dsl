package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v10.allTests
import com.github.mvysny.kaributools.VaadinVersion
import kotlin.test.expect

class AllTest : DynaTest({
    test("vaadin version") {
        expect(24) { VaadinVersion.get.major }
    }

    allTests()

    group("Components Vaadin 23") {
        vaadinComponents23Tests()
    }

    group("Dialog 23.1") {
        dialog23_1Tests()
    }

    group("TabSheet") {
        tabSheetTest()
    }

    group("SideNav") {
        sideNavTest()
    }
})