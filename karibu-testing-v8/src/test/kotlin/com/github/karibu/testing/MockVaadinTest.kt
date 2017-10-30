package com.github.karibu.testing

import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.expect

class MockVaadinTest {
    @Test
    fun testSetup() {
        MockVaadin.setup()
    }

    @Test
    fun verifyAttachCalled() {
        val attachCalled = AtomicInteger()
        val vl = object : VerticalLayout() {
            override fun attach() {
                super.attach()
                attachCalled.incrementAndGet()
            }
        }
        vl.addAttachListener { attachCalled.incrementAndGet() }
        UI.getCurrent().content = vl
        expect(2) { attachCalled.get() }
        expect(true) { vl.isAttached }
    }
}
