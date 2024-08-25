package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.jupiter.api.*
import kotlin.test.expect

abstract class LayoutsTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().flexLayout {
            verticalLayout()
            horizontalLayout()
        }
    }

    @Nested inner class `class names`() {
        @Test fun `flex layout`() {
            var layout: FlexLayout = UI.getCurrent().flexLayout()
            expect(null) { layout.className }
            layout = UI.getCurrent().flexLayout("foo bar foo")
            expect("foo bar") { layout.className }
        }
        @Test fun `vertical layout`() {
            var layout: VerticalLayout = UI.getCurrent().verticalLayout()
            expect(null) { layout.className }
            layout = UI.getCurrent().verticalLayout(classNames = "foo bar foo")
            expect("foo bar") { layout.className }
        }
        @Test fun `horizontal layout`() {
            var layout: HorizontalLayout = UI.getCurrent().horizontalLayout()
            expect(null) { layout.className }
            layout = UI.getCurrent().horizontalLayout(classNames = "foo bar foo")
            expect("foo bar") { layout.className }
        }
    }

    @Nested inner class flexGrow {
        @Test fun `flexGrow works even when the component is not yet attached to parent`() {
            Button().flexGrow = 1.0
        }
        @Test fun `setting flexGrow works`() {
            val button = Button()
            button.flexGrow = 1.0
            expect("1.0") { button.element.style.get("flexGrow") }
        }
        @Test fun `getting flexGrow works`() {
            val button = Button()
            button.element.style.set("flexGrow", "25")
            expect(25.0) { button.flexGrow }
        }
        @Test fun `setting flexGrow to null removes the style`() {
            val button = Button()
            button.element.style.set("flexGrow", "25")
            button.flexGrow = null
            expect(null) { button.element.style.get("flexGrow") }
        }
        @Test fun `by default component has flexGrow of null`() {
            expect(null) { Button().flexGrow }
        }
        @Test fun `setting negative flexGrow fails`() {
            assertThrows<IllegalArgumentException> {
                Button().flexGrow = -1.0
            }
        }
    }

    @Test fun expand() {
        val button = Button()
        expect(false) { button.isExpand }
        button.isExpand = true
        expect(true) { button.isExpand }
        button.isExpand = false
        expect(false) { button.isExpand }
    }

    @Test fun flexShrink() {
        val button = Button()
        expect(null) { button.flexShrink }
        button.flexShrink = 0.0
        expect<Double?>(0.0) { button.flexShrink }
        button.flexShrink = 1.0
        expect<Double?>(1.0) { button.flexShrink }
        expect("1.0") { button.element.style.get("flexShrink") }
        button.flexShrink = null
        expect(null) { button.flexShrink }
        expect(null) { button.element.style.get("flexShrink") }
    }

    @Test fun flexBasis() {
        val button = Button()
        expect(null) { button.flexBasis }
        button.flexBasis = "auto"
        expect<String?>("auto") { button.flexBasis }
        button.flexBasis = null
        expect(null) { button.flexBasis }
        expect(null) { button.element.style.get("flexBasis") }
    }

    @Test fun alignSelf() {
        lateinit var button: Button
        UI.getCurrent().verticalLayout {
            button = button()
        }
        expect(FlexComponent.Alignment.AUTO) { button.alignSelf }
        button.alignSelf = FlexComponent.Alignment.END
        expect<FlexComponent.Alignment?>(FlexComponent.Alignment.END) { button.alignSelf }
        button.alignSelf = null
        expect(FlexComponent.Alignment.AUTO) { button.alignSelf }
        expect(null) { button.element.style.get("alignSelf") }
    }
}
