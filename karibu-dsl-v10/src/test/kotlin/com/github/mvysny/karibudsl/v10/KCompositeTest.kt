package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.cloneBySerialization
import com.github.mvysny.dynatest.expectThrows
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import java.lang.IllegalStateException

/**
 * This is also an API test that we can create components based on [KComposite]. The components should be final,
 * so that developers prefer [composition over inheritance](https://reactjs.org/docs/composition-vs-inheritance.html).
 */
class ButtonBar : KComposite() {
    private lateinit var okButton: Button
    private val root = ui {
        // create the component UI here; maybe even attach very simple listeners here
        horizontalLayout {
            okButton = button("ok") {
                onLeftClick { okClicked() }
            }
            button("cancel") {
                onLeftClick { cancelClicked() }
            }
        }
    }

    init {
        // perform any further initialization here
    }

    // add listener methods here
    private fun okClicked() {}
    private fun cancelClicked() {}
}

@VaadinDsl
fun (@VaadinDsl HasComponents).buttonBar(block: (@VaadinDsl ButtonBar).()->Unit = {}) = init(ButtonBar(), block)

class KCompositeTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("lookup") {
        UI.getCurrent().apply {
            buttonBar()
        }
        _get<Button> { caption = "ok" }
    }

    test("serialize") {
        UI.getCurrent().apply {
            buttonBar()
        }
        UI.getCurrent().cloneBySerialization()
    }

    test("uninitialized composite fails with informative exception") {
        expectThrows(IllegalStateException::class, "The content has not yet been initialized") {
            UI.getCurrent().add(object : KComposite() {})
        }
    }
})
