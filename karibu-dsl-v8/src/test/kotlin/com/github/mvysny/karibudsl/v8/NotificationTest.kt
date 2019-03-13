package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.github.mvysny.kaributesting.v8.expectNotifications
import com.vaadin.ui.Notification
import kotlin.test.expect

class NotificationTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("simple") {
        Notification("foo").show()
        expectNotifications("foo" to null)
    }

    test("additional styles") {
        val n = Notification("foo")
        n.isBar = true
        n.isClosable = true
        n.isDark = true
        n.isFailure = true
        n.isSmall = true
        n.isSuccess = true
        expect("humanized bar closable dark failure small success") { n.styleName }
    }
})
