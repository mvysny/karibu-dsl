package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributools.template
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.renderer.LitRenderer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

val LitRenderer<*>._template: String get() {
    val templateProperty = LitRenderer::class.java.getDeclaredField("templateExpression")
    templateProperty.isAccessible = true
    val templateExpression = templateProperty.get(this) as String
    return templateExpression
}

abstract class KLitRendererTests {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        val r = buildLitRenderer<String> {
            val onButtonClick by function { item: String ->
            }

            templateExpression {
                button(
                    cssClass("category__edit"),
                    themeVariant(ButtonVariant.LUMO_TERTIARY),
                    click(onButtonClick)
                ) {
                    icon(icon(VaadinIcon.TRASH.create()))
                    +"Delete"
                }
            }
        }
        expect("""<vaadin-button class="category__edit" theme="tertiary" @click="${"$"}{onButtonClick}"><vaadin-icon icon="vaadin:trash"></vaadin-icon>Delete</vaadin-button>""") { r._template }
    }

    @Test fun hl() {
        val r = buildLitRenderer<String> {
            templateExpression {
                horizontalLayout(theme(spacing)) {}
            }
        }
        expect("""<vaadin-horizontal-layout theme="spacing"></vaadin-horizontal-layout>""") { r._template }
    }
}