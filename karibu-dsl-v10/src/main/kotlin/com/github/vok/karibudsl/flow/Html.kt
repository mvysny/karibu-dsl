package com.github.vok.karibudsl.flow

import com.vaadin.ui.Component
import com.vaadin.ui.common.HasComponents
import com.vaadin.ui.event.ClickEvent
import com.vaadin.ui.event.ClickNotifier
import com.vaadin.ui.html.*

fun (@VaadinDsl HasComponents).div(block: (@VaadinDsl Div).() -> Unit = {}) = init(Div(), block)

fun HasComponents.h1(text: String, block: (@VaadinDsl H1).() -> Unit = {}) = init(H1(text), block)
fun HasComponents.h2(text: String, block: (@VaadinDsl H2).() -> Unit = {}) = init(H2(text), block)
fun HasComponents.h3(text: String, block: (@VaadinDsl H3).() -> Unit = {}) = init(H3(text), block)
fun HasComponents.h4(text: String, block: (@VaadinDsl H4).() -> Unit = {}) = init(H4(text), block)
fun HasComponents.h5(text: String, block: (@VaadinDsl H5).() -> Unit = {}) = init(H5(text), block)
fun HasComponents.h6(text: String, block: (@VaadinDsl H6).() -> Unit = {}) = init(H6(text), block)
fun HasComponents.hr(block: (@VaadinDsl Hr).() -> Unit = {}) = init(Hr(), block)

fun HasComponents.em(text: String? = null, block: (@VaadinDsl Emphasis).() -> Unit = {}) = init(Emphasis(text), block)
fun HasComponents.span(text: String? = null, block: (@VaadinDsl Span).() -> Unit = {}) = init(Span(text), block)
fun HasComponents.anchor(href: String? = null, text: String? = href, block: (@VaadinDsl Anchor).() -> Unit = {}) = init(Anchor(href, text), block)
fun HasComponents.image(src: String? = null, alt: String? = src, block: (@VaadinDsl Image).() -> Unit = {}) = init(Image(src, alt), block)
fun HasComponents.label(text: String? = null, `for`: Component? = null, block: (@VaadinDsl Label).() -> Unit = {}) = init(Label(text).apply {
    if (`for` != null) setFor(`for`)
}, block)

fun HasComponents.input(block: (@VaadinDsl Input).() -> Unit = {}) = init(Input(), block)
// workaround around https://github.com/vaadin/flow/issues/1699
var Input.placeholder2: String?
get() = placeholder.orElse(null)
set(value) { setPlaceholder(value) }

fun (@VaadinDsl HasComponents).nativeButton(block: (@VaadinDsl NativeButton).() -> Unit = {}) = init(NativeButton(), block)
fun (@VaadinDsl ClickNotifier).onLeftClick(leftClickListener: (ClickEvent)->Unit) {
    addClickListener(leftClickListener)
}
