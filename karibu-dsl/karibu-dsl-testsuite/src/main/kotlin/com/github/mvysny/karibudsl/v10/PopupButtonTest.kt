package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.menubar.MenuBarVariant
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class PopupButtonTest() {
    @BeforeEach
    fun setup() { MockVaadin.setup() }
    @AfterEach
    fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().popupButton("foo") {
            content {
                button("Hello!")
            }
        }
        _get<PopupButton>()
        _get<Button> { text = "Hello!" }
    }

    @Test fun `content keeps its width and height`() {
        UI.getCurrent().popupButton("foo") {
            content {
                verticalLayout { width = "200px"; height = "300px" }
            }
        }
        expect("200px") { _get<VerticalLayout>().width }
        expect("300px") { _get<VerticalLayout>().height }
    }

    @Test fun caption() {
        val btn = UI.getCurrent().popupButton("foo")
        expect("foo") { btn.caption }
        btn.caption = "bar"
        expect("bar") { btn.caption }
    }

    @Test fun `setPopupContent replaces the previous content`() {
        val btn = UI.getCurrent().popupButton("foo")
        btn.setPopupContent(Button("First"))
        _get<Button> { text = "First" }
        btn.setPopupContent(Button("Second"))
        _expectNone<Button> { text = "First" }
        _get<Button> { text = "Second" }
    }

    @Test fun `content replaces the previous content`() {
        val btn = UI.getCurrent().popupButton("foo") {
            content { button("First") }
        }
        btn.content { button("Second") }
        _expectNone<Button> { text = "First" }
        _get<Button> { text = "Second" }
    }

    @Test fun addThemeVariants() {
        val btn = UI.getCurrent().popupButton("foo")
        btn.addThemeVariants(MenuBarVariant.LUMO_PRIMARY, MenuBarVariant.LUMO_SMALL)
        expect(setOf("primary", "small")) { _get<MenuBar>().themeNames.toSet() }
    }

    @Test fun close() {
        val btn = UI.getCurrent().popupButton("foo") {
            content { button("Hello!") }
        }
        btn.close()  // only schedules JS; must not blow up and must not drop the content
        _get<Button> { text = "Hello!" }
    }

    @Test fun addPopupVisibilityListener() {
        val visibilities = mutableListOf<Boolean>()
        val btn = UI.getCurrent().popupButton("foo") {
            content { button("Hello!") }
        }
        val registration = btn.addPopupVisibilityListener { visibilities.add(it.isVisible) }

        btn.fireOnPopupOpened(true)
        btn.fireOnPopupOpened(false)
        expect(listOf(true, false)) { visibilities }

        registration.remove()
        btn.fireOnPopupOpened(true)
        expect(listOf(true, false)) { visibilities }
    }

    @Test fun `the visibility event is sourced from the MenuBar`() {
        var source: Any? = null
        val btn = UI.getCurrent().popupButton("foo")
        btn.addPopupVisibilityListener { source = it.source }
        btn.fireOnPopupOpened(true)
        expect(true) { source === _get<MenuBar>() }
    }
}

/**
 * Calls the `@ClientCallable` that the browser invokes when the popup overlay opens or closes.
 */
private fun PopupButton.fireOnPopupOpened(opened: Boolean) {
    val method = PopupButton::class.java.getDeclaredMethod("onPopupOpened", Boolean::class.java)
    method.isAccessible = true
    method.invoke(this, opened)
}
