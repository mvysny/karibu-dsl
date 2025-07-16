package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.avatar
import com.github.mvysny.karibudsl.v10.badge
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.Badge
import com.github.mvysny.kaributools.BadgeVariant
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
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

    @Test fun `dsl syntax test`() {
        UI.getCurrent().card("Lapland", "The Exotic North") {
            headerPrefix { avatar("Lapland") }
            headerSuffix { badge("Arctic", BadgeVariant.SUCCESS) }
            add("Lapland is the northern-most region of Finland and an active outdoor destination.")
            footer {
                button("Book Vacation")
            }
        }
    }

    @Test fun footer() {
        UI.getCurrent().card {
            footer {
                button("Book Vacation")
            }
        }
        _expectOne<Button> { text = "Book Vacation" }
    }

    @Test fun headerPrefix() {
        UI.getCurrent().card {
            headerPrefix {
                avatar("Lapland")
            }
        }
        _expectOne<Avatar>()
    }

    @Test fun headerSuffix() {
        UI.getCurrent().card {
            headerSuffix {
                badge("Arctic", BadgeVariant.SUCCESS)
            }
        }
        _expectOne<Badge>()
    }
}
