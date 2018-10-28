package com.github.vok.karibudsl.flow.cssstyledsl.properties

import com.github.vok.karibudsl.flow.cssstyledsl.KCssStyle

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

