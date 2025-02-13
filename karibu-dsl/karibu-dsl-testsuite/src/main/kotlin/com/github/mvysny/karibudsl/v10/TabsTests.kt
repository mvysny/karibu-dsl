package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expect
import com.github.mvysny.kaributools.IconName
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractTabsTests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().tabs()
        UI.getCurrent().tabs{}
        UI.getCurrent().tabs(Tabs.Orientation.HORIZONTAL)
        UI.getCurrent().tabs(Tabs.Orientation.HORIZONTAL){}
        _expect<Tabs>(4)
    }

    @Test fun dsl() {
        UI.getCurrent().tabs {
            tab("Details", VaadinIcon.LEVEL_LEFT) {}
            tab("Payment")
            tab("Shipping") {}
            tab("Details", VaadinIcon.LEVEL_LEFT)
        }
        _expect<Tab>(4)
    }

    @Test fun icon() {
        UI.getCurrent().tabs {
            tab("Details", VaadinIcon.LEVEL_LEFT) {}
        }
        _expect<Tab>()
        _expect<Icon> { icon = IconName.of(VaadinIcon.LEVEL_LEFT) }
    }

    @Test fun onSelected() {
        lateinit var selected: Tab
        lateinit var firstTab: Tab
        lateinit var secondTab: Tab
        val tabs = UI.getCurrent().tabs {
            firstTab = tab("Details") {
                onSelected { selected = this }
            }
            secondTab = tab("Payment") {
                onSelected { selected = this }
            }
        }
        _expect<Tab>(2)
        expect(firstTab) { selected }

        tabs.selectedTab = secondTab
        expect(secondTab) { selected }
    }
}
