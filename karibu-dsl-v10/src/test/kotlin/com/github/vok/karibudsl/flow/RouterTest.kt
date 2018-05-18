package com.github.vok.karibudsl.flow

import com.github.karibu.testing.v10.MockVaadin
import com.github.karibu.testing.v10.Routes
import com.github.karibu.testing.v10._expectNone
import com.github.karibu.testing.v10._get
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import kotlin.test.expect

@Route("testing")
class TestingView : VerticalLayout()
@Route("testingp")
class TestingParametrizedView : VerticalLayout(), HasUrlParameter<Long> {
    override fun setParameter(event: BeforeEvent, parameter: Long?) {
        parameter!!
    }
}

class RouterTest : DynaTest({
    beforeEach {
        MockVaadin.setup(Routes().apply { routes.addAll(listOf(TestingView::class.java, TestingParametrizedView::class.java)) })
        _expectNone<TestingView>()
        _expectNone<TestingParametrizedView>()
    }

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
})