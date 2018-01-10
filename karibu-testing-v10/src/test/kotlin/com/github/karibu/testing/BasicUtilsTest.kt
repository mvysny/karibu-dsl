package com.github.karibu.testing

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.Route
import org.junit.Test
import kotlin.test.expect

class BasicUtilsTest {
    private val allViews = setOf(TestingView::class.java, HelloWorldView::class.java, WelcomeView::class.java)
    @Test
    fun testAutoViewDiscovery() {
        expect(allViews) { autoDiscoverViews("com.github") }
    }

    @Test
    fun testCallingAutoViewDiscoveryMultipleTimesWontFail() {
        expect(allViews) { autoDiscoverViews("com.github") }
        expect(allViews) { autoDiscoverViews("com.github") }
    }
}

@Route("testing")
class TestingView : Div()
