package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._text
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.dom.DomEventListener
import java.io.File
import java.util.*
import kotlin.streams.toList
import kotlin.test.expect

fun DynaNodeGroup.vaadinUtilsTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("text") {
        val t: Text = UI.getCurrent().text("foo")
        expect("foo") { t.text }
        expect(UI.getCurrent()) { t.parent.get() }
    }

    test("setOrRemoveAttribute") {
        val t = Div().element
        expect(null) { t.getAttribute("foo") }
        t.setOrRemoveAttribute("foo", "bar")
        expect("bar") { t.getAttribute("foo") }
        t.setOrRemoveAttribute("foo", null)
        expect(null) { t.getAttribute("foo") }
    }

    group("removeFromParent()") {
        test("component with no parent") {
            val t = Text("foo")
            t.removeFromParent()
            expect(null) { t.parent.orElse(null) }
        }
        test("nested component") {
            val fl = FlexLayout().apply { label("foo") }
            val label = fl.getComponentAt(0)
            expect(fl) { label.parent.get() }
            label.removeFromParent()
            expect(null) { label.parent.orElse(null) }
            expect(0) { fl.componentCount }
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
    }

    test("tooltip") {
        val b = Button()
        expect(null) { b.tooltip }
        b.tooltip = ""
        expect<String?>("") { b.tooltip } // https://youtrack.jetbrains.com/issue/KT-32501
        b.tooltip = "foo"
        expect<String?>("foo") { b.tooltip } // https://youtrack.jetbrains.com/issue/KT-32501
        b.tooltip = null
        expect(null) { b.tooltip }
    }

    test("addContextMenuListener smoke") {
        Button().addContextMenuListener(DomEventListener {})
    }

    group("findAncestor") {
        test("null on no parent") {
            expect(null) { Button().findAncestor { false } }
        }
        test("null on no acceptance") {
            expect(null) { UI.getCurrent().button().findAncestor { false } }
        }
        test("finds UI") {
            expect(UI.getCurrent()) { UI.getCurrent().button().findAncestor { it is UI } }
        }
        test("doesn't find self") {
            expect(UI.getCurrent()) { UI.getCurrent().button().findAncestor { true } }
        }
    }

    group("findAncestorOrSelf") {
        test("null on no parent") {
            expect(null) { Button().findAncestorOrSelf { false } }
        }
        test("null on no acceptance") {
            expect(null) { UI.getCurrent().button().findAncestorOrSelf { false } }
        }
        test("finds self") {
            val button = UI.getCurrent().button()
            expect(button) { button.findAncestorOrSelf { true } }
        }
    }

    test("isNestedIn") {
        expect(false) { Button().isNestedIn(UI.getCurrent()) }
        expect(true) { UI.getCurrent().button().isNestedIn(UI.getCurrent()) }
    }

    test("isAttached") {
        expect(true) { UI.getCurrent().isAttached() }
        expect(false) { Button("foo").isAttached() }
        expect(true) {
            val button: Button = UI.getCurrent().button {}
            button.isAttached()
        }
        UI.getCurrent().close()
        expect(true) { UI.getCurrent().isAttached() }
    }

    test("insertBefore") {
        val l = HorizontalLayout()
        val first = Span("first")
        l.addComponentAsFirst(first)
        val second = Span("second")
        l.insertBefore(second, first)
        expect("second, first") { l.children.toList().map { it._text } .joinToString() }
        l.insertBefore(Span("third"), first)
        expect("second, third, first") { l.children.toList().map { it._text } .joinToString() }
    }

    test("isNotEmpty") {
        val l = HorizontalLayout()
        expect(false) { l.isNotEmpty }
        l.addComponentAsFirst(Span("first"))
        expect(true) { l.isNotEmpty }
        l.removeAll()
        expect(false) { l.isNotEmpty }
    }

    test("isEmpty") {
        val l = HorizontalLayout()
        expect(true) { l.isEmpty }
        l.addComponentAsFirst(Span("first"))
        expect(false) { l.isEmpty }
        l.removeAll()
        expect(true) { l.isEmpty }
    }
}

fun File.loadAsProperties(): Properties {
    val p = Properties()
    absoluteFile.reader().use { p.load(it.buffered()) }
    return p
}
