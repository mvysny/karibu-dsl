package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import java.util.*

@DynaTestDsl
fun DynaNodeGroup.allTests() {
    beforeEach {
        // make sure that Validator produces messages in English
        Locale.setDefault(Locale.ENGLISH)
    }

    group("accordion test") {
        accordionTest()
    }
    group("applayout utils") {
        appLayoutTest()
    }
    group("binder utils") {
        binderUtilsTest()
    }
    group("ContextMenu test") {
        contextMenuTest()
    }
    group("DateRangePopup test") {
        dateRangePopupTest()
    }
    group("Details test") {
        detailsTest()
    }
    group("Element test") {
        elementTest()
    }
    group("FormLayout test") {
        formLayoutsTest()
    }
    group("Grid test") {
        gridTest()
    }
    group("Html test") {
        htmlTest()
    }
    group("Icon test") {
        iconTest()
    }
    group("KComposite test") {
        kcompositeTest()
    }
    group("Layouts test") {
        layoutsTest()
    }
    group("LoginForm test") {
        loginFormTest()
    }
    group("MenuBar test") {
        menuBarTest()
    }
    group("NumberRangePopup test") {
        numberRangePopupTest()
    }
    group("PopupButton test") {
        popupButtonTest()
    }
    group("Router test") {
        routerTest()
    }
    group("Upload test") {
        uploadTest()
    }
    group("VaadinComponents test") {
        vaadinComponentsTest()
    }
    group("Messages") {
        messagesTests()
    }
    group("Badge") {
        badgesTest()
    }
}
