package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route("")
class RootView : VerticalLayout()

@DynaTestDsl
fun DynaNodeGroup.routerTest() {
    beforeEach {
        MockVaadin.setup(Routes().apply { routes.addAll(listOf(RootView::class.java)) })
    }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().routerLink(VaadinIcon.ABACUS, "Foo") {}
        UI.getCurrent().routerLink(VaadinIcon.ABACUS, "Foo")
        UI.getCurrent().routerLink()
        UI.getCurrent().routerLink {}
    }
}
