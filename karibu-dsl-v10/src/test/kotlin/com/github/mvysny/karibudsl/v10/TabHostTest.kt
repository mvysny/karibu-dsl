package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10.expectList
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.tabs.Tab
import kotlin.test.expect

class TabHostTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().tabHost()
        _expectOne<TabHost>()
    }

    test("Initially empty") {
        val th = TabHost()
        expect(-1) { th.selectedIndex }
        expect(null) { th.selectedTab }
        expectList() { th.tabs }
        expect(0) { th.tabCount }
    }

    test("Adding a tab to an empty tabhost selects it immediately") {
        lateinit var tab: Tab
        val th = UI.getCurrent().tabHost {
            tab = tab("foo") {
                span("it works!")
            }
        }
        expect(0) { th.selectedIndex }
        expect(tab) { th.selectedTab }
        expectList(tab) { th.tabs }
        expect(1) { th.tabCount }
        _expectOne<Span> { text = "it works!" }
    }

    test("Adding a tab to a non-empty tabhost doesn't change the selection") {
        lateinit var tab: Tab
        lateinit var tab2: Tab
        val th = UI.getCurrent().tabHost {
            tab = tab("foo") {
                span("it works!")
            }
            tab2 = tab("bar") {
                span("it works 2!")
            }
        }
        expect(0) { th.selectedIndex }
        expect(tab) { th.selectedTab }
        expectList(tab, tab2) { th.tabs }
        expect(2) { th.tabCount }
        _expectOne<Span> { text = "it works!" }
    }

    test("Adding a tab with null contents works") {
        lateinit var tab: Tab
        val th = UI.getCurrent().tabHost {
            tab = addTab("foo")
        }
        expect(0) { th.selectedIndex }
        expect(tab) { th.selectedTab }
        expectList(tab) { th.tabs }
        expect(1) { th.tabCount }

        th.setTabContents(tab, Span("it works!"))
        _expectOne<Span> { text = "it works!" }
    }

    test("Removing last tab clears selection") {
        lateinit var tab: Tab
        val th = UI.getCurrent().tabHost {
            tab = tab("foo") { span("it works!") }
        }
        th.remove(tab)

        expect(-1) { th.selectedIndex }
        expect(null) { th.selectedTab }
        expectList() { th.tabs }
        expect(0) { th.tabCount }
        _expectNone<Span> { text = "it works!" }
    }

    test("Removing all tabs clears selection") {
        lateinit var tab: Tab
        val th = UI.getCurrent().tabHost {
            tab = tab("foo") { span("it works!") }
        }
        th.removeAll()

        expect(-1) { th.selectedIndex }
        expect(null) { th.selectedTab }
        expectList() { th.tabs }
        expect(0) { th.tabCount }
        _expectNone<Span> { text = "it works!" }
    }
})
