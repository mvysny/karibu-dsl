package com.github.mvysny.karibudsl.v23

import org.junit.jupiter.api.Nested

abstract class AllTests24() {
    @Nested inner class vaadinComponents23 : VaadinComponents23Tests()
    @Nested inner class `Dialog 23-1` : Dialog23_1Tests()
    @Nested inner class TabSheet : TabSheetTest()
    @Nested inner class SideNav : SideNavTest()
    @Nested inner class Icon : IconTest()
    @Nested inner class LoginForm : LoginFormTest()
    @Nested inner class Html : HtmlTest()
    @Nested inner class MarkdownTests: AbstractMarkdownTests()
    @Nested inner class CardTests : AbstractCardTests()
    @Nested inner class MasterDetailLayoutTests: AbstractMasterDetailLayoutTests()
}
