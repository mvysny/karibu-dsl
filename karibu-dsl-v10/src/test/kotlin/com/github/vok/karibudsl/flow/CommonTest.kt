package com.github.vok.karibudsl.flow

import com.github.karibu.testing.v10.MockVaadin
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.FlexLayout
import kotlin.streams.toList
import kotlin.test.expect

class CommonTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    group("removeFromParent()") {
        test("component with no parent") {
            val t = Text("foo")
            t.removeFromParent()
            expect(null) { t.parent.orElse(null) }
        }
        test("nested component") {
            val l = FlexLayout().apply { label("foo") }
            val label = l.getComponentAt(0)
            expect(l) { label.parent.get() }
            label.removeFromParent()
            expect(null) { label.parent.orElse(null) }
            expect(0) { l.componentCount }
        }
    }
})