package com.github.vok.karibudsl.flow

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing.header2
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.provider.SortDirection
import kotlin.streams.toList
import kotlin.test.expect

class GridTest : DynaTest({
    beforeEach { MockVaadin.setup(setOf()) }

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
    }
})
