package com.github.mvysny.karibudsl.v10

import kotlinx.css.LinearDimension
import kotlinx.css.properties.LineHeight

/**
 * [Lumo Typography] (https://vaadin.com/docs/latest/styling/lumo/lumo-style-properties/typography)
 */
@Suppress("unused", "SpellCheckingInspection")
public enum class KLumoFontSize(override val varName: String) : KCssVarNameA {
    XXXL("--lumo-font-size-xxxl"),
    XXL("--lumo-font-size-xxl"),
    XL("--lumo-font-size-xl"),
    L("--lumo-font-size-l"),
    M("--lumo-font-size-m"),
    S("--lumo-font-size-s"),
    XS("--lumo-font-size-xs"),
    XXS("--lumo-font-size-xxs"),

    ;

    public operator fun unaryPlus(): LinearDimension = LinearDimension(cssValue)

}

@Suppress("unused")
public enum class KLumoLineHeight(override val varName: String) : KCssVarNameA {
    M("--lumo-line-height-m"),
    S("--lumo-line-height-s"),
    XS("--lumo-line-height-xs"),
    ;

    public operator fun unaryPlus(): LineHeight = LineHeight(cssValue)

}

