package com.github.karibu.testing

import com.github.vok.karibudsl.showColumns
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.ui.Grid
import org.junit.Before
import org.junit.Test
import kotlin.test.expect

class GridTest {
    @Before
    fun testSetup() {
        MockVaadin.setup()
    }

    @Test
    fun testSize() {
        expect(20) { ListDataProvider<TestPerson>((0 until 20).map { TestPerson("name $it", it) })._size() }
    }

    @Test
    fun testGet() {
        expect("name 5") { ListDataProvider<TestPerson>((0 until 20).map { TestPerson("name $it", it) })._get(5).name }
    }

    @Test
    fun testDump() {
        val dp = ListDataProvider<TestPerson>((0 until 7).map { TestPerson("name $it", it) })
        val grid = Grid<TestPerson>(TestPerson::class.java)
        grid.removeColumn("name")
        grid.addColumn({ it.name }, { it.toUpperCase() }).apply { id = "name"; caption = "The Name" }
        grid.dataProvider = dp
        grid.showColumns(TestPerson::name, TestPerson::age)
        expect("--[The Name]-[Age]--\n--and 7 more\n") { grid._dump(0 until 0) }
        expect("--[The Name]-[Age]--\n0: NAME 0, 0\n1: NAME 1, 1\n2: NAME 2, 2\n3: NAME 3, 3\n4: NAME 4, 4\n--and 2 more\n") { grid._dump(0 until 5) }
        expect("--[The Name]-[Age]--\n0: NAME 0, 0\n1: NAME 1, 1\n2: NAME 2, 2\n3: NAME 3, 3\n4: NAME 4, 4\n5: NAME 5, 5\n6: NAME 6, 6\n") { grid._dump(0 until 20) }
    }

    @Test
    fun testExpectRow() {
        val grid = Grid<TestPerson>(TestPerson::class.java)
        grid.dataProvider = ListDataProvider<TestPerson>((0 until 7).map { TestPerson("name $it", it) })
        grid.expectRows(7)
        grid.expectRow(0, "name 0", "0")
    }
}

data class TestPerson(val name: String, val age: Int)
