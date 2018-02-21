package com.github.vok.karibudsl.flow

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.provider.SortDirection
import kotlin.streams.toList

class GridTest : DynaTest({
    test("grid column sort property") {
        val grid = Grid<Person>().apply {
            addColumnFor(Person::fullName)
        }
        expectList("fullName") {
            grid.getColumnByKey(Person::fullName.name).getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted }
        }
    }
})
