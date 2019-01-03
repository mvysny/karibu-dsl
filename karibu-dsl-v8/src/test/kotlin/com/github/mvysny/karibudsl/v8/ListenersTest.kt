package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.dynatest.serializeToBytes
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.vaadin.ui.Button
import com.vaadin.ui.Image
import java.io.Serializable

class ListenersTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("buttonListenerSerializable") {
        ({ println("foo") } as Serializable).cloneBySerialization()
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
