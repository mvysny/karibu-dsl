package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.expect

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
                onClick { okClicked() }
            }
            button("cancel") {
                onClick { cancelClicked() }
            }
        }
    }

    init {
        // perform any further initialization here
    }

    // listener methods here
    private fun okClicked() {}
    private fun cancelClicked() {}
}

@VaadinDsl
fun (@VaadinDsl HasComponents).buttonBar(block: (@VaadinDsl ButtonBar).()->Unit = {}) = init(ButtonBar(), block)

class MyButton : KComposite(Button("Click me!"))

/**
 * Demoes the possibility of overriding [initContent].
 */
class MyComponent : KComposite() {
    private val content = Button("Click me!")
    override fun initContent(): Component {
        return content
    }
}

abstract class KcompositeTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun lookup() {
        UI.getCurrent().apply {
            buttonBar()
        }
        _expectOne<ButtonBar>()
        _expectOne<Button> { text = "ok" }
    }

    @Test fun `uninitialized composite fails with informative exception`() {
        val ex = assertThrows<IllegalStateException> {
            UI.getCurrent().add(object : KComposite() {})
        }
        expect("The content has not yet been initialized") { ex.message }
    }

    @Test fun `provide contents in constructor`() {
        UI.getCurrent().add(MyButton())
        _expectOne<MyButton>()
        _expectOne<Button> { text = "Click me!" }
    }

    @Test fun `provide contents in initContent()`() {
        UI.getCurrent().add(MyComponent())
        _expectOne<Button> { text = "Click me!" }
    }
}
