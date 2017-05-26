package com.github.vok.karibudsl

import com.vaadin.ui.Button
import com.vaadin.ui.Image
import org.junit.Before
import org.junit.Test

class ListenersTest {
    @Before
    fun setupVaadin() = MockVaadin.setup()

    @Test
    fun buttonListenerSerializable() {
        { println("foo") }.serializeDeserialize()()
        Button().apply {
            onLeftClick { println("bla") }
        }.serializeToBytes()
    }

    @Test
    fun imageListenerSerializable() {
        Image().apply {
            onLeftClick { println("bla") }
        }.serializeToBytes()
    }
}
