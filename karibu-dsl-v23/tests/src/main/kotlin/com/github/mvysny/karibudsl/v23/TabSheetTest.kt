package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.span
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.v23.removeAll
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.expect

abstract class TabSheetTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().tabSheet()
        _expectOne<TabSheet>()
    }

    @Test
    fun exampleDsl() {
        UI.getCurrent().tabSheet {
            tab("Dashboard") {
                span("This is the Dashboard tab content")
            }
            tab("Payment") {
                span("This is the Payment tab content")
            }
        }
        _expectOne<TabSheet>()
        _expect<Tab>(2)
        _expect<Span> { text = "This is the Dashboard tab content" } // only one span is attached at one time
        _get<TabSheet>().selectedIndex = 1
        _expect<Span> { text = "This is the Payment tab content" } // only one span is attached at one time
    }

    @Nested inner class ContentPopulation {
        @Test fun `Initially empty`() {
            val ts = TabSheet()
            ts._expectNone<Tab>()
        }
        @Test fun `Adding a tab to an empty TabSheet shows it immediately`() {
            UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
            }
            _expectOne<Span> { text = "it works!" }
        }
        @Test fun `Adding a tab to a non-empty TabSheet doesn't change the currently shown tab`() {
            UI.getCurrent().tabSheet {
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
        @Test fun `Removing last tab clears the TabSheet contents`() {
            lateinit var tab: Tab
            val ts = UI.getCurrent().tabSheet {
                tab = tab("foo") { span("it works!") }
            }
            ts.remove(tab)
            _expectNone<Span> { text = "it works!" }
        }
        @Test fun `Removing all tabs clears the TabSheet contents`() {
            val ts = UI.getCurrent().tabSheet {
                tab("foo") { span("it works!") }
            }
            ts.removeAll()
            _expectNone<Span> { text = "it works!" }
        }
    }

    @Nested inner class SelectedIndex {
        @Test fun `-1 when empty`() {
            expect(-1) { TabSheet().selectedIndex }
        }
        @Test fun `Adding a tab to an empty TabSheet selects it immediately`() {
            val ts = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
            }
            expect(0) { ts.selectedIndex }
        }
        @Test fun `Adding a tab to a non-empty TabSheet doesn't change the selection`() {
            val ts = UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                }
                tab("bar") {
                    span("it works 2!")
                }
            }
            expect(0) { ts.selectedIndex }
        }
        @Test fun `Removing last tab clears selection`() {
            lateinit var tab: Tab
            val ts = UI.getCurrent().tabSheet {
                tab = tab("foo") { span("it works!") }
            }
            ts.remove(tab)

            expect(-1) { ts.selectedIndex }
        }
        @Test fun `Removing all tabs clears selection`() {
            val ts = UI.getCurrent().tabSheet {
                tab("foo") { span("it works!") }
            }
            ts.removeAll()
            expect(-1) { ts.selectedIndex }
        }
    }

    @Nested inner class SelectedTab {
        @Test fun `null when empty`() {
            expect(null) { TabSheet().selectedTab }
        }
        @Test fun `Adding a tab to an empty TabSheet selects it immediately`() {
            lateinit var tab: Tab
            val ts = UI.getCurrent().tabSheet {
                tab = tab("foo") {
                    span("it works!")
                }
            }
            expect(tab) { ts.selectedTab }
        }
        @Test fun `Adding a tab to a non-empty TabSheet doesn't change the selection`() {
            lateinit var tab: Tab
            val ts = UI.getCurrent().tabSheet {
                tab = tab("foo") {
                    span("it works!")
                }
                tab("bar") {
                    span("it works 2!")
                }
            }
            expect(tab) { ts.selectedTab }
        }
        @Test fun `Removing last tab clears selection`() {
            lateinit var tab: Tab
            val ts = UI.getCurrent().tabSheet {
                tab = tab("foo") { span("it works!") }
            }
            ts.remove(tab)

            expect(null) { ts.selectedTab }
        }
        @Test fun `Removing all tabs clears selection`() {
            val ts = UI.getCurrent().tabSheet {
                tab("foo") { span("it works!") }
            }
            ts.removeAll()

            expect(null) { ts.selectedTab }
        }
    }

    @Test fun `helpful error message when trying to add two components as tab content`() {
        val ex = assertThrows<IllegalStateException> {
            UI.getCurrent().tabSheet {
                tab("foo") {
                    span("it works!")
                    span("can't have two children")
                }
            }
        }
        expect("Too many components to add - TabSheet.tab{} can only host one!") { ex.message }
    }
}
