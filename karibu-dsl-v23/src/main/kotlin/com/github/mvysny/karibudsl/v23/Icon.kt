package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.FontIcon
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.SvgIcon
import com.vaadin.flow.component.icon.VaadinIcon

/**
 * Creates a Vaadin [SvgIcon](https://vaadin.com/elements/vaadin-icons/).
 *
 * The [src] may be:
 * * A path to a standalone SVG file
 * * A path in the format `"path/to/file.svg#symbol-id"` to an SVG file, where
 *   "symbol-id" refers to an id of an element (generally a
 *   `<symbol></symbol>` element) to be rendered in the icon component. Note that the sprite file needs to follow the same-origin policy
 * * Alternatively, the source can be defined as a string in the format `"data:image/svg+xml,<svg>...</svg>`
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).svgIcon(
    src: String? = null,
    symbol: String? = null,
    block: (@VaadinDsl SvgIcon).() -> Unit = {}
): SvgIcon {
    val icon = SvgIcon()
    if (src != null) {
        icon.src = src
    }
    if (symbol != null) {
        icon.symbol = symbol
    }
    return init(icon, block)
}

/**
 * Creates a font icon component with the given icon class names.
 *
 * Example: `fontIcon("fa-solid", "fa-user") {}`
 * @param iconClassNames The icon class names, not null
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).fontIcon(
    vararg iconClassNames: String,
    block: (@VaadinDsl FontIcon).() -> Unit = {}
): FontIcon =
    init(FontIcon(*iconClassNames), block)
