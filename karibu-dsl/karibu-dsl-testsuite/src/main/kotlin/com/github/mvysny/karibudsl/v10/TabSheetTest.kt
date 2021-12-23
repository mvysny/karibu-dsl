package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10.expectList
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.tabs.Tab
import kotlin.test.expect
import kotlin.test.fail

fun DynaNodeGroup.tabSheetTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().tabSheet()
        _expectOne<TabSheet>()
    }

    group("content population") {
        test("Initially empty") {
            val th = TabSheet()
            th._expectNone<Tab>()
        }
        test("Adding a tab to an empty TabSheet shows it immediately") {
            val th = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
            }
            _expectOne<Span> { text = "it works!" }
        }
        test("Adding a tab to a non-empty TabSheet doesn't change the currently shown tab") {
            val th = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
                tab("bar") {
                    span("it works 2!")
                }
            }
            _expectOne<Span> { text = "it works!" }
            _expectNone<Span>() { text = "it works 2!" }
        }
    }

    group("tabCount") {
        test("zero when empty") {
            expect(0) { TabSheet().tabCount }
        }
        test("adding 1 tab") {
            val ts = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
            }
            expect(1) { ts.tabCount }
        }
        test("two tabs") {
            val ts = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
                tab("bar") {
                    span("it works 2!")
                }
            }
            expect(2) { ts.tabCount }
        }
        test("10 tabs") {
            val ts = UI.getCurrent().tabSheet()
            (0..9).map { ts.addTab("tab $it") }
            expect(10) { ts.tabCount }
        }
    }

    group("selectedIndex") {
        test("-1 when empty") {
            expect(-1) { TabSheet().selectedIndex }
        }
        test("Adding a tab to an empty TabSheet selects it immediately") {
            val th = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
            }
            expect(0) { th.selectedIndex }
        }
        test("Adding a tab to a non-empty TabSheet doesn't change the selection") {
            val th = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
                tab("bar") {
                    span("it works 2!")
                }
            }
            expect(0) { th.selectedIndex }
        }
    }

    group("selectedTab") {
        test("null when empty") {
            expect(null) { TabSheet().selectedTab }
        }
        test("Adding a tab to an empty TabSheet selects it immediately") {
            lateinit var tab: Tab
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo") {
                    span("it works!")
                }
            }
            expect(tab) { th.selectedTab }
        }
        test("Adding a tab to a non-empty TabSheet doesn't change the selection") {
            lateinit var tab: Tab
            lateinit var tab2: Tab
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo") {
                    span("it works!")
                }
                tab2 = tab("bar") {
                    span("it works 2!")
                }
            }
            expect(tab) { th.selectedTab }
        }
    }

    group("tabs") {
        test("empty when no tabs") {
            expectList() { TabSheet().tabs }
        }
        test("adding 1 tab") {
            lateinit var tab: Tab
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo") {
                    span("it works!")
                }
            }
            expectList(tab) { th.tabs }
        }
        test("two tabs") {
            lateinit var tab: Tab
            lateinit var tab2: Tab
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo") {
                    span("it works!")
                }
                tab2 = tab("bar") {
                    span("it works 2!")
                }
            }
            expectList(tab, tab2) { th.tabs }
        }
        test("10 tabs") {
            val th = UI.getCurrent().tabSheet()
            val tabs = (0..9).map { th.addTab("tab $it") }
            expect(tabs) { th.tabs }
        }
    }

    test("Adding a tab with null contents works") {
        lateinit var tab: Tab
        val th = UI.getCurrent().tabSheet {
            tab = addTab("foo")
        }
        expect(0) { th.selectedIndex }
        expect(tab) { th.selectedTab }
        expect(null) { th.getTabContents(tab) }
        expectList(tab) { th.tabs }
        expect(1) { th.tabCount }
        expect(0) { tab.index }

        val span = Span("it works!")
        th.setTabContents(tab, span)
        _expectOne<Span> { text = "it works!" }
        expect(span) { th.getTabContents(tab) }
    }

    test("Removing last tab clears selection") {
        lateinit var tab: Tab
        val th = UI.getCurrent().tabSheet {
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
        val th = UI.getCurrent().tabSheet {
            tab("foo") { span("it works!") }
        }
        th.removeAll()

        expect(-1) { th.selectedIndex }
        expect(null) { th.selectedTab }
        expectList() { th.tabs }
        expect(0) { th.tabCount }
        _expectNone<Span> { text = "it works!" }
    }

    test("owner") {
        lateinit var tab: Tab
        val th = UI.getCurrent().tabSheet {
            tab = tab("foo") { span("it works!") }
        }
        expect(th) { tab.ownerTabSheet }
        tab.owner // check that the tab has an owner
    }

    group("tab contents") {
        test("non-empty contents") {
            lateinit var tab: Tab
            UI.getCurrent().tabSheet {
                tab = tab("foo") { span("it works!") }
            }
            expect<Class<*>>(Span::class.java) { tab.contents!!.javaClass }
        }

        test("clearing contents") {
            lateinit var tab: Tab
            UI.getCurrent().tabSheet {
                tab = tab("foo") { span("it works!") }
            }
            expect<Class<*>>(Span::class.java) { tab.contents!!.javaClass }
            tab.contents = null
            _expectNone<Span>()
            expect(null) { tab.contents }
        }

        test("clearing contents 2") {
            lateinit var tab: Tab
            val th = UI.getCurrent().tabSheet {
                tab = addTab("foo", Span("it works!"))
            }
            expect<Class<*>>(Span::class.java) { tab.contents!!.javaClass }

            th.setTabContents(tab, null)
            expect(null) { th.getTabContents(tab) }
        }
    }
    group("find contents") {
        test("empty tab") {
            lateinit var tab: Tab
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo")
            }
            expect(null) { tab.contents }
            expect(null) { th.findTabWithContents(Span("bar")) }
        }
        
        test("simple test") {
            lateinit var tab: Tab
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo") { span("it works!") }
            }
            expect(tab) { th.findTabWithContents(tab.contents!!) }
        }
    }
    group("findTabContaining") {
        test("empty tab") {
            val th = UI.getCurrent().tabSheet {
                tab("foo")
            }
            expect(null) { th.findTabContaining(Span("bar")) }
        }

        test("simple test") {
            lateinit var tab: Tab
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo") { span("it works!") }
            }
            expect(tab) { th.findTabContaining(tab.contents!!) }
        }

        test("complex test") {
            lateinit var tab: Tab
            lateinit var nested: Button
            val th = UI.getCurrent().tabSheet {
                tab = tab("foo") {
                    div {
                        div {
                            nested = button()
                        }
                    }
                }
            }
            expect(tab) { th.findTabContaining(nested) }
        }
    }

    group("lazy tabs") {
        test("addFirstLazyTabImmediatelyInvokesClosure") {
            val th = UI.getCurrent().tabSheet {}
            val producedLabel = Label("baz")
            th.addLazyTab("foo") { producedLabel }
            expect(producedLabel) { th.selectedTab!!.contents }
        }

        test("addSecondLazyTabDelaysClosure") {
            val th = UI.getCurrent().tabSheet {}
            val producedLabel = Label("baz")
            var allowInvoking = false
            val tab1 = th.addTab("bar")
            val tab2 = th.addLazyTab("foo") {
                if (!allowInvoking) fail("Should not invoke")
                producedLabel
            }
            expect(tab1) { th.selectedTab }
            allowInvoking = true

            th.selectedTab = tab2
            expect(tab2) { th.selectedTab!! }
            expect(producedLabel) { th.selectedTab!!.contents }
        }
    }
}
