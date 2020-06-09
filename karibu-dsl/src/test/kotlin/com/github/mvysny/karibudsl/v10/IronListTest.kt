package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.ironlist.IronList

class IronListTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    group("iron list dsl") {
        test("basic properties") {
            val il: IronList<String> = UI.getCurrent().ironList<String>()
            // todo assert that it has 0 items. Need support from Karibu-Testing
        }
    }

    test("serialization") {
        val il = IronList<Person>().apply {
            setRenderer { it.toString() }
        }
        il.cloneBySerialization()
    }
})
