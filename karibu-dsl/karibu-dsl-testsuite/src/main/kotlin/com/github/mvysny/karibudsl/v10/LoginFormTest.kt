package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v10._login
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginOverlay
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class LoginFormTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        val loginI18n: LoginI18n = loginI18n {
            header.title = "VoK Security Demo"
            additionalInformation = "Log in as user/user or admin/admin"
            setErrorMessage("foo", "bar")
        }
        UI.getCurrent().loginForm(loginI18n) {
            addLoginListener {}
        }
        _expectOne<LoginForm>()
    }

    @Test fun loginFormTest() {
        val loginI18n: LoginI18n = loginI18n {}
        UI.getCurrent().loginForm(loginI18n) {
            addLoginListener { isError = true }
        }
        _expectOne<LoginForm>()

        _get<LoginForm>()._login("foo", "bar")
        expect(true) { _get<LoginForm>().isError }
    }

    @Test fun loginOverlayTest() {
        val loginI18n: LoginI18n = loginI18n {}
        openLoginOverlay(loginI18n) {
            addLoginListener { isError = true }
        }
        _expectOne<LoginOverlay>()

        _get<LoginOverlay>()._login("foo", "bar")
        expect(true) { _get<LoginOverlay>().isError }
    }
}
