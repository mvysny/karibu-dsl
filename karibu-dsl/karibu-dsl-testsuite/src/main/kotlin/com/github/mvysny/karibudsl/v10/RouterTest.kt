package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Route("")
class RootView : VerticalLayout()

abstract class RouterTest {
    @BeforeEach fun setup() {
        MockVaadin.setup(Routes().apply { routes.addAll(listOf(RootView::class.java)) })
    }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().routerLink(VaadinIcon.ABACUS, "Foo") {}
        UI.getCurrent().routerLink(VaadinIcon.ABACUS, "Foo")
        UI.getCurrent().routerLink()
        UI.getCurrent().routerLink {}
    }
}
