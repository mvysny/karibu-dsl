package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.*
import com.vaadin.flow.component.html.*
import org.intellij.lang.annotations.Language
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

fun (@VaadinDsl HasComponents).div(block: (@VaadinDsl Div).() -> Unit = {}) = init(Div(), block)

fun (@VaadinDsl HasComponents).h1(text: String = "", block: (@VaadinDsl H1).() -> Unit = {}) = init(H1(text), block)
fun (@VaadinDsl HasComponents).h2(text: String = "", block: (@VaadinDsl H2).() -> Unit = {}) = init(H2(text), block)
fun (@VaadinDsl HasComponents).h3(text: String = "", block: (@VaadinDsl H3).() -> Unit = {}) = init(H3(text), block)
fun (@VaadinDsl HasComponents).h4(text: String = "", block: (@VaadinDsl H4).() -> Unit = {}) = init(H4(text), block)
fun (@VaadinDsl HasComponents).h5(text: String = "", block: (@VaadinDsl H5).() -> Unit = {}) = init(H5(text), block)
fun (@VaadinDsl HasComponents).h6(text: String = "", block: (@VaadinDsl H6).() -> Unit = {}) = init(H6(text), block)
fun (@VaadinDsl HasComponents).hr(block: (@VaadinDsl Hr).() -> Unit = {}) = init(Hr(), block)

fun (@VaadinDsl HasComponents).em(text: String? = null, block: (@VaadinDsl Emphasis).() -> Unit = {}) = init(Emphasis(text), block)
fun (@VaadinDsl HasComponents).span(text: String? = null, block: (@VaadinDsl Span).() -> Unit = {}) = init(Span(text), block)
fun (@VaadinDsl HasComponents).anchor(href: String? = null, text: String? = href, block: (@VaadinDsl Anchor).() -> Unit = {}) = init(Anchor(href, text), block)
fun (@VaadinDsl HasComponents).image(src: String? = null, alt: String? = src, block: (@VaadinDsl Image).() -> Unit = {}) = init(Image(src, alt), block)
fun (@VaadinDsl HasComponents).label(text: String? = null, `for`: Component? = null, block: (@VaadinDsl Label).() -> Unit = {}) = init(Label(text).apply {
    if (`for` != null) setFor(`for`)
}, block)

fun (@VaadinDsl HasComponents).input(block: (@VaadinDsl Input).() -> Unit = {}) = init(Input(), block)
// workaround around https://github.com/vaadin/flow/issues/1699
var Input.placeholder2: String?
get() = placeholder.orElse(null)
set(value) { setPlaceholder(value) }

fun (@VaadinDsl HasComponents).nativeButton(block: (@VaadinDsl NativeButton).() -> Unit = {}) = init(NativeButton(), block)
fun (@VaadinDsl ClickNotifier).onLeftClick(leftClickListener: (ClickEvent)->Unit) {
    addClickListener(leftClickListener)
}

fun (@VaadinDsl HasComponents).html(@Language("html") html: String) {
    val doc: Element = Jsoup.parse(html).body()
    for (childNode in doc.childNodes()) {
        when(childNode) {
            is TextNode -> text(childNode.text())
            is Element -> add(Html(childNode.outerHtml()))
        }
    }
}
