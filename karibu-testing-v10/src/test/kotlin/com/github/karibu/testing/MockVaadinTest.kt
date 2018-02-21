package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.github.vok.karibudsl.flow.button
import com.github.vok.karibudsl.flow.text
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.expect

class MockVaadinTest : DynaTest({

    beforeEach { MockVaadin.setup(autoDiscoverViews("com.github")) }

    test("verifyAttachCalled") {
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

    test("Navigation") {
        // no need: when UI is initialized in MockVaadin.setup(), automatic navigation to "" is performed.
//        UI.getCurrent().navigateTo("")
        _get<Text> { text = "Welcome!" }
        UI.getCurrent().navigateTo("helloworld")
        _get<Button> { caption = "Hello, World!" }
    }
})

@Route("helloworld")
class HelloWorldView : VerticalLayout() {
    init {
        button("Hello, World!")
    }
}

@Route("")
class WelcomeView : VerticalLayout() {
    init {
        text("Welcome!")
    }
}

