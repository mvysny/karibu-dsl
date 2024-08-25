package com.github.mvysny.karibudsl.v20

import com.github.mvysny.karibudsl.v23.fontIcon
import com.github.mvysny.karibudsl.v23.svgIcon
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class IconTest {
    init {
        Assumptions.assumeTrue(VaadinVersion.get.isAtLeast(24, 2))
    }
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().apply {
            svgIcon {}
            svgIcon()
            svgIcon("foo") {}
            fontIcon()
            fontIcon {}
            fontIcon("fa-solid", "fa-user")
            fontIcon("fa-solid", "fa-user") {}
        }
    }
}
