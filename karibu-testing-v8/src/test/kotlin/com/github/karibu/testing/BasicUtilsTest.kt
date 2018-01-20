package com.github.karibu.testing

import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.AutoViewProvider
import com.vaadin.navigator.View
import org.junit.Test
import kotlin.test.expect

class BasicUtilsTest {
    @Test
    fun testAutoViewDiscovery() {
        autoDiscoverViews("com.github")
        expect("testing") { AutoViewProvider.getMapping(TestingView::class.java) }
    }

    @Test
    fun testCallingAutoViewDiscoveryMultipleTimesWontFail() {
        autoDiscoverViews("com.github")
        autoDiscoverViews("com.github")
        expect("testing") { AutoViewProvider.getMapping(TestingView::class.java) }
    }

    @Test
    fun testIntRangeSize() {
        expect(0) { (0 until 0).size }
        expect(1) { (0..0).size }
        expect(1) { (0..0).toList().size }
    }
}

@AutoView
class TestingView : View
