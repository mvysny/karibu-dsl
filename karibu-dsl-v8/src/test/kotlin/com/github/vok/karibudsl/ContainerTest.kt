package com.github.vok.karibudsl

import com.vaadin.ui.*
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import kotlin.test.expect

class ContainerTest {

    @Before
    fun setupVaadin() = MockVaadin.setup()

    private fun <T: HasComponents> T.dummyContent(size: Int): T {
        (0..size - 1).forEach { label("$it") }
        return this
    }

    @Test
    fun testComponentContainerIndices() {
        Assume.assumeFalse(AbsoluteLayout() is AbstractOrderedLayout)
        Assume.assumeTrue(VerticalLayout() is AbstractOrderedLayout)
        fun testComponentContainerIndices(container: ()-> ComponentContainer) {
            expect(0..-1) { container().indices }
            expect(0..99) { container().dummyContent(100).indices }
            expect(0..9) { container().dummyContent(10).indices }
            expect(0..19) { container().dummyContent(20).indices }
        }

        testComponentContainerIndices { CssLayout() }
        testComponentContainerIndices { AbsoluteLayout() }
        testComponentContainerIndices { VerticalLayout() }
    }


    @Test
    fun testComponentContainerGetComponentAt() {
        fun testComponentContainerGetComponentAt(container: ()-> ComponentContainer) {
            expect("25") { (container().dummyContent(100).getComponentAt(25) as Label).value }
            expect("0") { (container().dummyContent(100).getComponentAt(0) as Label).value }
            expect("99") { (container().dummyContent(100).getComponentAt(99) as Label).value }
        }

        testComponentContainerGetComponentAt { CssLayout() }
        testComponentContainerGetComponentAt { AbsoluteLayout() }
        testComponentContainerGetComponentAt { VerticalLayout() }
    }

    @Test
    fun testComponentContainerRemoveComponentAt() {
        fun ComponentContainer.join() = filterIsInstance<Label>().joinToString("", transform = { it.value })
        fun testComponentContainerRemoveComponentAt(containre: ()-> ComponentContainer) {
            expect("1234") {
                containre().dummyContent(5).apply {
                    removeComponentAt(0)
                }.join()
            }
            expect("0134") {
                containre().dummyContent(5).apply {
                    removeComponentAt(2)
                }.join()
            }
            expect("0123") {
                containre().dummyContent(5).apply {
                    removeComponentAt(4)
                }.join()
            }
        }

        testComponentContainerRemoveComponentAt { CssLayout() }
        testComponentContainerRemoveComponentAt { AbsoluteLayout() }
        testComponentContainerRemoveComponentAt { VerticalLayout() }
    }

    @Test
    fun testComponentContainerRemoveComponentsAt() {
        fun ComponentContainer.join() = filterIsInstance<Label>().joinToString("", transform = { it.value })
        fun testComponentContainerRemoveComponentsAt(container: ()-> ComponentContainer) {
            expect("01234") {
                container().dummyContent(5).apply {
                    removeComponentsAt(0..-1)
                }.join()
            }
            expect("1234") {
                container().dummyContent(5).apply {
                    removeComponentsAt(0..0)
                }.join()
            }
            expect("234") {
                container().dummyContent(5).apply {
                    removeComponentsAt(0..1)
                }.join()
            }
            expect("") {
                container().dummyContent(5).apply {
                    removeComponentsAt(0..4)
                }.join()
            }
            expect("04") {
                container().dummyContent(5).apply {
                    removeComponentsAt(1..3)
                }.join()
            }
            expect("012") {
                container().dummyContent(5).apply {
                    removeComponentsAt(3..4)
                }.join()
            }
        }

        testComponentContainerRemoveComponentsAt { CssLayout() }
        testComponentContainerRemoveComponentsAt { AbsoluteLayout() }
        testComponentContainerRemoveComponentsAt { VerticalLayout() }
    }
}
