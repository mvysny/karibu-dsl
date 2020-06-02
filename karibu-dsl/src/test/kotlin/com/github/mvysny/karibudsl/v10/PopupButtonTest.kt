package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import kotlin.test.expect

class PopupButtonTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().popupButton("foo") {
            content {
                button("Hello!")
            }
        }
        _get<PopupButton>()
        _get<Button> { caption = "Hello!" }
    }

    test("content keeps its width and height") {
        UI.getCurrent().popupButton("foo") {
            content {
                verticalLayout { width = "200px"; height = "300px" }
            }
        }
        expect("200px") { _get<VerticalLayout>().width }
        expect("300px") { _get<VerticalLayout>().height }
    }
})
