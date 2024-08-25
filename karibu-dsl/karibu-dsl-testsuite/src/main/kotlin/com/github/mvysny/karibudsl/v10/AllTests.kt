package com.github.mvysny.karibudsl.v10

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import java.util.*

abstract class AllTests {
    @BeforeEach fun setLocale() {
        // make sure that Validator produces messages in English
        Locale.setDefault(Locale.ENGLISH)
    }

    @Nested inner class Accordion : AccordionTest()
    @Nested inner class AppLayout : AppLayoutTest()
    @Nested inner class BinderUtils : BinderUtilsTest()
    @Nested inner class ContextMenu : ContextMenuTest()
    @Nested inner class Details : DetailsTest()
    @Nested inner class Element : ElementTest()
    @Nested inner class FormLayouts : FormLayoutsTest()
    @Nested inner class Grid : GridTest()
    @Nested inner class Html : HtmlTest()
    @Nested inner class Icon : IconTest()
    @Nested inner class KaribuTools : KaribuToolsTests()
    @Nested inner class KComposite : KcompositeTest()
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
