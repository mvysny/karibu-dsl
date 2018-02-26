package com.github.karibu.testing

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Div
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

    test("open dialog") {
        // there should be no dialogs in the UI
        _expectNone<Dialog>()
        _expectNone<Div> { text = "Dialog Text" }
        val dialog = Dialog(Div().apply { text("Dialog Text") })
        dialog.open()
        _get<Dialog>()  // should be in the UI, along with its contents
        _get<Div> { text = "Dialog Text" }
        dialog.close()
        // there should be no dialogs in the UI
        _expectNone<Div> { text = "Dialog Text" }
        _expectNone<Dialog>()
    }

    test("the dialogs must be cleared up from the component tree on close") {
        val dialog = Dialog(Div().apply { text("Dialog Text") })
        dialog.open()
        dialog.close()
        cleanupDialogs()
        expect("""
└── MockedUI[]
    └── WelcomeView[]
        └── Text[text='Welcome!']
""".trim()) { UI.getCurrent().toPrettyTree().trim() }
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
