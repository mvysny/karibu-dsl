package com.vaadin.starter.beveragebuddy.ui

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._get
import com.github.karibu.testing._size
import com.github.karibu.testing.autoDiscoverViews
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import org.junit.Before
import org.junit.Test
import kotlin.test.expect

class CategoriesListTest {
    @Before
    fun mockVaadin() {
        MockVaadin.setup(autoDiscoverViews("com.vaadin.starter"))
    }

    @Test
    fun testGridHasNineRows() {
        UI.getCurrent().navigateTo("categories")
        val grid = _get<Grid<*>>()
        expect(9) { grid.dataProvider._size() }
    }
}
