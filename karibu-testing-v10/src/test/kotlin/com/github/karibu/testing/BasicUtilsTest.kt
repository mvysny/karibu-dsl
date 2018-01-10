package com.github.karibu.testing

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.Route
import org.junit.Test
import kotlin.test.expect

class BasicUtilsTest {
    @Test
    fun testAutoViewDiscovery() {
        expect(setOf(TestingView::class.java)) { autoDiscoverViews("com.github") }
    }

    @Test
    fun testCallingAutoViewDiscoveryMultipleTimesWontFail() {
        expect(setOf(TestingView::class.java)) { autoDiscoverViews("com.github") }
        expect(setOf(TestingView::class.java)) { autoDiscoverViews("com.github") }
    }
}

@Route("testing")
class TestingView : Div()

