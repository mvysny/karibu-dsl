package com.github.mvysny.karibudsl.v10

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import java.util.*

class AllTests {
    @BeforeEach fun setLocale() {
        // make sure that Validator produces messages in English
        Locale.setDefault(Locale.ENGLISH)
    }

    @Nested inner class Accordion : AccordionTest()
    @Nested inner class AppLayout : AppLayoutTest()
    @Nested inner class BinderUtils : BinderUtilsTest()
    @Nested inner class ContextMenu : ContextMenuTest()
    @Nested inner class Details : DetailsTest()
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
    group("PopupButton test") {
        popupButtonTest()
    }
    group("Router test") {
        routerTest()
    }
    group("Upload test") {
        uploadTest()
    }
    @Nested inner class VaadinComponents : VaadinComponentsTest()
    group("Messages") {
        messagesTests()
    }
    @Nested inner class Badges : BadgesTest()
}
