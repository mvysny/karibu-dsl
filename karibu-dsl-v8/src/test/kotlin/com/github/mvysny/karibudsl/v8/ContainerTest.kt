package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.dynatest.expectThrows
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.vaadin.ui.*
import java.lang.IllegalStateException
import kotlin.test.expect

class ContainerTest : DynaTest({

    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("prerequisites") {
        expect(false) { AbsoluteLayout() is AbstractOrderedLayout }
        expect(true) { VerticalLayout() is AbstractOrderedLayout }
    }

    containerBattery(CssLayout::class.java)
    containerBattery(AbsoluteLayout::class.java)
    containerBattery(VerticalLayout::class.java)

    group("expanded") {
        lateinit var container: VerticalLayout
        beforeEach { container = VerticalLayout() }
        test("not expanded by default") {
            expect(false) { container.label().isExpanded }
            expect(0f) { container.label().expandRatio }
        }
        test("set expanded") {
            val l = container.label { isExpanded = true }
            expect(true) { l.isExpanded }
            expect(1f) { l.expandRatio }
        }
        test("isExpanded fails to be set in a FormLayout") {
            expectThrows(IllegalStateException::class, "Setting expandRatio of a component nested inside of a FormLayout does nothing") {
                FormLayout().apply { label { isExpanded = true } }
            }
        }
    }

    group("removeChild") {
        test("from Window") {
            val w = Window("foo", Label("bar"))
            w.removeChild(Label("other"))
            expect("bar") { (w.content as Label).value }
            w.removeChild(w.content)
            expect(null) { w.content }
        }
        test("from VerticalSplitPanel") {
            val p = VerticalSplitPanel().apply { label("first"); label("second") }
            p.removeChild(Label("third"))
            expect(2) { p.componentCount }
            p.removeChild(p.secondComponent)
            expect(1) { p.componentCount }
            p.removeChild(p.firstComponent)
            expect(0) { p.componentCount }
        }
    }

    group("removeAllComponents()") {
        test("from Window") {
            val w = Window("foo", Label("bar"))
            w.removeAllComponents()
            expect(null) { w.content }
        }
        test("from VerticalSplitPanel") {
            val p = VerticalSplitPanel().apply { label("first"); label("second") }
            p.removeAllComponents()
            expect(0) { p.componentCount }
        }
    }

    group("removeFromParent") {
        test("with null parent") {
            val l = Label("foo")
            l.removeFromParent()
            expect(null) { l.parent }
        }
        test("from window") {
            val w = Window("foo", Label("bar"))
            w.content.removeFromParent()
            expect(null) { w.content }
        }
    }

    group("getComponentCount()") {
        test("window") {
            val w = Window("foo", Label("bar"))
            expect(1) { (w as HasComponents).getComponentCount() }
            w.removeAllComponents()
            expect(0) { (w as HasComponents).getComponentCount() }
        }
        test("composite") {
            var w = Composite(Label("bar"))
            expect(1) { (w as HasComponents).getComponentCount() }
            w = Composite()
            expect(0) { (w as HasComponents).getComponentCount() }
        }
    }

    group("isEmpty") {
        test("window") {
            val w = Window("foo", Label("bar"))
            expect(false) { (w as HasComponents).isEmpty }
            w.removeAllComponents()
            expect(true) { (w as HasComponents).isEmpty }
        }
        test("composite") {
            var w = Composite(Label("bar"))
            expect(false) { (w as HasComponents).isEmpty }
            w = Composite()
            expect(true) { (w as HasComponents).isEmpty }
        }
    }
})

fun DynaNodeGroup.containerBattery(containerClazz: Class<out ComponentContainer>) {
    fun <T: HasComponents> T.dummyContent(size: Int): T {
        (0 until size).forEach { label("$it") }
        return this
    }

    lateinit var container: ComponentContainer
    beforeEach { container = containerClazz.newInstance() }

    group("test ${containerClazz.simpleName}") {

        group("indices") {
            test("empty") { expectList() { container.indices.toList() } }
            test("100 children") { expect(0..99) { container.dummyContent(100).indices } }
            test("10 children") { expect(0..9) { container.dummyContent(10).indices } }
            test("20 children") { expect(0..19) { container.dummyContent(20).indices } }
        }

        test("test getComponentAt()") {
            container.dummyContent(100)
            expect("25") { (container.getComponentAt(25) as Label).value }
            expect("0") { (container.getComponentAt(0) as Label).value }
            expect("99") { (container.getComponentAt(99) as Label).value }
        }

        group("removeComponentAt()") {
            fun ComponentContainer.join() = filterIsInstance<Label>().joinToString("", transform = { it.value })
            test("remove first") {
                expect("1234") {
                    container.dummyContent(5).apply {
                        removeComponentAt(0)
                    }.join()
                }
            }
            test("remove middle") {
                expect("0134") {
                    container.dummyContent(5).apply {
                        removeComponentAt(2)
                    }.join()
                }
            }
            test("remove middle via removeChild") {
                expect("0134") {
                    container.dummyContent(5).apply {
                        removeChild(getComponentAt(2))
                    }.join()
                }
            }
            test("remove of a component nested in another layout does nothing") {
                val l = VerticalLayout().run { label("2") }
                expect(true) { l.parent != null }
                expect("01234") {
                    container.dummyContent(5).apply {
                        removeChild(l)
                    }.join()
                }
            }
            test("remove last") {
                expect("0123") {
                    container.dummyContent(5).apply {
                        removeComponentAt(4)
                    }.join()
                }
            }
        }

        group("removeComponentsAt()") {
            fun ComponentContainer.join() = filterIsInstance<Label>().joinToString("", transform = { it.value })

            test("remove empty range") {
                expect("01234") {
                    container.dummyContent(5).apply {
                        removeComponentsAt(0..-1)
                    }.join()
                }
            }
            test("remove first") {
                expect("1234") {
                    container.dummyContent(5).apply {
                        removeComponentsAt(0..0)
                    }.join()
                }
            }
            test("remove first two") {
                expect("234") {
                    container.dummyContent(5).apply {
                        removeComponentsAt(0..1)
                    }.join()
                }
            }
            test("remove all") {
                expect("") {
                    container.dummyContent(5).apply {
                        removeComponentsAt(0..4)
                    }.join()
                }
            }
            test("remove middle three") {
                expect("04") {
                    container.dummyContent(5).apply {
                        removeComponentsAt(1..3)
                    }.join()
                }
            }
            test("remove last two") {
                expect("012") {
                    container.dummyContent(5).apply {
                        removeComponentsAt(3..4)
                    }.join()
                }
            }
        }

        group("removeAllComponents()") {
            fun ComponentContainer.join() = filterIsInstance<Label>().joinToString("", transform = { it.value })

            test("remove all on already empty does nothing") {
                expect("") {
                    container.apply {
                        removeAllComponents()
                    }.join()
                }
            }
            test("remove all") {
                expect("") {
                    container.dummyContent(5).apply {
                        removeAllComponents()
                    }.join()
                }
            }
        }

        group("getComponentCount()") {
            lateinit var l: HasComponents
            beforeEach { l = container }

            test("empty") { expect(0) { l.getComponentCount() } }
            test("100 children") { expect(100) { l.dummyContent(100).getComponentCount() } }
            test("10 children") { expect(10) { l.dummyContent(10).getComponentCount() } }
            test("20 children") { expect(20) { l.dummyContent(20).getComponentCount() } }
        }

        group("isEmpty") {
            lateinit var l: HasComponents
            beforeEach { l = container }

            test("empty") { expect(true) { l.isEmpty } }
            test("100 children") { expect(false) { l.dummyContent(100).isEmpty } }
            test("10 children") { expect(false) { l.dummyContent(10).isEmpty } }
            test("20 children") { expect(false) { l.dummyContent(1).isEmpty } }
        }
    }
}
