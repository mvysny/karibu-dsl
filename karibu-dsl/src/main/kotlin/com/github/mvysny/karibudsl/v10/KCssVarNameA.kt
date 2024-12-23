package com.github.mvysny.karibudsl.v10

/**
 * style="line-height: var(--lumo-line-height-m);"
 * style="font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);"
 */
public interface KCssVarNameA {
    public val varName : String
    public val cssValue: String get() = "var($varName)"
}