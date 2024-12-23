package com.github.mvysny.karibudsl.v10

/**
 * --- Renderers https://vaadin.com/docs/latest/components/grid/renderers
 *      "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
 * --- Vaadin 23 - CSS Remove theme=“padding spacing” https://vaadin.com/forum/t/vaadin-23-css-remove-theme-padding-spacing/167207
 *      The CSS and the HTML produced are the same comparing old to new, but for some reason, the theme=“padding spacing” is now causing issues.
 */
@Suppress("EnumEntryName")
enum class KLitRendererTheme {
    spacing,
    padding,
    ;
}