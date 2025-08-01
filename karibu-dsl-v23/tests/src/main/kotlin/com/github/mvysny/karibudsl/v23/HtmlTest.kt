package com.github.mvysny.karibudsl.v23

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.SemanticVersion
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.RangeInput
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class HtmlTest {
    init {
        Assumptions.assumeTrue(VaadinVersion.get >= SemanticVersion(24, 3, 0, "alpha6"))
    }
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().apply {
            rangeInput()
        }
        _expectOne<RangeInput>()
    }
}
