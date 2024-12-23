package com.github.mvysny.karibudsl.v10

/**
 * style="line-height: var(--lumo-line-height-m);"
 * style="font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);"
 */
interface KCssVarNameA {
    val varName : String
    val cssValue get() = "var($varName)"
}