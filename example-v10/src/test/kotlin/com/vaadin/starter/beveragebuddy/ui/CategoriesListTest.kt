package com.vaadin.starter.beveragebuddy.ui

import com.github.karibu.testing.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import kotlin.test.expect

class CategoriesListTest : DynaTest({

    beforeEach { MockVaadin.setup(autoDiscoverViews("com.vaadin.starter")) }

    test("grid has 9 rows") {
        UI.getCurrent().navigate("categories")
        val grid = _get<Grid<*>>()
        expect(9) { grid.dataProvider._size() }
    }

    test("create new category") {
        UI.getCurrent().navigate("categories")
        _get<CategoriesList>()  // make sure that the navigation succeeded
        _get<Button> { caption = "New category" } ._click()

        // make sure that the "New Category" dialog is opened
        _get<CategoryEditorDialog>()

        // do the happy flow: fill in the form with valid values and click "Save"
        _get<TextField> { caption = "Category Name" } .value = "FooBarBaz"
        _get<Button> { caption = "Save" } ._click()

        _expectNone<CategoryEditorDialog>()     // expect the dialog to close
        expect(true) { CategoryService.findAll().any { it.name == "FooBarBaz" } }
    }
})
