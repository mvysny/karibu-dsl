package com.github.karibu.testing

import com.vaadin.data.provider.ListDataProvider
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
        expect(20) { ListDataProvider<TestPerson>((0 until 20).map { TestPerson("name $it") })._size() }
    }

    @Test
    fun testGet() {
        expect("name 5") { ListDataProvider<TestPerson>((0 until 20).map { TestPerson("name $it") })._get(5).name }
    }
}

data class TestPerson(val name: String)
