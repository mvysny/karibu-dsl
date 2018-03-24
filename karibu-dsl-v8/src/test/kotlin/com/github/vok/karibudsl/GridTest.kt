package com.github.vok.karibudsl

import com.github.karibu.testing.MockVaadin
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.shared.data.sort.SortDirection
import com.vaadin.ui.Grid
import kotlin.test.expect

class GridTest : DynaTest({
    beforeEach { MockVaadin.setup() }

    group("addColumnFor tests") {
        test("grid addColumnFor works both for nullable and non-null properties") {
            data class TestingClass(var foo: String?, var bar: String)
            Grid<TestingClass>().apply {
                addColumnFor(TestingClass::foo)   // this must compile
                addColumnFor(TestingClass::bar)   // this must compile
            }
        }

        test("sets column by default to sortable") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
            }
            expect(true) { grid.getColumn("fullName").isSortable }
        }

        test("column header is set properly") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                addColumnFor(Person::alive)
                addColumnFor(Person::dateOfBirth)
            }
            expect("Full Name") { grid.getColumnBy(Person::fullName).caption }
            expect("Alive") { grid.getColumnBy(Person::alive).caption }
            expect("Date Of Birth") { grid.getColumnBy(Person::dateOfBirth).caption }
        }

        test("sorting by column also works with in-memory container") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                setItems((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider::class.java) { grid.dataProvider.javaClass }
            grid.sort("fullName", SortDirection.DESCENDING)
            expect((9 downTo 0).map { it.toString() }) { grid.fetchItems().map { it.fullName } }
        }
    }
})

/**
 * Returns the items as they would have been shown in the Grid, taking into account current sorting/filters.
 */
fun <T> Grid<T>.fetchItems(): List<T> = dataCommunicator.fetchItemsWithRange(0, Int.MAX_VALUE)
