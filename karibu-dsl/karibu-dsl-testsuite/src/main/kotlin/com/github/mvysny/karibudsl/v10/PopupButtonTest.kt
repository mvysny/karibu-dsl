package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class PopupButtonTest() {
    @BeforeEach
    fun setup() { MockVaadin.setup() }
    @AfterEach
    fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().popupButton("foo") {
            content {
                button("Hello!")
            }
        }
        _get<PopupButton>()
        _get<Button> { text = "Hello!" }
    }

    @Test fun `content keeps its width and height`() {
        UI.getCurrent().popupButton("foo") {
            content {
                verticalLayout { width = "200px"; height = "300px" }
            }
        }
        expect("200px") { _get<VerticalLayout>().width }
        expect("300px") { _get<VerticalLayout>().height }
    }
}
