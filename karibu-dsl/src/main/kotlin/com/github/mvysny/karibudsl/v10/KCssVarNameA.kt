package com.github.mvysny.karibudsl.v10

/**
 * [KCssVarNameA] allows you to insert a CSS variable
 * [varName] - the name of variable
 * [cssValue] - a css representation of the variable
 * Example of usage:
 * ```kotlin
 * verticalLayout(style { lineHeight = +KLumoLineHeight.XS; }) {
 *   ...
 * }
 * ```
 * Generated html code:
 * ```
 * <vaadin-vertical-layout style="line-height: var(----lumo-font-size-xs)"/></vaadin-vertical-layout>
 * ```
 */
public interface KCssVarNameA {
    public val varName : String
    public val cssValue: String get() = "var($varName)"
}