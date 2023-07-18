package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl

@DynaTestDsl
fun DynaNodeGroup.allTests24() {
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
}
