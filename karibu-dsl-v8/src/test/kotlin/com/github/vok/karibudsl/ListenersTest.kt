package com.github.vok.karibudsl

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.ui.Button
import com.vaadin.ui.Image

class ListenersTest : DynaTest({
    beforeEach { MockVaadin.setup() }

    test("buttonListenerSerializable") {
        { println("foo") }.serializeDeserialize()()
        Button().apply {
            onLeftClick { println("bla") }
        }.serializeToBytes()
    }

    test("imageListenerSerializable") {
        Image().apply {
            onLeftClick { println("bla") }
        }.serializeToBytes()
    }
})
