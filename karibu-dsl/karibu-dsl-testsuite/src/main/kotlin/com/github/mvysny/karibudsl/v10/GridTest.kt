package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributesting.v10.getCell
import com.github.mvysny.kaributools.*
import com.github.mvysny.kaributools.sort
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class GridTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Nested inner class GridDsl {
        @Test fun simple() {
            UI.getCurrent().grid<Person>()
            _expectOne<Grid<*>>()
        }
        @Test fun basicProperties() {
            val grid: Grid<String> = UI.getCurrent().grid<String>()
            // TODO commented out until the Grid row situation is resolved. See [Grid.hotfixMissingHeaderRow] for more details.
            //expect(1) { grid.headerRows.size }
            expect(0) { grid.columns.size }
            expect(String::class.java) { grid.beanType }
        }
        @Test fun withGenerifiedItem() {
            data class TestingClass<T>(var item: T? = null)
            UI.getCurrent().grid<TestingClass<String>>()
        }
        @Test fun clazz() {
            UI.getCurrent().grid(Person::class.java)
            _expectOne<Grid<*>>()
        }
        @Test fun kclass() {
            UI.getCurrent().grid(Person::class)
            _expectOne<Grid<*>>()
        }
        @Test fun nullKclass() {
            UI.getCurrent().grid<Person>(klass = null)
            _expectOne<Grid<*>>()
        }
        @Test fun nullClass() {
            UI.getCurrent().grid<Person>(clazz = null)
            _expectOne<Grid<*>>()
        }
    }

    @Nested inner class `columnFor() tests` {
        @Test fun `grid columnFor() works both for nullable and non-null properties`() {
            data class TestingClass(var foo: String?, var bar: String, var nonComparable: List<String>)
            Grid<TestingClass>().apply {
                columnFor(TestingClass::foo)   // this must compile
                columnFor(TestingClass::bar) {}   // this must compile
                columnFor(TestingClass::nonComparable) {}  // this must compile
            }
        }

        @Test fun `sets column by default to sortable`() {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
            }
            expectList("fullName") {
                grid.getColumnBy(Person::fullName).getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted }
            }
        }

        @Test fun `column header is set properly`() {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                columnFor(Person::alive)
                columnFor(Person::dateOfBirth)
            }
            expect("Full Name") { grid.getColumnBy(Person::fullName).header2 }
            expect("Alive") { grid.getColumnBy(Person::alive).header2 }
            expect("Date Of Birth") { grid.getColumnBy(Person::dateOfBirth).header2 }
        }

        @Test fun `sorting by column also works with in-memory container`() {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        @Test fun `sorting by column also works with in-memory container 2`() {
            val grid = Grid<Person>().apply {
                columnFor<Person, String>("fullName")
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        @Test fun `sorting by column also works with in-memory container 3`() {
            val grid = Grid<Person>().apply {
                val fullNameColumn = columnFor(Person::fullName)
                setItems2((0..9).map { Person(fullName = it.toString()) })
                sort(fullNameColumn.desc)
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        @Test fun `custom key`() {
            val grid = Grid<Person>()
            expect("mykey") { grid.columnFor(Person::fullName, key = "mykey") {} .key }
            expect("mykey2") { grid.columnFor(Person::fullName, key = "mykey2", converter = { it }) {} .key }
            expect("mykey3") { grid.columnFor("fullName", key = "mykey3", converter = { it: String? -> "" }) {} .key }
        }
    }

    @Test fun `column isExpand`() {
        val grid = Grid<Person>()
        val col = grid.columnFor(Person::alive)
        expect(1) { col.flexGrow }  // by default the flexGrow is 1
        val col2 = grid.columnFor(Person::fullName) { isExpand = false }
        expect(0) { col2.flexGrow }
    }

    @Nested inner class `componentColumn tests` {
        @Test fun smoke() {
            val grid = Grid<String>()
            grid.componentColumn({ span("hello $it") })
            grid.setItems("a")
            grid.expectRows(1)
            grid.expectRow(0, "Span[text='hello a']")
        }
        @Test fun nullComponents() {
            val grid = Grid<String>()
            grid.componentColumn({ if(false) span("hello $it") else null })
            grid.setItems("a")
            grid.expectRows(1)
            grid.expectRow(0, "")
        }
    }

    @Nested inner class `header cell retrieval test`() {
        @Test fun `one component`() {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                appendHeaderRow().getCell(Person::fullName).component = TextField("Foo!")
            }
            expect("Foo!") {
                val tf = grid.headerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
            grid.headerRows.last().getCell(Person::fullName).component = null
            expect(null) { grid.headerRows.last().getCell(Person::fullName).component }
        }
        @Test fun twoComponents() {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                appendHeaderRow().getCell(Person::fullName).component = TextField("Foo!")
                appendHeaderRow().getCell(Person::fullName).component = TextField("Bar!")
            }
            expect("Bar!") {
                val tf = grid.headerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
        }
    }

    @Nested inner class `footer cell retrieval test`() {
        @Test fun oneComponent() {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
            }
            expect("Foo!") {
                val tf = grid.footerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
            grid.footerRows.last().getCell(Person::fullName).component = null
            expect(null) { grid.footerRows.last().getCell(Person::fullName).component }
        }
        @Test fun twoComponents() {
            val grid = Grid<Person>().apply {
                columnFor(Person::fullName)
                appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
                appendFooterRow().getCell(Person::fullName).component = TextField("Bar!")
            }
            expect("Bar!") {
                val tf = grid.footerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
        }
    }

    @Test fun serialization() {
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

    @Nested inner class treeGrid {
        @Test fun smoke() {
            UI.getCurrent().treeGrid<String>()
            _expectOne<TreeGrid<*>>()
        }
        @Test fun basicProperties() {
            val grid: TreeGrid<String> = UI.getCurrent().treeGrid<String>()
            expect(1) { grid.headerRows.size }
            expect(0) { grid.columns.size }
            expect(String::class.java) { grid.beanType }
        }
        @Test fun `class`() {
            UI.getCurrent().treeGrid(Person::class.java)
            _expectOne<Grid<*>>()
        }
        @Test fun kclass() {
            UI.getCurrent().treeGrid(Person::class)
            _expectOne<Grid<*>>()
        }
        @Test fun nullKclass() {
            UI.getCurrent().treeGrid<Person>(klass = null)
            _expectOne<Grid<*>>()
        }
        @Test fun nullClass() {
            UI.getCurrent().treeGrid<Person>(clazz = null)
            _expectOne<Grid<*>>()
        }
        @Test fun dsl() {
            UI.getCurrent().treeGrid<Person> {
                hierarchyColumnFor(Person::fullName)   // this must compile
                hierarchyColumnFor(Person::alive) {}   // this must compile
                hierarchyColumnFor(Person::testCalendar) {}  // this must compile
                hierarchyColumnFor<Person, String>("created")   // this must compile
                hierarchyColumnFor<Person, String>("testInt") {}   // this must compile
                componentHierarchyColumn({ Span(it.fullName) }) {}
                componentHierarchyColumn({ Span(it.fullName) })
            }
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
