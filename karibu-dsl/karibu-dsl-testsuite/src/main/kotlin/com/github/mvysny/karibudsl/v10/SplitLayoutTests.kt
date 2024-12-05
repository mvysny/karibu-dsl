package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expect
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.splitlayout.SplitLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class SplitLayoutTests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test
    fun smoke() {
        UI.getCurrent().splitLayout()
        UI.getCurrent().splitLayout{}
        UI.getCurrent().splitLayout(SplitLayout.Orientation.VERTICAL)
        UI.getCurrent().splitLayout(SplitLayout.Orientation.VERTICAL){}
        _expect<SplitLayout>(4)
    }

    @Test
    fun dsl() {
        lateinit var left: Button
        lateinit var right: Span
        UI.getCurrent().splitLayout {
            primary {
                left = button("left side")
            }
            secondary {
                right = span("right side")
            }
        }
        _expectOne<SplitLayout>()
        _expectOne<Button>()
        _expectOne<Span>()
        expect(left) { _get<SplitLayout>().primaryComponent }
        expect(right) { _get<SplitLayout>().secondaryComponent }
    }
}
