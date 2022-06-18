package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributools.addClassNames2
import com.vaadin.flow.component.*
import com.vaadin.flow.component.html.*
import com.vaadin.flow.server.AbstractStreamResource
import org.intellij.lang.annotations.Language
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

/**
 * Creates a div.
 * @param classNames optional additional class names, space-separated
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).div(classNames: String = "", block: (@VaadinDsl Div).() -> Unit = {}): Div {
    val div = Div()
    div.addClassNames2(classNames)
    return init(div, block)
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).h1(text: String = "", block: (@VaadinDsl H1).() -> Unit = {}): H1
        = init(H1(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).h2(text: String = "", block: (@VaadinDsl H2).() -> Unit = {}): H2
        = init(H2(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).h3(text: String = "", block: (@VaadinDsl H3).() -> Unit = {}): H3
        = init(H3(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).h4(text: String = "", block: (@VaadinDsl H4).() -> Unit = {}): H4
        = init(H4(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).h5(text: String = "", block: (@VaadinDsl H5).() -> Unit = {}): H5
        = init(H5(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).h6(text: String = "", block: (@VaadinDsl H6).() -> Unit = {}): H6
        = init(H6(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).hr(block: (@VaadinDsl Hr).() -> Unit = {}): Hr
        = init(Hr(), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).p(text: String = "", block: (@VaadinDsl Paragraph).() -> Unit = {}): Paragraph
        = init(Paragraph(text), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).em(text: String? = null, block: (@VaadinDsl Emphasis).() -> Unit = {}): Emphasis
        = init(Emphasis(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).span(text: String? = null, block: (@VaadinDsl Span).() -> Unit = {}): Span
        = init(Span(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).anchor(href: String = "", text: String? = href, block: (@VaadinDsl Anchor).() -> Unit = {}): Anchor
        = init(Anchor(href, text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).anchor(href: AbstractStreamResource, text: String? = null, block: (@VaadinDsl Anchor).() -> Unit = {}): Anchor
        = init(Anchor(href, text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).image(src: String = "", alt: String = "", block: (@VaadinDsl Image).() -> Unit = {}): Image
        = init(Image(src, alt), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).image(src: AbstractStreamResource, alt: String = "", block: (@VaadinDsl Image).() -> Unit = {}): Image
        = init(Image(src, alt), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).label(text: String? = null, `for`: Component? = null, block: (@VaadinDsl Label).() -> Unit = {}): Label {
    val label = Label(text)
    if (`for` != null) label.setFor(`for`)
    return init(label, block)
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).input(block: (@VaadinDsl Input).() -> Unit = {}): Input
        = init(Input(), block)

// workaround around https://github.com/vaadin/flow/issues/1699
public var Input.placeholder2: String?
    get() = placeholder.orElse(null)
    set(value) { setPlaceholder(value) }

@VaadinDsl
public fun (@VaadinDsl HasComponents).nativeButton(text: String? = null, block: (@VaadinDsl NativeButton).() -> Unit = {}): NativeButton =
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
public fun (@VaadinDsl HasComponents).html(@Language("html") html: String) {
    val doc: Element = Jsoup.parse(html).body()
    for (childNode in doc.childNodes()) {
        when(childNode) {
            is TextNode -> text(childNode.text())
            is Element -> add(Html(childNode.outerHtml()))
        }
    }
}

@Tag(Tag.STRONG)
public class Strong : HtmlContainer(), HasText
@VaadinDsl
public fun (@VaadinDsl HasComponents).strong(text: String = "", block: (@VaadinDsl Strong).() -> Unit = {}): Strong
        = init(Strong().apply { this.text = text }, block)

/**
 * Component representing a `<br>` element.
 */
@Tag(Tag.BR)
public class Br : HtmlComponent()
@VaadinDsl
public fun (@VaadinDsl HasComponents).br(block: (@VaadinDsl Br).() -> Unit = {}): Br
        = init(Br(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).ol(type: OrderedList.NumberingType? = null, block: (@VaadinDsl OrderedList).() -> Unit = {}): OrderedList
        = init(if (type == null) OrderedList() else OrderedList(type), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).li(text: String? = null, block: (@VaadinDsl ListItem).() -> Unit = {}): ListItem
        = init(if (text == null) ListItem() else ListItem(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).iframe(src: String? = null, block: (@VaadinDsl IFrame).() -> Unit = {}): IFrame
        = init(if (src == null) IFrame() else IFrame(src), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).article(block: (@VaadinDsl Article).() -> Unit = {}): Article
        = init(Article(), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).aside(block: (@VaadinDsl Aside).() -> Unit = {}): Aside
        = init(Aside(), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).dl(block: (@VaadinDsl DescriptionList).() -> Unit = {}): DescriptionList
        = init(DescriptionList(), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).dd(text: String? = null, block: (@VaadinDsl DescriptionList.Description).() -> Unit = {}): DescriptionList.Description
        = init(if (text == null) DescriptionList.Description() else DescriptionList.Description(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).dt(text: String? = null, block: (@VaadinDsl DescriptionList.Term).() -> Unit = {}): DescriptionList.Term
        = init(if (text == null) DescriptionList.Term() else DescriptionList.Term(text), block)

// Don't add support for these HTML elements. They are really rarely used and will collide with Vaadin 23.1 Dialog footer()/header() DSL.
//@VaadinDsl
//public fun (@VaadinDsl HasComponents).footer(block: (@VaadinDsl Footer).() -> Unit = {}): Footer
//        = init(Footer(), block)
//@VaadinDsl
//public fun (@VaadinDsl HasComponents).header(block: (@VaadinDsl Header).() -> Unit = {}): Header
//        = init(Header(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).pre(text: String? = null, block: (@VaadinDsl Pre).() -> Unit = {}): Pre
        = init(if (text == null) Pre() else Pre(text), block)
@VaadinDsl
public fun (@VaadinDsl HasComponents).ul(block: (@VaadinDsl UnorderedList).() -> Unit = {}): UnorderedList
        = init(UnorderedList(), block)
