package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributesting.v10.getCell
import com.github.mvysny.kaributools.*
import com.github.mvysny.kaributools.sort
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection
import kotlin.streams.toList
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.gridTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    group("grid dsl") {
        test("simple") {
            UI.getCurrent().grid<Person>()
            _expectOne<Grid<*>>()
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
        test("class") {
            UI.getCurrent().grid(Person::class.java)
            _expectOne<Grid<*>>()
        }
        test("kclass") {
            UI.getCurrent().grid(Person::class)
            _expectOne<Grid<*>>()
        }
        test("null kclass") {
            UI.getCurrent().grid<Person>(klass = null)
            _expectOne<Grid<*>>()
        }
        test("null class") {
            UI.getCurrent().grid<Person>(clazz = null)
            _expectOne<Grid<*>>()
        }
    }

    group("columnFor() tests") {
        test("grid columnFor() works both for nullable and non-null properties") {
            data class TestingClass(var foo: String?, var bar: String, var nonComparable: List<String>)
            Grid<TestingClass>().apply {
                columnFor(TestingClass::foo)   // this must compile
                columnFor(TestingClass::bar) {}   // this must compile
                columnFor(TestingClass::nonComparable) {}  // this must compile
            }
        }

        test("sets column by default to sortable") {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
            }
            expectList("fullName") {
                grid.getColumnBy(Person::fullName).getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted }
            }
        }

        test("column header is set properly") {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                columnFor(Person::alive)
                columnFor(Person::dateOfBirth)
            }
            expect("Full Name") { grid.getColumnBy(Person::fullName).header2 }
            expect("Alive") { grid.getColumnBy(Person::alive).header2 }
            expect("Date Of Birth") { grid.getColumnBy(Person::dateOfBirth).header2 }
        }

        test("sorting by column also works with in-memory container") {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        test("sorting by column also works with in-memory container 2") {
            val grid = Grid<Person>().apply {
                columnFor<Person, String>("fullName")
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        test("sorting by column also works with in-memory container 3") {
            val grid = Grid<Person>().apply {
                val fullNameColumn = columnFor(Person::fullName)
                setItems2((0..9).map { Person(fullName = it.toString()) })
                sort(fullNameColumn.desc)
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }
    }

    test("column isExpand") {
        val grid = Grid<Person>()
        val col = grid.columnFor(Person::alive)
        expect(1) { col.flexGrow }  // by default the flexGrow is 1
        val col2 = grid.columnFor(Person::fullName) { isExpand = false }
        expect(0) { col2.flexGrow }
    }


    group("header cell retrieval test") {
        test("one component") {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
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
                columnFor(Person::fullName)
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
                columnFor(Person::fullName)
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
                columnFor(Person::fullName)
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
        Grid<Person>().apply {
            columnFor(Person::fullName)
            appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
        }
        // Kotlin 1.5.0 lambdas are no longer serializable: https://youtrack.jetbrains.com/issue/KT-46373
        // Screw it, it makes no sense to serialize Vaadin components anyway because
        // Vaadin doesn't really support session replication as per
        // https://mvysny.github.io/vaadin-14-session-replication/
//        grid.cloneBySerialization()
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
        test("class") {
            UI.getCurrent().treeGrid(Person::class.java)
            _expectOne<Grid<*>>()
        }
        test("kclass") {
            UI.getCurrent().treeGrid(Person::class)
            _expectOne<Grid<*>>()
        }
        test("null kclass") {
            UI.getCurrent().treeGrid<Person>(klass = null)
            _expectOne<Grid<*>>()
        }
        test("null class") {
            UI.getCurrent().treeGrid<Person>(clazz = null)
            _expectOne<Grid<*>>()
        }
    }
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
