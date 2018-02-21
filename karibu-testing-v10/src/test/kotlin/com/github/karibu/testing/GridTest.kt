package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.provider.ListDataProvider
import kotlin.test.expect

class GridTest : DynaTest({

    beforeEach { MockVaadin.setup()  }

    test("_size") {
        expect(20) { ListDataProvider<TestPerson>((0 until 20).map { TestPerson("name $it", it) })._size() }
    }

    test("_get") {
        expect("name 5") { ListDataProvider<TestPerson>((0 until 20).map { TestPerson("name $it", it) })._get(5).name }
    }

    test("_dump") {
        val dp = ListDataProvider<TestPerson>((0 until 7).map { TestPerson("name $it", it) })
        val grid = Grid<TestPerson>(TestPerson::class.java)
        grid.dataProvider = dp
        expect("--[Name]-[Age]--\n--and 7 more\n") { grid._dump(0 until 0) }
        expect("--[Name]-[Age]--\n0: name 0, 0\n1: name 1, 1\n2: name 2, 2\n3: name 3, 3\n4: name 4, 4\n--and 2 more\n") { grid._dump(0 until 5) }
        expect("--[Name]-[Age]--\n0: name 0, 0\n1: name 1, 1\n2: name 2, 2\n3: name 3, 3\n4: name 4, 4\n5: name 5, 5\n6: name 6, 6\n") { grid._dump(0 until 20) }
    }

    test("expectRow()") {
        val grid = Grid<TestPerson>(TestPerson::class.java)
        grid.dataProvider = ListDataProvider<TestPerson>((0 until 7).map { TestPerson("name $it", it) })
        grid.expectRows(7)
        grid.expectRow(0, "name 0", "0")
    }
})

data class TestPerson(val name: String, val age: Int)
