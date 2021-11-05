package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI

fun DynaNodeGroup.karibuToolsTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    group("LabelWrapper DSL") {
        test("smoke") {
            UI.getCurrent().labelWrapper("label")
            UI.getCurrent().labelWrapper("foo") {
                span("bar")
            }
        }
    }

    group("HtmlSpan DSL") {
        test("smoke") {
            UI.getCurrent().htmlSpan("<pre>foo bar</pre>")
        }
    }
}
