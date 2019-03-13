package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.vaadin.ui.Label
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
    }
})
