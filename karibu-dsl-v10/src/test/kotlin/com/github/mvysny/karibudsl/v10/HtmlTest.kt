package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

class HtmlTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke test") {
        UI.getCurrent()!!.apply {
            div()
            h1()
            h2()
            h3()
            h4()
            h5()
            h6()
            hr()
            p()
            em()
            span()
            anchor()
            image()
            label()
            input()
            nativeButton()
            strong()
        }
    }
})