package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributools.template
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.renderer.LitRenderer
import kotlinx.css.lineHeight
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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
                button({
                    cssClass("category__edit")
                    themeVariant(ButtonVariant.LUMO_TERTIARY)
                    click(onButtonClick)
                }) {
                    icon({ icon(VaadinIcon.TRASH.create()) })
                    +"Delete"
                }
            }
        }
        expect("""<vaadin-button class="category__edit" theme="tertiary" @click="${"$"}{onButtonClick}"><vaadin-icon icon="vaadin:trash"></vaadin-icon>Delete</vaadin-button>""") { r._template }
    }

    @Test fun hl() {
        val r = buildLitRenderer<String> {
            templateExpression {
                horizontalLayout({theme(spacing)}) {}
            }
        }
        expect("""<vaadin-horizontal-layout theme="spacing"></vaadin-horizontal-layout>""") { r._template }
    }

    @Test fun vl() {
        val r = buildLitRenderer<String> {
            templateExpression {
                verticalLayout({ style { lineHeight = +KLumoLineHeight.XS } })
            }
        }
        expect("""<vaadin-vertical-layout style="line-height: var(--lumo-line-height-xs)"></vaadin-vertical-layout>""") { r._template }
    }

    @Test fun testImage() {
        val r = buildLitRenderer<String> {
            val columnIcon by property { row -> VaadinIcon.PLUS_CIRCLE.create().icon }
            templateExpression {
                icon({icon(columnIcon)})
            }
        }
        expect("""<vaadin-icon icon="${"$"}{item.columnIcon}"></vaadin-icon>""") { r._template }
    }

    @Nested inner class tags {
        @Test fun `span, div and anchor`() {
            val r = buildLitRenderer<String> {
                templateExpression {
                    span({ id("s") })
                    div({ cssClass("d") })
                    anchor({ href("https://vaadin.com") })
                }
            }
            expect("""<span id="s"></span><div class="d"></div><a href="https://vaadin.com"></a>""") { r._template }
        }

        @Test fun `a tag without attributes still emits the separating space`() {
            val r = buildLitRenderer<String> {
                templateExpression { span { +"hello" } }
            }
            expect("""<span >hello</span>""") { r._template }
        }

        @Test fun `img is self-closing, avatar is not`() {
            val r = buildLitRenderer<String> {
                val photo by property { it }
                templateExpression {
                    img({ src(photo) })
                    avatar({ name(photo) })
                }
            }
            expect("""<img src="${"$"}{item.photo}"/><vaadin-avatar name="${"$"}{item.photo}"></vaadin-avatar>""") { r._template }
        }

        @Test fun `tags nest`() {
            val r = buildLitRenderer<String> {
                templateExpression {
                    div({ cssClass("outer") }) {
                        span({ cssClass("inner") }) { +"x" }
                    }
                }
            }
            expect("""<div class="outer"><span class="inner">x</span></div>""") { r._template }
        }

        @Test fun `unaryPlus interpolates a property and a function`() {
            val r = buildLitRenderer<String> {
                val label by property { it }
                val onEdit by function { }
                templateExpression {
                    span {
                        +label
                        +onEdit
                    }
                }
            }
            expect("""<span >${"$"}{item.label}${"$"}{onEdit}</span>""") { r._template }
        }
    }

    @Nested inner class attributes {
        @Test fun `id, name, src, img and href of a property`() {
            val r = buildLitRenderer<String> {
                val p by property { it }
                templateExpression {
                    div({
                        id("the-id")
                        name(p)
                        src(p)
                        img(p)
                        href(p)
                    })
                }
            }
            expect(
                """<div id="the-id" name="${"$"}{item.p}" src="${"$"}{item.p}" img="${"$"}{item.p}" href="${"$"}{item.p}"></div>"""
            ) { r._template }
        }

        @Test fun `style from a literal String`() {
            val r = buildLitRenderer<String> {
                templateExpression { div({ style("color: red") }) }
            }
            expect("""<div style="color: red"></div>""") { r._template }
        }

        @Test fun `padding and spacing themes`() {
            val r = buildLitRenderer<String> {
                templateExpression {
                    horizontalLayout({ theme(padding) })
                    verticalLayout({ theme(spacing) })
                }
            }
            expect(
                """<vaadin-horizontal-layout theme="padding"></vaadin-horizontal-layout>""" +
                        """<vaadin-vertical-layout theme="spacing"></vaadin-vertical-layout>"""
            ) { r._template }
        }

        @Test fun `KLitRendererAttribute renders as name=value`() {
            val attr = KLitRendererAttribute("theme", "spacing")
            expect("theme") { attr.name }
            expect("spacing") { attr.value }
            expect("""theme="spacing"""") { attr.toString() }
        }
    }

    @Nested inner class propertiesAndFunctions {
        @Test fun `a property exposes its name, provider and litItem`() {
            lateinit var p: KLitRendererBuilder.Property<String>
            buildLitRenderer<String> {
                val myProp by property { "v-$it" }
                p = myProp
                templateExpression { span { +myProp } }
            }
            expect("myProp") { p.name }
            expect("v-a") { p.provider("a") }
            expect("\${item.myProp}") { p.litItem }
        }

        @Test fun `a function exposes its name, handler and litItem`() {
            lateinit var f: KLitRendererBuilder.Function<String>
            var called: String? = null
            buildLitRenderer<String> {
                val myFn by function { called = it }
                f = myFn
                templateExpression { span { +myFn } }
            }
            expect("myFn") { f.name }
            expect("\${myFn}") { f.litItem }
            f.handler("hello")
            expect("hello") { called }
        }

        @Test fun `a property not registered with this builder is rejected`() {
            val ex = runCatching {
                buildLitRenderer<String> {
                    templateExpression {
                        span { +KLitRendererBuilder.Property<String>("bogus") { it } }
                    }
                }
            }.exceptionOrNull()
            check(ex is IllegalArgumentException) { "expected IllegalArgumentException, got $ex" }
        }

        @Test fun `a function not registered with this builder is rejected`() {
            val ex = runCatching {
                buildLitRenderer<String> {
                    templateExpression {
                        span { +KLitRendererBuilder.Function<String>("bogus") { } }
                    }
                }
            }.exceptionOrNull()
            check(ex is IllegalArgumentException) { "expected IllegalArgumentException, got $ex" }
        }

        @Test fun `a function name must be a valid JavaScript identifier`() {
            val ex = runCatching {
                buildLitRenderer<String> { function("not-an-identifier") { } }
            }.exceptionOrNull()
            check(ex is IllegalArgumentException) { "expected IllegalArgumentException, got $ex" }
            expect(true, ex.message) { ex.message!!.contains("not valid JavaScript identifier") }
        }

        @Test fun `a function name must not be a JavaScript reserved word`() {
            val ex = runCatching {
                buildLitRenderer<String> { function("class") { } }
            }.exceptionOrNull()
            check(ex is IllegalArgumentException) { "expected IllegalArgumentException, got $ex" }
            expect(true, ex.message) { ex.message!!.contains("reserved keyword") }
        }

        @Test fun `templateExpression accepts a raw String`() {
            val r = buildLitRenderer<String> {
                templateExpression("<span>hello</span>")
            }
            expect("<span>hello</span>") { r._template }
        }
    }
}
