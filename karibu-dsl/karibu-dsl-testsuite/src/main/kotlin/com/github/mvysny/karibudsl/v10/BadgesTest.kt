package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.Badge
import com.github.mvysny.kaributools.BadgeVariant
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class BadgesTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun tearDown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().badge("Hello") {
            addThemeVariants(BadgeVariant.PILL, BadgeVariant.SMALL)
        }
        _expectOne<Badge>()
    }
}
