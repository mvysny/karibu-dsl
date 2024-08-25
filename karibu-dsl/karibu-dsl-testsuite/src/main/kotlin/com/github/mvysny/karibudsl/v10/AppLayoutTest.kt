package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

open class AppLayoutTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        val a = UI.getCurrent().appLayout {
            navbar {
                drawerToggle()
                h3("My App")
            }
            drawer {
                routerLink(VaadinIcon.ARCHIVE, "Foo")
                routerLink(VaadinIcon.ACADEMY_CAP, "Bar")
            }
            content {
                span("Hello world!")
            }
        }
        a.cloneBySerialization()
    }
}
