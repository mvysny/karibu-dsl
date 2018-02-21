package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.Route
import kotlin.test.expect

class BasicUtilsTest : DynaTest({

    val allViews = setOf(TestingView::class.java, HelloWorldView::class.java, WelcomeView::class.java)

    test("AutoViewDiscovery") {
        expect(allViews) { autoDiscoverViews("com.github") }
    }

    test("calling autoDiscoverViews() multiple times won't fail") {
        expect(allViews) { autoDiscoverViews("com.github") }
        expect(allViews) { autoDiscoverViews("com.github") }
    }
})

@Route("testing")
class TestingView : Div()
