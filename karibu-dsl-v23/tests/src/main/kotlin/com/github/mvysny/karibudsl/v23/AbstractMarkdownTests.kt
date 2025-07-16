package com.github.mvysny.karibudsl.v23

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.markdown.Markdown
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class AbstractMarkdownTests {
    init {
        Assumptions.assumeTrue(VaadinVersion.get.isAtLeast(24, 8))
    }
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().markdown()
        _expectOne<Markdown>()
        UI.getCurrent().markdown{}
        UI.getCurrent().markdown("Foo")
        UI.getCurrent().markdown("Foo"){}
    }
}