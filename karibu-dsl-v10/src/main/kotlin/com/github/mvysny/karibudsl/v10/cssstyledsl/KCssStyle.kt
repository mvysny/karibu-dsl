package com.github.vok.karibudsl.flow.cssstyledsl

import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.dom.Style

/**
 * Type-safe CSS style DSL
 */
class KCssStyle(val style: Style)

typealias KCssStyleBlock = KCssStyle.() -> Unit

fun HasStyle.cssStyleBlock(block: KCssStyleBlock) {
    KCssStyle(style).apply(block)
}


/*
val cssBlueColorStyle: KCssStyleBlock = {
    color = KCssColor.Blue.value
}

cssStyleBlock {
    cssBlueColorStyle()
    margin.left = "0px"
}
*/

