package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class AccordionTest {
    @BeforeEach
    fun setup() { MockVaadin.setup() }
    @AfterEach
    fun teardown() { MockVaadin.tearDown() }

    @Test
    fun smoke() {
        UI.getCurrent().accordion {
            panel("lorem ipsum") {
                content {
                    nativeLabel("dolor sit amet")
                }
            }
            panel {
                summary { checkBox("More Lorem Ipsum?") }
                content {
                    nativeLabel("dolor sit amet")
                }
            }
        }
    }
}
