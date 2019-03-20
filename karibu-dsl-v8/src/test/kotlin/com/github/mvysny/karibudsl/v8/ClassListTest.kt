package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.vaadin.ui.*
import com.vaadin.ui.components.grid.FooterRow
import com.vaadin.ui.components.grid.HeaderRow
import kotlin.test.expect

class ClassListTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("empty class") {
        val label = Label()
        val styleNames = label.styleNames
        expect(true) { styleNames.isEmpty() }
        expect(0) { styleNames.size }
        expect("[]") { styleNames.toString() }
        expect(false) { styleNames.iterator().hasNext() }
        expect(false) { styleNames.contains("a") }
        styleNames.clear()
        expect("") { label.styleName }
    }

    test("single class") {
        val label = Label().apply { styleName = "foo" }
        val styleNames = label.styleNames
        expect(false) { styleNames.isEmpty() }
        expect(1) { styleNames.size }
        expect("[foo]") { styleNames.toString() }
        expect(true) { styleNames.iterator().hasNext() }
        expect("foo") { styleNames.iterator().next() }
        expect(false) { styleNames.iterator().apply { next() }.hasNext() }
        expect(false) { styleNames.contains("a") }
        expect(true) { styleNames.contains("foo") }
        styleNames.clear()
        expect("") { label.styleName }
    }

    test("three classes") {
        val label = Label().apply { styleName = "foo bar baz" }
        val styleNames = label.styleNames
        expect(false) { styleNames.isEmpty() }
        expect(3) { styleNames.size }
        expect("[foo, bar, baz]") { styleNames.toString() }
        expectList("foo", "bar", "baz") { styleNames.iterator().asSequence().toList() }
        expect(false) { styleNames.contains("a") }
        expect(true) { styleNames.contains("foo") }
        styleNames.clear()
        expect("") { label.styleName }
    }

    group("mutation") {
        test("repeated add") {
            val label = Label()
            val styleNames = label.styleNames
            expect(true) { styleNames.add("foo") }
            expect("foo") { label.styleName }
            expect(false) { styleNames.add("foo") }
            expect("foo") { label.styleName }
        }
        test("multiple add") {
            val label = Label()
            val styleNames = label.styleNames
            expect(true) { styleNames.addAll(listOf("foo", "bar", "baz")) }
            expect("foo bar baz") { label.styleName }
            expect(false) { styleNames.addAll(listOf("foo", "bar", "baz")) }
            expect("foo bar baz") { label.styleName }
        }
        test("removal") {
            val label = Label().apply { styleName = "foo bar baz" }
            val styleNames = label.styleNames
            expect(true) { styleNames.remove("foo") }
            expect("bar baz") { label.styleName }
            expect(false) { styleNames.remove("foo") }
            expect("bar baz") { label.styleName }
        }
        test("toggle") {
            val label = Label().apply { styleName = "foo bar baz" }
            label.styleNames.toggle("bar")
            expect("foo baz") { label.styleName }
            label.styleNames.toggle("bar")
            expect("foo baz bar") { label.styleName }
        }
    }

    group("null styles") {
        test("component") {
            val n = Label("foo").apply { styleName = null }
            expect("") { n.styleName }  // Vaadin Component never returns null as styleName
            n.styleNames.add("foo")
            expect("foo") { n.styleName }
            n.styleNames.remove("foo")
            expect("") { n.styleName }
        }

        test("notification") {
            val n = Notification("foo").apply { styleName = null }
            n.styleNames.add("foo")
            expect("foo") { n.styleName }
            n.styleNames.remove("foo")
            expect(null) { n.styleName }
        }

        test("TabSheet.Tab") {
            lateinit var t: TabSheet.Tab
            UI.getCurrent().apply {
                tabSheet {
                    label("foo") {
                        t = tab
                    }
                }
            }
            t.styleName = null
            t.styleNames.add("foo")
            expect("foo") { t.styleName }
            t.styleNames.remove("foo")
            expect(null) { t.styleName }
        }

        test("MenuItem") {
            lateinit var t: MenuBar.MenuItem
            UI.getCurrent().apply {
                menuBar {
                    t = item("foo")
                }
            }
            t.styleName = null
            t.styleNames.add("foo")
            expect("foo") { t.styleName }
            t.styleNames.remove("foo")
            expect(null) { t.styleName }
        }

        test("HeaderRow") {
            lateinit var t: HeaderRow
            UI.getCurrent().apply {
                grid<String> {
                    t = appendHeaderRow()
                }
            }
            t.styleName = null
            t.styleNames.add("foo")
            expect("foo") { t.styleName }
            t.styleNames.remove("foo")
            expect(null) { t.styleName }
        }

        test("FooterRow") {
            lateinit var t: FooterRow
            UI.getCurrent().apply {
                grid<String> {
                    t = appendFooterRow()
                }
            }
            t.styleName = null
            t.styleNames.add("foo")
            expect("foo") { t.styleName }
            t.styleNames.remove("foo")
            expect(null) { t.styleName }
        }
    }
})
