package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8.MockVaadin
import com.github.mvysny.kaributesting.v8._get
import com.vaadin.ui.Button
import com.vaadin.ui.Composite
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI

/**
 * This is also an API test that we can create components based on [Composite]. The components should be final,
 * so that developers prefer [composition over inheritance](https://reactjs.org/docs/composition-vs-inheritance.html).
 */
class ButtonBar : Composite() {
    init {
        // create the component UI here; maybe even attach very simple listeners here
        horizontalLayout {
            button("ok") {
                onLeftClick { okClicked() }
            }
            button("cancel") {
                onLeftClick { cancelClicked() }
            }
        }

        // perform any further initialization here
    }

    // add listener methods here
    private fun okClicked() {}
    private fun cancelClicked() {}
}

@VaadinDsl
fun (@VaadinDsl HasComponents).buttonBar(block: (@VaadinDsl ButtonBar).() -> Unit = {}) = init(ButtonBar(), block)

class CompositeTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("lookup") {
        UI.getCurrent().apply {
            buttonBar()
        }
        _get<Button> { caption = "ok" }
    }
})
