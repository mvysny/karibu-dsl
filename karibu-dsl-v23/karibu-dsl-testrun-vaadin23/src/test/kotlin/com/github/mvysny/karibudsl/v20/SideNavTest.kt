package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.div
import com.github.mvysny.karibudsl.v10.span
import com.github.mvysny.karibudsl.v23.*
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.util.function.Predicate
import kotlin.test.expect

val routes = Routes().autoDiscoverViews("com.github.mvysny.karibudsl.v20")

@DynaTestDsl
fun DynaNodeGroup.sideNavTest() {
    beforeEach { MockVaadin.setup(routes) }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().sideNav()
        _expectOne<SideNav>()
    }

    test("full dsl test") {
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
        _expectOne<SideNavItem> { sidenavLabel = "Hello!" }
        _expectOne<SideNavItem> { sidenavLabel = "Hello2!" }
        _expectOne<SideNavItem> { sidenavLabel = "subitem" }
        _expectOne<SideNavItem> { sidenavLabel = "Hierarchy" }
        _expectOne<SideNavItem> { sidenavLabel = "Main" }
        _expectOne<SideNavItem> { sidenavLabel = "Main2" }
        _expectOne<SideNavItem> { sidenavLabel = "DemoRoute" }
    }

    test("navigation") {
        UI.getCurrent().sideNav {
            route(MainRoute::class)
            route(DemoRoute::class)
        }
        _get<SideNavItem> { sidenavLabel = "Main" } ._click()
        _expectOne<MainRoute>()
        _expectNone<DemoRoute>()
        _get<SideNavItem> { sidenavLabel = "DemoRoute" } ._click()
        _expectOne<DemoRoute>()
        _expectNone<MainRoute>()
    }
}

@Route("foo")
@PageTitle("Main")
class MainRoute : Div()

@Route("demo")
class DemoRoute : Div()

/**
 * Workaround for https://github.com/vaadin/flow/issues/16969
 */
var <T: Component> SearchSpec<T>.sidenavLabel: String?
    get() = null
    set(value) {
        this.predicates.add { (it is SideNav && it.label == value) || (it is SideNavItem && it.label == value) }
    }

/**
 * Workaround for https://github.com/mvysny/karibu-testing/issues/154
 */
@VaadinDsl
fun (@VaadinDsl SideNavItem)._click() {
    _expectEditableByUser()
    navigateTo(path)
}
