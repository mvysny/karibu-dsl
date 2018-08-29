package com.github.vok.karibudsl.flow

import com.github.karibu.testing.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.DataCommunicator
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import java.util.stream.Stream
import kotlin.streams.toList
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
            expectList("fullName") {
                grid.getColumnBy(Person::fullName).getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted }
            }
        }

        test("column header is set properly") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                addColumnFor(Person::alive)
                addColumnFor(Person::dateOfBirth)
            }
            expect("Full Name") { grid.getColumnBy(Person::fullName).header2 }
            expect("Alive") { grid.getColumnBy(Person::alive).header2 }
            expect("Date Of Birth") { grid.getColumnBy(Person::dateOfBirth).header2 }
        }

        test("sorting by column also works with in-memory container") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                setItems((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid.fetchItems().map { it.fullName } }
        }

        test("sorting by column also works with in-memory container 2") {
            val grid = Grid<Person>().apply {
                addColumnFor<Person, String>("fullName")
                setItems((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid.fetchItems().map { it.fullName } }
        }
    }


    test("column isExpand") {
        val grid = Grid<Person>()
        val col = grid.addColumnFor(Person::alive)
        expect(1) { col.flexGrow }  // by default the flexGrow is 1
        val col2 = grid.addColumnFor(Person::fullName) { isExpand = false }
        expect(0) { col2.flexGrow }
    }


    test("header cell retrieval test") {
        val grid = Grid<Person>().apply {
            addColumnFor(Person::fullName)
            appendHeaderRow().getCell(Person::fullName).component = TextField("Foo!")
        }
        expect("Foo!") {
            val tf = grid.headerRows.last().getCell(Person::fullName).component
            (tf as TextField).caption
        }
    }

    test("footer cell retrieval test") {
        val grid = Grid<Person>().apply {
            addColumnFor(Person::fullName)
            appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
        }
        expect("Foo!") {
            val tf = grid.footerRows.last().getCell(Person::fullName).component
            (tf as TextField).caption
        }
    }
})

/**
 * Enforces a particular sorting upon a grid.
 */
fun Grid<*>.sort(vararg criteria: QuerySortOrder) {
    // check that columns are sortable
    val crit: List<GridSortOrder<out Any>> = criteria.map {
        val col: Grid.Column<out Any> = getColumnByKey(it.sorted)
        require(col.isSortable) { "Column for ${it.sorted} is not marked sortable" }
        GridSortOrder(col, it.direction)
    }

    // remove this method when https://github.com/vaadin/vaadin-grid-flow/issues/130 is fixed
    val setSortOrder = Grid::class.java.getDeclaredMethod("setSortOrder", List::class.java, Boolean::class.java)
    setSortOrder.isAccessible = true
    setSortOrder.invoke(this, crit, false)
}

/**
 * Returns the items as they would have been shown in the Grid, taking into account current sorting/filters.
 */
fun <T> Grid<T>.fetchItems(): List<T> {
    val fetchFromProvider = DataCommunicator::class.java.getDeclaredMethod("fetchFromProvider", Int::class.java, Int::class.java)
    fetchFromProvider.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    val stream: Stream<T> = fetchFromProvider.invoke(dataCommunicator, 0, Int.MAX_VALUE) as Stream<T>
    return stream.toList()
}
