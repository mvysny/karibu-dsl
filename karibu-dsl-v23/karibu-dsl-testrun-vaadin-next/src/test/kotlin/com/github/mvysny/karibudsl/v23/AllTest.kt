package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.AllTests
import com.github.mvysny.karibudsl.v10.div
import com.github.mvysny.karibudsl.v10.h1
import com.github.mvysny.karibudsl.v10.h2
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

class AllTest {
    @Test fun `vaadin version`() {
        expect(24) { VaadinVersion.get.major }
        expect(9) { VaadinVersion.get.minor }
    }

    @Nested inner class AllTest : AllTests()
    @Nested inner class AllTest24 : AllTests24()
    @Nested inner class HorizontalLayoutV24_7Tests {
        @BeforeEach fun setup() { MockVaadin.setup() }
        @AfterEach fun teardown() { MockVaadin.tearDown() }
        @Test fun addToStart() {
            val h = HorizontalLayout()
            lateinit var d: Div
            h.start {
                d = div()
            }
            expect(h) { d.parent.get() }
            expect(null) { d.element.getAttribute("slot") }
        }
        @Test fun addToMiddle() {
            val h = HorizontalLayout()
            lateinit var d: Div
            h.middle {
                d = div()
            }
            expect(h) { d.parent.get() }
            expect("middle") { d.element.getAttribute("slot") }
        }
        @Test fun addToEnd() {
            val h = HorizontalLayout()
            lateinit var d: Div
            h.end {
                d = div()
            }
            expect(h) { d.parent.get() }
            expect("end") { d.element.getAttribute("slot") }
        }
        @Test fun ordering() {
            val h = HorizontalLayout().apply {
                end { h3() }
                middle { h2() }
                start { h1() }
            }
            expect(listOf(H1::class.java, H2::class.java, H3::class.java)) {
                h.children.toList().map { it.javaClass as Class<*> }
            }
        }
    }
}
