package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class HtmlTest() {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent()!!.apply {
            div()
            h1()
            h2()
            h3()
            h4()
            h5()
            h6()
            hr()
            p()
            em()
            span()
            anchor()
            anchor(StreamResource("foo.txt", InputStreamFactory { "foo".byteInputStream() }))
            image()
            image(StreamResource("foo.txt", InputStreamFactory { "foo".byteInputStream() }))
            @Suppress("DEPRECATION")
            label()
            nativeLabel()
            input()
            nativeButton()
            strong()
            br()
            ol()
            li()
            iframe()
            article()
            aside()
            dl()
            dd()
            dt()
            pre()
            ul()
        }
    }

    @Test fun `div class names`() {
        var layout: Div = UI.getCurrent().div()
        expect(null) { layout.className }
        layout = UI.getCurrent().div("foo bar foo")
        expect("foo bar") { layout.className }
    }
}
