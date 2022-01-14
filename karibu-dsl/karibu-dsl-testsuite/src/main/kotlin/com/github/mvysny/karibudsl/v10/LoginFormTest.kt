package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v10._login
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.loginFormTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        val loginI18n: LoginI18n = loginI18n {
            header.title = "VoK Security Demo"
            additionalInformation = "Log in as user/user or admin/admin"
        }
        UI.getCurrent().loginForm(loginI18n) {
            addLoginListener { isError = true }
        }
        _expectOne<LoginForm>()

        _get<LoginForm>()._login("foo", "bar")
        expect(true) { _get<LoginForm>().isError }
    }
}
