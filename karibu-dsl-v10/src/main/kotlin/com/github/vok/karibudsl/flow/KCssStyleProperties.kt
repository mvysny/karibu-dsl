package com.github.vok.karibudsl.flow

var KCssStyle.background: String?
    get() = style["background"]
    set(value) {
        style["background"] = value
    }

var KCssStyle.color: String?
    get() = style["color"]
    set(value) {
        style["color"] = value
    }

var KCssStyle.marginLeft: String?
    get() = style["margin-left"]
    set(value) {
        style["margin-left"] = value
    }
