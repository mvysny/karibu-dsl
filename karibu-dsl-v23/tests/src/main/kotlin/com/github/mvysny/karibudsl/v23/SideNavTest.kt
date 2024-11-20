package com.github.mvysny.karibudsl.v23

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributesting.v23._click
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.myapp.DemoRoute
import org.myapp.MainRoute

val routes = Routes().autoDiscoverViews("org.myapp")

abstract class SideNavTest {
    @BeforeEach fun setup() { MockVaadin.setup(routes) }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().sideNav()
        _expectOne<SideNav>()
    }

    @Test fun `full dsl test`() {
        UI.getCurrent().sideNav {
            route(MainRoute::class, VaadinIcon.QUESTION) {
                item("Hello!")
                item("Hello2!", "path") {
                    item("subitem")
                }
            }
            route(DemoRoute::class)
            item("Hierarchy") {
                route(MainRoute::class, VaadinIcon.QUESTION, "Main2") {
                }
            }
        }
        _expectOne<SideNav>()
        _expectOne<SideNavItem> { label = "Hello!" }
        _expectOne<SideNavItem> { label = "Hello2!" }
        _expectOne<SideNavItem> { label = "subitem" }
        _expectOne<SideNavItem> { label = "Hierarchy" }
        _expectOne<SideNavItem> { label = "Main" }
        _expectOne<SideNavItem> { label = "Main2" }
        _expectOne<SideNavItem> { label = "DemoRoute" }
    }

    @Test fun navigation() {
        UI.getCurrent().sideNav {
            route(MainRoute::class)
            route(DemoRoute::class)
        }
        _get<SideNavItem> { label = "Main" } ._click()
        _expectOne<MainRoute>()
        _expectNone<DemoRoute>()
        _get<SideNavItem> { label = "DemoRoute" } ._click()
        _expectOne<DemoRoute>()
        _expectNone<MainRoute>()
    }
}
