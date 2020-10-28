package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import kotlin.test.expect

class AppLayout : VerticalLayout(), RouterLayout
@Route("")
class RootView : VerticalLayout()
@Route("testing", layout = AppLayout::class)
class TestingView : VerticalLayout()
@Route("testing/foo/bar")
class TestingView2 : VerticalLayout()
@Route("testingp")
class TestingParametrizedView : VerticalLayout(), HasUrlParameter<Long> {
    override fun setParameter(event: BeforeEvent, parameter: Long?) {
        parameter!!
    }
}
@Route("testingp/with/subpath")
class TestingParametrizedView2 : VerticalLayout(), HasUrlParameter<Long> {
    override fun setParameter(event: BeforeEvent, parameter: Long?) {
        parameter!!
    }
}

class RouterTest : DynaTest({
    beforeEach {
        MockVaadin.setup(Routes().apply { routes.addAll(listOf(
                TestingView::class.java,
                TestingParametrizedView::class.java,
                RootView::class.java,
                TestingParametrizedView2::class.java,
                TestingView2::class.java
        )) })
        _expectNone<TestingView>()
        _expectNone<TestingParametrizedView>()
    }
    afterEach { MockVaadin.tearDown() }

    group("navigateToView") {
        test("reified") {
            navigateToView<TestingView>()
            _get<TestingView>()
        }
        test("reified doesn't work on parametrized views") {
            expectThrows(IllegalArgumentException::class, "requires a parameter and can not be resolved") {
                // it fails somewhere deeply in Vaadin Flow
                navigateToView<TestingParametrizedView>()
            }
            _expectNone<TestingParametrizedView>()
        }
        test("class") {
            navigateToView(TestingView::class)
            _get<TestingView>()
        }
        test("parametrized") {
            navigateToView(TestingParametrizedView::class, 1L)
            _get<TestingParametrizedView>()
        }
    }

    group("routerLinks") {
        test("no target") {
            expect("") { UI.getCurrent().routerLink().href }
        }
        test("simple target") {
            expect("testing") { UI.getCurrent().routerLink(null, "foo", TestingView::class).href }
        }
        test("parametrized target with missing param") {
            expectThrows(IllegalArgumentException::class, "requires a parameter and can not be resolved") {
                UI.getCurrent().routerLink(null, "foo", TestingParametrizedView::class)
            }
        }
        test("parametrized target") {
            expect("testingp/1") { UI.getCurrent().routerLink(null, "foo", TestingParametrizedView::class, 1L).href }
        }
    }

    group("viewClass") {
        test("navigating to root view") {
            UI.getCurrent().navigate(TestingView::class.java)
            var called = false
            UI.getCurrent().addAfterNavigationListener {
                called = true
                expect(RootView::class.java) { it.viewClass }
            }
            UI.getCurrent().navigate(RootView::class.java)
            expect(true) { called }
        }
        test("navigating to TestingView") {
            var called = false
            UI.getCurrent().addAfterNavigationListener {
                called = true
                expect(TestingView::class.java) { it.viewClass }
            }
            UI.getCurrent().navigate(TestingView::class.java)
            expect(true) { called }
        }
        test("navigating to TestingView2") {
            var called = false
            UI.getCurrent().addAfterNavigationListener {
                called = true
                expect(TestingView::class.java) { it.viewClass }
            }
            UI.getCurrent().navigate(TestingView::class.java)
            expect(true) { called }
        }
        test("navigating to TestingParametrizedView") {
            var called = false
            UI.getCurrent().addAfterNavigationListener {
                called = true
                expect(TestingParametrizedView::class.java) { it.viewClass }
            }
            UI.getCurrent().navigate(TestingParametrizedView::class.java, 25L)
            expect(true) { called }
        }
        test("navigating to TestingParametrizedView2") {
            var called = false
            UI.getCurrent().addAfterNavigationListener {
                called = true
                expect(TestingParametrizedView2::class.java) { it.viewClass }
            }
            UI.getCurrent().navigate(TestingParametrizedView2::class.java, 25L)
            expect(true) { called }
        }
        test("location") {
            expect(RootView::class.java) { Location("").getViewClass() }
            expect(TestingView::class.java) { Location("testing").getViewClass() }
            expect(TestingView2::class.java) { Location("testing/foo/bar").getViewClass() }
            expect(TestingParametrizedView::class.java) { Location("testingp/20").getViewClass() }
            expect(TestingParametrizedView2::class.java) { Location("testingp/with/subpath/20").getViewClass() }
            expect(null) { Location("nonexisting").getViewClass() }
        }
    }

    test("UI.currentLocation") {
        var expectedLocation = "."
        var expectedLocationBefore = "."
        UI.getCurrent().addBeforeLeaveListener {
            expect(expectedLocationBefore) { UI.getCurrent().currentLocation.pathWithQueryParameters }
        }
        UI.getCurrent().addBeforeEnterListener {
            expect(expectedLocationBefore) { UI.getCurrent().currentLocation.pathWithQueryParameters }
        }
        UI.getCurrent().addAfterNavigationListener {
            expect(expectedLocation) { UI.getCurrent().currentLocation.pathWithQueryParameters }
        }
        expect(expectedLocation) { UI.getCurrent().currentLocation.pathWithQueryParameters }

        expectedLocation = "testing"
        navigateToView<TestingView>()
        expect("testing") { UI.getCurrent().currentLocation.pathWithQueryParameters }
        expectedLocationBefore = "testing"
    }
})
