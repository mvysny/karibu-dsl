package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import kotlin.streams.toList
import kotlin.test.expect

fun DynaNodeGroup.gridTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    group("grid dsl") {
        test("simple") {
            UI.getCurrent().grid<Person>()
        }
        test("basic properties") {
            val grid: Grid<String> = UI.getCurrent().grid<String>()
            // TODO commented out until the Grid row situation is resolved. See [Grid.hotfixMissingHeaderRow] for more details.
            //expect(1) { grid.headerRows.size }
            expect(0) { grid.columns.size }
            expect(String::class.java) { grid.beanType }
        }
        test("with generified item") {
            data class TestingClass<T>(var item: T? = null)
            UI.getCurrent().grid<TestingClass<String>>()
        }
    }

    group("addColumnFor tests") {
        test("grid addColumnFor works both for nullable and non-null properties") {
            data class TestingClass(var foo: String?, var bar: String, var nonComparable: List<String>)
            Grid<TestingClass>().apply {
                addColumnFor(TestingClass::foo)   // this must compile
                addColumnFor(TestingClass::bar)   // this must compile
                addColumnFor(TestingClass::nonComparable)  // this must compile
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
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        test("sorting by column also works with in-memory container 2") {
            val grid = Grid<Person>().apply {
                addColumnFor<Person, String>("fullName")
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }
    }


    test("column isExpand") {
        val grid = Grid<Person>()
        val col = grid.addColumnFor(Person::alive)
        expect(1) { col.flexGrow }  // by default the flexGrow is 1
        val col2 = grid.addColumnFor(Person::fullName) { isExpand = false }
        expect(0) { col2.flexGrow }
    }


    group("header cell retrieval test") {
        test("one component") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendHeaderRow().getCell(Person::fullName).component = TextField("Foo!")
            }
            expect("Foo!") {
                val tf = grid.headerRows.last().getCell(Person::fullName).component
                (tf as TextField).caption
            }
            grid.headerRows.last().getCell(Person::fullName).component = null
            expect(null) { grid.headerRows.last().getCell(Person::fullName).component }
        }
        test("two components") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendHeaderRow().getCell(Person::fullName).component = TextField("Foo!")
                appendHeaderRow().getCell(Person::fullName).component = TextField("Bar!")
            }
            expect("Bar!") {
                val tf = grid.headerRows.last().getCell(Person::fullName).component
                (tf as TextField).caption
            }
        }
    }

    group("footer cell retrieval test") {
        test("one component") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
            }
            expect("Foo!") {
                val tf = grid.footerRows.last().getCell(Person::fullName).component
                (tf as TextField).caption
            }
            grid.footerRows.last().getCell(Person::fullName).component = null
            expect(null) { grid.footerRows.last().getCell(Person::fullName).component }
        }
        test("two components") {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
                appendFooterRow().getCell(Person::fullName).component = TextField("Bar!")
            }
            expect("Bar!") {
                val tf = grid.footerRows.last().getCell(Person::fullName).component
                (tf as TextField).caption
            }
        }
    }

    test("serialization") {
        val grid = Grid<Person>().apply {
            addColumnFor(Person::fullName)
            appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
        }
        grid.cloneBySerialization()
    }

    group("treeGrid") {
        test("smoke") {
            UI.getCurrent().treeGrid<String>()
            _expectOne<TreeGrid<*>>()
        }
        test("basic properties") {
            val grid: TreeGrid<String> = UI.getCurrent().treeGrid<String>()
            expect(1) { grid.headerRows.size }
            expect(0) { grid.columns.size }
            expect(String::class.java) { grid.beanType }
        }
    }
}

/**
 * Enforces a particular sorting upon a grid.
 */
fun <T> Grid<T>.sort(vararg criteria: QuerySortOrder) {
    // check that columns are sortable
    val crit: List<GridSortOrder<T>> = criteria.map {
        val col: Grid.Column<T> = getColumnByKey(it.sorted)
        require(col.isSortable) { "Column for ${it.sorted} is not marked sortable" }
        GridSortOrder(col, it.direction)
    }

    sort(crit)
}

fun <T> Grid<T>.setItems2(items: Collection<T>) {
    dataProvider = ListDataProvider2(items)
}

/**
 * Need to have this class because of https://github.com/vaadin/flow/issues/8553
 */
class ListDataProvider2<T>(items: Collection<T>): ListDataProvider<T>(items) {
    override fun toString(): String = "ListDataProvider2{${items.size} items}"
}
