package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.starter.beveragebuddy.backend.Category
import com.vaadin.starter.beveragebuddy.backend.CategoryService
import com.vaadin.starter.beveragebuddy.ui.categories.CategoriesList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

/**
 * Tests the UI. Uses the Browserless Testing approach as provided by the [Karibu Testing](https://github.com/mvysny/karibu-testing) library.
 */
class CategoriesListTest : AbstractAppTest() {
    @BeforeEach fun navigate() {
        UI.getCurrent().navigate("categories")
        _expectOne<CategoriesList>()  // make sure that the navigation succeeded
    }

    @Test fun `grid lists all categories`() {
        // prepare testing data
        CategoryService.saveCategory(Category(name = "Beers"))
        UI.getCurrent().page.reload()

        // now the "Categories" list should be displayed. Look up the Grid and assert on its contents.
        val grid = _get<Grid<*>>()
        expect(1) { grid.dataProvider._size() }
    }

    @Test fun `create new category`() {
        UI.getCurrent().navigate("categories")
        _get<Button> { text = "New category (Alt+N)" } ._click()

        // make sure that the "New Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()

        // do the happy flow: fill in the form with valid values and click "Save"
        _get<TextField> { label = "Category Name" } .value = "Beer"
        clearNotifications()
        _get<Button> { text = "Save" } ._click()
        expectNotifications("Category successfully added.")

        _expectNone<EditorDialogFrame<*>>()     // expect the dialog to close
        expectList("Beer") { CategoryService.findAll().map { it.name } }
    }

    @Test fun `edit existing category`() {
        val cat = Category(name = "Beers")
        CategoryService.saveCategory(cat)
        UI.getCurrent().page.reload()
        val grid = _get<Grid<Category>>()
        grid.expectRow(0, "Beers", "0", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']", "Delete")
        grid._clickRenderer(0, "edit")

        // make sure that the "Edit Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()
        expect(cat.name) { _get<TextField> { label = "Category Name" } ._value }
    }

    @Test fun `edit existing category via context menu`() {
        val cat = Category(name = "Beers")
        CategoryService.saveCategory(cat)
        UI.getCurrent().page.reload()
        val grid = _get<Grid<Category>>()
        grid.expectRow(0, "Beers", "0", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']", "Delete")
        _get<CategoriesList>().gridContextMenu._clickItemWithCaption("Edit (Alt+E)", cat)

        // make sure that the "Edit Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()
        expect(cat.name) { _get<TextField> { label = "Category Name" } ._value }
    }

    @Test fun `delete existing category via context menu`() {
        val cat = Category(name = "Beers")
        CategoryService.saveCategory(cat)
        UI.getCurrent().page.reload()
        val grid = _get<Grid<Category>>()
        grid.expectRow(0, "Beers", "0", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']", "Delete")
        _get<CategoriesList>().gridContextMenu._clickItemWithCaption("Delete", cat)
        expectList() { CategoryService.findAll() }
        _get<Grid<Category>>().expectRows(0)
    }
}
