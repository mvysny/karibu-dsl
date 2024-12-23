package com.github.mvysny.karibudsl.v10

import kotlinx.css.Color

/**
 * --- Lumo Colors https://vaadin.com/docs/latest/styling/lumo/lumo-style-properties/color
 */
@Suppress("unused", "EnumEntryName")
enum class KLumoPrimaryColor(override val varName: String) : KCssVarNameA {
    `10pct`("--lumo-primary-color-10pct"),
    `50pct`("--lumo-primary-color-50pct"),
    `100pct`("--lumo-primary-color"),
    Text("--lumo-primary-text-color"),
    Contrast("--lumo-primary-contrast-color"),

    ;

    operator fun unaryPlus()  = Color(cssValue)

}

@Suppress("unused")
enum class KLumoTextColor(override val varName: String) : KCssVarNameA {
    Heading("--lumo-header-text-color"),
    Body("--lumo-body-text-color"),
    Secondary("--lumo-secondary-text-color"),
    Tertiary("--lumo-tertiary-text-color"),
    Disabled("--lumo-disabled-text-color"),

    ;

    operator fun unaryPlus()  = Color(cssValue)

}
