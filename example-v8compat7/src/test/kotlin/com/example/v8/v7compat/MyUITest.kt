@file:Suppress("DEPRECATION")

package com.example.v8.v7compat

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.github.mvysny.kaributesting.v8._get
import com.vaadin.v7.ui.Table
import kotlin.test.expect

class MyUITest : DynaTest({
    beforeEach { MockVaadin.setup({ MyUI() }) }
    afterEach { MockVaadin.tearDown() }

    test("verify that the table is displayed") {
        val table = _get<Table>()
        expect(4) { table.size() }
    }
})
