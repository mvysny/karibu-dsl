package com.vaadin.starter.beveragebuddy.ui

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._get
import com.github.karibu.testing._size
import com.github.karibu.testing.autoDiscoverViews
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import kotlin.test.expect

class CategoriesListTest : DynaTest({

    beforeEach { MockVaadin.setup(autoDiscoverViews("com.vaadin.starter")) }

    test("grid has 9 rows") {
        UI.getCurrent().navigate("categories")
        val grid = _get<Grid<*>>()
        expect(9) { grid.dataProvider._size() }
    }
})
