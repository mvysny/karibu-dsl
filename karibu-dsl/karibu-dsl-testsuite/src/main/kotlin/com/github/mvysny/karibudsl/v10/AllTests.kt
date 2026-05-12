package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.backgroundSize
import kotlinx.css.color
import kotlinx.css.fontFamily
import kotlinx.css.paddingLeft
import kotlinx.css.borderTopWidth
import kotlinx.css.px
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.expect

abstract class AllTests {
    @BeforeEach fun setLocale() {
        // make sure that Validator produces messages in English
        Locale.setDefault(Locale.ENGLISH)
    }

    @Nested inner class VaadinDslTests : AbstractVaadinDslTests()
    @Nested inner class Accordion : AccordionTest()
    @Nested inner class AppLayout : AppLayoutTest()
    @Nested inner class BinderUtils : BinderUtilsTest()
    @Nested inner class ContextMenu : ContextMenuTest()
    @Nested inner class CustomField : CustomFieldTest()
    @Nested inner class Details : DetailsTest()
    @Nested inner class Element : ElementTest()
    @Nested inner class FormLayouts : FormLayoutsTest()
    @Nested inner class Grid : GridTest()
    @Nested inner class Html : HtmlTest()
    @Nested inner class Icon : IconTest()
    @Nested inner class KaribuTools : KaribuToolsTests()
    @Nested inner class KComposite : KcompositeTest()
    @Nested inner class KLumo : KLumoTest()
    @Nested inner class Layouts : LayoutsTest()
    @Nested inner class LoginForm : LoginFormTest()
    @Nested inner class MenuBar : MenuBarTest()
    @Nested inner class PopupButton : PopupButtonTest()
    @Nested inner class Router : RouterTest()
    @Nested inner class Upload : UploadTest()
    @Nested inner class VaadinComponents : VaadinComponentsTest()
    @Nested inner class TabsTests : AbstractTabsTests()
    @Nested inner class Messages : MessagesTest()
    @Nested inner class Badges : BadgesTest()
    @Nested inner class SplitLayout : SplitLayoutTests()
    @Nested inner class TextFieldTests : AbstractTextFieldTests()
    @Nested inner class Utils : UtilsTest()
    @Nested inner class css {
        @BeforeEach fun fakeVaadin() {
            MockVaadin.setup()
        }
        @AfterEach fun tearDownVaadin() {
            MockVaadin.tearDown()
        }
        @Test fun smoke() {
            val div = UI.getCurrent().div {
                css {
                    backgroundColor = Color.turquoise
                    color = Color.black
                }
            }
            expect("turquoise") { div.style["background-color"] }
            expect("black") { div.style["color"] }
        }
        @Test fun `camelCase properties are hyphenized`() {
            // Pins the kotlinx-css name -> CSS property mapping for the camelCase
            // properties users hit most often. Catches regressions if kotlinx-css
            // ever changes its hyphenize() rules.
            val div = UI.getCurrent().div {
                css {
                    fontFamily = "Inter, sans-serif"
                    paddingLeft = 8.px
                    borderTopWidth = 2.px
                }
            }
            expect("Inter, sans-serif") { div.style["font-family"] }
            expect("8px") { div.style["padding-left"] }
            expect("2px") { div.style["border-top-width"] }
        }
    }
    @Nested inner class KLitRenderer : KLitRendererTests()
}
