package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import org.junit.jupiter.api.*
import kotlin.test.expect

abstract class LayoutsTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().flexLayout {
            verticalLayout()
            horizontalLayout()
            formLayout()
        }
    }

    @Nested
    inner class formLayout {
        @Test
        fun smoke() {
            UI.getCurrent().formLayout()
            UI.getCurrent().formLayout {}
            UI.getCurrent().formLayout("foo")
            UI.getCurrent().formLayout("foo") {}
            _expect<FormLayout>(4)
        }
        @Test
        fun simple() {
            UI.getCurrent().formLayout {
                responsiveSteps { "0px"(1, top); "30em"(2, aside) }
                textField("Name") {
                    colspan = 2
                }
            }
            _expectOne<FormLayout>()
            _expectOne<TextField>()
        }
        @Test fun `class names`() {
            var layout: FormLayout = UI.getCurrent().formLayout()
            expect(null) { layout.className }
            layout = UI.getCurrent().formLayout("foo bar foo")
            expect("foo bar") { layout.className }
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

    @Nested inner class alignSelfInParent {
        @Test fun verticalAlignSelf() {
            lateinit var button: Button
            UI.getCurrent().horizontalLayout { button = button() }
            expect(FlexComponent.Alignment.AUTO) { button.verticalAlignSelf }
            button.verticalAlignSelf = FlexComponent.Alignment.END
            expect(FlexComponent.Alignment.END) { button.verticalAlignSelf }
        }

        @Test fun horizontalAlignSelf() {
            lateinit var button: Button
            UI.getCurrent().verticalLayout { button = button() }
            expect(FlexComponent.Alignment.AUTO) { button.horizontalAlignSelf }
            button.horizontalAlignSelf = FlexComponent.Alignment.END
            expect(FlexComponent.Alignment.END) { button.horizontalAlignSelf }
        }

        @Test fun `verticalAlignSelf only works inside a HorizontalLayout`() {
            lateinit var button: Button
            UI.getCurrent().verticalLayout { button = button() }
            val ex = runCatching { button.verticalAlignSelf }.exceptionOrNull()
            check(ex is ClassCastException) { "expected ClassCastException, got $ex" }
        }

        @Test fun `horizontalAlignSelf only works inside a VerticalLayout`() {
            lateinit var button: Button
            UI.getCurrent().horizontalLayout { button = button() }
            val ex = runCatching { button.horizontalAlignSelf }.exceptionOrNull()
            check(ex is ClassCastException) { "expected ClassCastException, got $ex" }
        }
    }

    @Nested inner class horizontalLayoutContent {
        @Test fun `every horizontalAlignment constant`() {
            val hl = HorizontalLayout()
            hl.content {
                for ((constant, expected) in listOf(
                    left to FlexComponent.JustifyContentMode.START,
                    right to FlexComponent.JustifyContentMode.END,
                    center to FlexComponent.JustifyContentMode.CENTER,
                    between to FlexComponent.JustifyContentMode.BETWEEN,
                    around to FlexComponent.JustifyContentMode.AROUND,
                    evenly to FlexComponent.JustifyContentMode.EVENLY
                )) {
                    horizontalAlignment = constant
                    expect(expected) { horizontalAlignment }
                    expect(expected) { hl.justifyContentMode }
                }
            }
        }

        @Test fun `every verticalAlignment constant`() {
            val hl = HorizontalLayout()
            hl.content {
                for ((constant, expected) in listOf(
                    top to FlexComponent.Alignment.START,
                    bottom to FlexComponent.Alignment.END,
                    middle to FlexComponent.Alignment.CENTER,
                    stretch to FlexComponent.Alignment.STRETCH,
                    baseline to FlexComponent.Alignment.BASELINE
                )) {
                    verticalAlignment = constant
                    expect(expected) { verticalAlignment }
                    expect(expected) { hl.defaultVerticalComponentAlignment }
                }
            }
        }

        @Test fun align() {
            val hl = HorizontalLayout()
            hl.content { align(right, bottom) }
            expect(FlexComponent.JustifyContentMode.END) { hl.justifyContentMode }
            expect(FlexComponent.Alignment.END) { hl.defaultVerticalComponentAlignment }
        }

        @Test fun center() {
            val hl = HorizontalLayout()
            hl.content { center() }
            expect(FlexComponent.JustifyContentMode.CENTER) { hl.justifyContentMode }
            expect(FlexComponent.Alignment.CENTER) { hl.defaultVerticalComponentAlignment }
        }
    }

    @Nested inner class verticalLayoutContent {
        @Test fun `every horizontalAlignment constant`() {
            val vl = VerticalLayout()
            vl.content {
                for ((constant, expected) in listOf(
                    left to FlexComponent.Alignment.START,
                    right to FlexComponent.Alignment.END,
                    center to FlexComponent.Alignment.CENTER,
                    stretch to FlexComponent.Alignment.STRETCH
                )) {
                    horizontalAlignment = constant
                    expect(expected) { horizontalAlignment }
                    expect(expected) { vl.defaultHorizontalComponentAlignment }
                }
            }
        }

        @Test fun `every verticalAlignment constant`() {
            val vl = VerticalLayout()
            vl.content {
                for ((constant, expected) in listOf(
                    top to FlexComponent.JustifyContentMode.START,
                    bottom to FlexComponent.JustifyContentMode.END,
                    middle to FlexComponent.JustifyContentMode.CENTER,
                    between to FlexComponent.JustifyContentMode.BETWEEN,
                    around to FlexComponent.JustifyContentMode.AROUND,
                    evenly to FlexComponent.JustifyContentMode.EVENLY
                )) {
                    verticalAlignment = constant
                    expect(expected) { verticalAlignment }
                    expect(expected) { vl.justifyContentMode }
                }
            }
        }

        @Test fun align() {
            val vl = VerticalLayout()
            vl.content { align(right, bottom) }
            expect(FlexComponent.Alignment.END) { vl.defaultHorizontalComponentAlignment }
            expect(FlexComponent.JustifyContentMode.END) { vl.justifyContentMode }
        }

        @Test fun center() {
            val vl = VerticalLayout()
            vl.content { center() }
            expect(FlexComponent.Alignment.CENTER) { vl.defaultHorizontalComponentAlignment }
            expect(FlexComponent.JustifyContentMode.CENTER) { vl.justifyContentMode }
        }
    }

    /**
     * Only the enum is reachable. Vaadin's own [FlexLayout.setFlexWrap]/[FlexLayout.getFlexWrap]
     * members shadow Karibu's `FlexLayout.flexWrap` extension property, and in Kotlin a member
     * always wins over an extension - so `flexLayout.flexWrap` resolves to Vaadin's, taking
     * Vaadin's own `FlexLayout.FlexWrap`. The extension property cannot be called from Kotlin
     * source at all, which is why it has no test here.
     */
    @Nested inner class flexWrapTests {
        @Test fun findByFlex() {
            expect(FlexWrap.Nowrap) { FlexWrap.findByFlex("nowrap") }
            expect(FlexWrap.Wrap) { FlexWrap.findByFlex("wrap") }
            expect(FlexWrap.WrapReverse) { FlexWrap.findByFlex("wrap-reverse") }
            expect(null) { FlexWrap.findByFlex(null) }
            expect(null) { FlexWrap.findByFlex("bogus") }
        }

        @Test fun flexValue() {
            expect(listOf("nowrap", "wrap", "wrap-reverse")) { FlexWrap.entries.map { it.flexValue } }
        }

        @Test fun `every constant is findable by its own flexValue`() {
            for (wrap in FlexWrap.entries) {
                expect(wrap) { FlexWrap.findByFlex(wrap.flexValue) }
            }
        }
    }
}
