package com.github.karibu.testing

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.expect

class MockVaadinTest {
    @Before
    fun testSetup() {
        MockVaadin.setup()
    }

    @Test
    fun verifyAttachCalled() {
        val attachCalled = AtomicInteger()
        val vl = object : VerticalLayout() {
            override fun onAttach(attachEvent: AttachEvent?) {
                super.onAttach(attachEvent)
                attachCalled.incrementAndGet()
            }
        }
        vl.addAttachListener { attachCalled.incrementAndGet() }
        UI.getCurrent().add(vl)
        expect(2) { attachCalled.get() }
        expect(true) { vl.isAttached }
    }
}
