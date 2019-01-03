package com.github.vok.karibudsl.flow.cssstyledsl.properties

import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.dom.Style

class KCssStyleMarginProperty(style: Style) : KCssStyleAbstractPropertyA(style) {

    var top: String?
        get() = style["margin-top"]
        set(value) {
            style["margin-top"] = value
        }

    var left: String?
        get() = style["margin-left"]
        set(value) {
            style["margin-left"] = value
        }

    var right: String?
        get() = style["margin-right"]
        set(value) {
            style["margin-right"] = value
        }

    var bottom: String?
        get() = style["margin-bottom"]
        set(value) {
            style["margin-bottom"] = value
        }

}

val HasStyle.margin get() = KCssStyleMarginProperty(style)

