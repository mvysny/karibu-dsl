package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import kotlin.test.expect

class VaadinUtilsTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

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

    group("toggle class name") {
        test("add") {
            val t = Div()
            t.classNames.toggle("test")
            expect(setOf("test")) { t.classNames }
        }
        test("remove") {
            val t = Div()
            t.classNames.add("test")
            t.classNames.toggle("test")
            expect(setOf<String>()) { t.classNames }
        }
    }

    test("serverClick()") {
        val b = Button()
        var clicked = 0
        b.onLeftClick { clicked++ }
        b.serverClick()
        expect(1) { clicked }
        b.cloneBySerialization()
    }

    test("title") {
        val b = Button()
        expect(null) { b.tooltip }
        b.tooltip = ""
        expect<String?>("") { b.tooltip } // https://youtrack.jetbrains.com/issue/KT-32501
        b.tooltip = "foo"
        expect<String?>("foo") { b.tooltip } // https://youtrack.jetbrains.com/issue/KT-32501
        b.tooltip = null
        expect(null) { b.tooltip }
    }
})
