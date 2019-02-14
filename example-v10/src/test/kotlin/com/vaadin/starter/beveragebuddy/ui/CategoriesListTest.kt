package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import kotlin.test.expect

class CategoriesListTest : DynaTest({

    beforeEach {
        MockVaadin.setup(Routes().autoDiscoverViews("com.vaadin.starter"))
        UI.getCurrent().navigate("categories")
        _get<CategoriesList>()  // make sure that the navigation succeeded
    }
    afterEach { MockVaadin.tearDown() }

    test("grid has 9 rows") {
        UI.getCurrent().navigate("categories")
        val grid = _get<Grid<*>>()
        expect(9) { grid.dataProvider._size() }
    }

    test("create new category") {
        _get<Button> { caption = "New category (Alt+N)" } ._click()

        // make sure that the "New Category" dialog is opened
        _get<CategoryEditorDialog>()

        // do the happy flow: fill in the form with valid values and click "Save"
        _get<TextField> { caption = "Category Name" } ._value = "FooBarBaz"
        _get<Button> { caption = "Save" } ._click()

        _expectNone<CategoryEditorDialog>()     // expect the dialog to close
        expect(true) { CategoryService.findAll().any { it.name == "FooBarBaz" } }
    }

    test("edit existing category") {
        val cat = CategoryService.findAll()[0]
        val grid = _get<Grid<Category>>()
        grid.expectRow(0, cat.name, cat.getReviewCount(), "null")
        grid._clickRenderer(0, "edit")

        // make sure that the "Edit Category" dialog is opened
        _get<CategoryEditorDialog>()
        expect(cat.name) { _get<TextField> { caption = "Category Name" } ._value }
    }

    test("edit existing category via context menu") {
        val cat = CategoryService.findAll()[0]
        val grid = _get<Grid<Category>>()
        grid.expectRow(0, cat.name, cat.getReviewCount(), "null")
        _get<CategoriesList>().gridContextMenu._clickItemWithCaption("Edit (Alt+E)", cat)

        // make sure that the "Edit Category" dialog is opened
        _get<CategoryEditorDialog>()
        expect(cat.name) { _get<TextField> { caption = "Category Name" } ._value }
    }
})
