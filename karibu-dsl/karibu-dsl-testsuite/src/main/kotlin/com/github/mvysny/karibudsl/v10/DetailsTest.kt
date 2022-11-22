package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v10._text
import com.github.mvysny.kaributools.label
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.details.Details
import com.vaadin.flow.component.html.Span
import kotlin.streams.toList
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.detailsTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().details("Hello") {
            content {
                button("hi!")
            }
        }
        _expectOne<Span> { text = "Hello" }
        _expectOne<Button> { text = "hi!" }
        expect("hi!") { _get<Details>().content.toList().joinToString { it._text!! } }
    }

    test("summary as component") {
        UI.getCurrent().details {
            summary {
                button("hi!")
            }
        }
        _expectOne<Button> { text = "hi!" }
        expect("hi!") { _get<Details>().summary._text }
    }

    test("clear content") {
        val d = UI.getCurrent().details()
        expectList() { d.content.toList() }
        d.content {
            button("hi!")
        }
        expect(1) { d.content.count() }
        d.clearContent()
        expectList() { d.content.toList() }
    }
}
