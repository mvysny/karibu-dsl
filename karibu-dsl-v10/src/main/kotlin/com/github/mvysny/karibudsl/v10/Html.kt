package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.*
import com.vaadin.flow.component.html.*
import com.vaadin.flow.server.AbstractStreamResource
import org.intellij.lang.annotations.Language
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

@VaadinDsl
fun (@VaadinDsl HasComponents).div(block: (@VaadinDsl Div).() -> Unit = {}) = init(Div(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).h1(text: String = "", block: (@VaadinDsl H1).() -> Unit = {}) = init(H1(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).h2(text: String = "", block: (@VaadinDsl H2).() -> Unit = {}) = init(H2(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).h3(text: String = "", block: (@VaadinDsl H3).() -> Unit = {}) = init(H3(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).h4(text: String = "", block: (@VaadinDsl H4).() -> Unit = {}) = init(H4(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).h5(text: String = "", block: (@VaadinDsl H5).() -> Unit = {}) = init(H5(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).h6(text: String = "", block: (@VaadinDsl H6).() -> Unit = {}) = init(H6(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).hr(block: (@VaadinDsl Hr).() -> Unit = {}) = init(Hr(), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).p(text: String = "", block: (@VaadinDsl Paragraph).() -> Unit = {}) = init(Paragraph(text), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).em(text: String? = null, block: (@VaadinDsl Emphasis).() -> Unit = {}) = init(Emphasis(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).span(text: String? = null, block: (@VaadinDsl Span).() -> Unit = {}) = init(Span(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).anchor(href: String = "", text: String? = href, block: (@VaadinDsl Anchor).() -> Unit = {}) = init(Anchor(href, text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).anchor(href: AbstractStreamResource, text: String? = null, block: (@VaadinDsl Anchor).() -> Unit = {}) = init(Anchor(href, text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).image(src: String = "", alt: String = "", block: (@VaadinDsl Image).() -> Unit = {}) = init(Image(src, alt), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).image(src: AbstractStreamResource, alt: String = "", block: (@VaadinDsl Image).() -> Unit = {}) = init(Image(src, alt), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).label(text: String? = null, `for`: Component? = null, block: (@VaadinDsl Label).() -> Unit = {}) = init(Label(text).apply {
    if (`for` != null) setFor(`for`)
}, block)

@VaadinDsl
fun (@VaadinDsl HasComponents).input(block: (@VaadinDsl Input).() -> Unit = {}) = init(Input(), block)
// workaround around https://github.com/vaadin/flow/issues/1699
var Input.placeholder2: String?
get() = placeholder.orElse(null)
set(value) { setPlaceholder(value) }

@VaadinDsl
fun (@VaadinDsl HasComponents).nativeButton(text: String? = null, block: (@VaadinDsl NativeButton).() -> Unit = {}) =
        init(NativeButton(text), block)

/**
 * Adds given html snippet into the current element. Way better than [Html] since:
 * * It doesn't ignore root text nodes
 * * It supports multiple elements.
 * Example of use:
 * ```
 * div { html("I <strong>strongly</strong> believe in <i>openness</i>") }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).html(@Language("html") html: String) {
    val doc: Element = Jsoup.parse(html).body()
    for (childNode in doc.childNodes()) {
        when(childNode) {
            is TextNode -> text(childNode.text())
            is Element -> add(Html(childNode.outerHtml()))
        }
    }
}

@Tag(Tag.STRONG)
class Strong : HtmlContainer(), HasText
@VaadinDsl
fun (@VaadinDsl HasComponents).strong(text: String = "", block: (@VaadinDsl Strong).() -> Unit = {}) = init(Strong().apply { this.text = text }, block)

/**
 * Component representing a `<br>` element.
 */
@Tag(Tag.BR)
class Br : HtmlComponent()
@VaadinDsl
fun (@VaadinDsl HasComponents).br(block: (@VaadinDsl Br).() -> Unit = {}) = init(Br(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).ol(type: OrderedList.NumberingType? = null, block: (@VaadinDsl OrderedList).() -> Unit = {})
        = init(if (type == null) OrderedList() else OrderedList(type), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).li(text: String? = null, block: (@VaadinDsl ListItem).() -> Unit = {})
        = init(if (text == null) ListItem() else ListItem(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).iframe(src: String? = null, block: (@VaadinDsl IFrame).() -> Unit = {})
        = init(if (src == null) IFrame() else IFrame(src), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).article(block: (@VaadinDsl Article).() -> Unit = {})
        = init(Article(), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).aside(block: (@VaadinDsl Aside).() -> Unit = {})
        = init(Aside(), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).dl(block: (@VaadinDsl DescriptionList).() -> Unit = {})
        = init(DescriptionList(), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).dd(text: String? = null, block: (@VaadinDsl DescriptionList.Description).() -> Unit = {})
        = init(if (text == null) DescriptionList.Description() else DescriptionList.Description(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).dt(text: String? = null, block: (@VaadinDsl DescriptionList.Term).() -> Unit = {})
        = init(if (text == null) DescriptionList.Term() else DescriptionList.Term(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).footer(block: (@VaadinDsl Footer).() -> Unit = {})
        = init(Footer(), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).header(block: (@VaadinDsl Header).() -> Unit = {})
        = init(Header(), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).pre(text: String? = null, block: (@VaadinDsl Pre).() -> Unit = {})
        = init(if (text == null) Pre() else Pre(text), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).ul(block: (@VaadinDsl UnorderedList).() -> Unit = {})
        = init(UnorderedList(), block)
