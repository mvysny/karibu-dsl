package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.AutoViewProvider
import com.vaadin.navigator.View
import kotlin.test.expect

class BasicUtilsTest : DynaTest({
    test("AutoViewDiscovery") {
        autoDiscoverViews("com.github")
        expect("testing") { AutoViewProvider.getMapping(TestingView::class.java) }
    }

    test("calling autoDiscoverViews() multiple times won't fail") {
        autoDiscoverViews("com.github")
        autoDiscoverViews("com.github")
        expect("testing") { AutoViewProvider.getMapping(TestingView::class.java) }
    }

    test("IntRangeSize") {
        expect(0) { (0 until 0).size }
        expect(1) { (0..0).size }
        expect(1) { (0..0).toList().size }
    }
})

@AutoView
class TestingView : View
