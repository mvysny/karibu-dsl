package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import java.lang.IllegalStateException

/**
 * This is also an API test that we can create components based on [Composite]. The components should be final,
 * so that developers prefer [composition over inheritance](https://reactjs.org/docs/composition-vs-inheritance.html).
 */
class MyComposite : KComposite() {
    init {
        ui {
            // create the component UI here; maybe even attach very simple listeners here
            verticalLayout {
                horizontalLayout {
                    button("ok")
                    button("cancel")
                }
            }
        }

        // perform any further initialization here
    }

    // add listener methods here
}

@VaadinDsl
fun (@VaadinDsl HasComponents).myComposite(block: (@VaadinDsl MyComposite).()->Unit = {}) = init(MyComposite(), block)

class CompositeTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("lookup") {
        UI.getCurrent().apply {
            myComposite()
        }
        _get<Button> { caption = "ok" }
    }

    test("empty composite fails when added to the UI") {
        expectThrows(IllegalStateException::class, "The content has not yet been initialized") {
            UI.getCurrent().add(object : KComposite() {})
        }
    }
})
