package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class KaribuToolsTests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Nested inner class LabelWrapperDSL {
        @Test fun smoke() {
            UI.getCurrent().labelWrapper("label")
            UI.getCurrent().labelWrapper("foo") {
                span("bar")
            }
        }
    }

    @Nested inner class HtmlSpanDSL() {
        @Test fun smoke() {
            UI.getCurrent().htmlSpan("<pre>foo bar</pre>")
        }
    }
}
