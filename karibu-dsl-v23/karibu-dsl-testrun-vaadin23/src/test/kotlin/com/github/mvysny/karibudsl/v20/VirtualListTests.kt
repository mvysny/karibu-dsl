package com.github.mvysny.karibudsl.v20

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.karibudsl.v23.virtualList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.virtuallist.VirtualList
import com.vaadin.flow.data.provider.ListDataProvider

@DynaTestDsl
fun DynaNodeGroup.virtualListTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("dsl syntax test") {
        UI.getCurrent().virtualList<String>()
        UI.getCurrent().virtualList<String> {
            setItems("a", "b", "c")
        }
        UI.getCurrent().virtualList<String>(ListDataProvider(listOf()))
        UI.getCurrent().virtualList<String>(ListDataProvider(listOf())) {  }
    }

    test("smoke") {
        UI.getCurrent().virtualList<String>()
        _expectOne<VirtualList<*>>()
    }
}
