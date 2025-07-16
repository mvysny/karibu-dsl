package com.github.mvysny.karibudsl.v23

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.card.Card
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class AbstractCardTests {
    init {
        Assumptions.assumeTrue(VaadinVersion.get.isAtLeast(24, 8))
    }
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().card("Lapland", "The Exotic North")
        _expectOne<Card>()
        UI.getCurrent().card()
        UI.getCurrent().card{}
        UI.getCurrent().card(){}
        UI.getCurrent().card("Lapland"){}
        UI.getCurrent().card("Lapland")
        UI.getCurrent().card("Lapland", "The Exotic North") {
            add("Lapland is the northern-most region of Finland and an active outdoor destination.")
        }
    }
}