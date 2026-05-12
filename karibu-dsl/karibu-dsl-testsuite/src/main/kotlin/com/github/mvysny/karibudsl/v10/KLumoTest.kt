package com.github.mvysny.karibudsl.v10

import org.junit.jupiter.api.Test
import kotlin.test.expect

/**
 * Pins the Lumo CSS custom-property names against the values published in
 * https://vaadin.com/docs/latest/styling/lumo/lumo-style-properties — a typo
 * in any varName here would silently produce broken styles in user apps.
 */
abstract class KLumoTest {
    @Test fun `font size css variables`() {
        expect("--lumo-font-size-xxxl") { KLumoFontSize.XXXL.varName }
        expect("--lumo-font-size-xxl") { KLumoFontSize.XXL.varName }
        expect("--lumo-font-size-xl") { KLumoFontSize.XL.varName }
        expect("--lumo-font-size-l") { KLumoFontSize.L.varName }
        expect("--lumo-font-size-m") { KLumoFontSize.M.varName }
        expect("--lumo-font-size-s") { KLumoFontSize.S.varName }
        expect("--lumo-font-size-xs") { KLumoFontSize.XS.varName }
        expect("--lumo-font-size-xxs") { KLumoFontSize.XXS.varName }
    }

    @Test fun `line height css variables`() {
        expect("--lumo-line-height-m") { KLumoLineHeight.M.varName }
        expect("--lumo-line-height-s") { KLumoLineHeight.S.varName }
        expect("--lumo-line-height-xs") { KLumoLineHeight.XS.varName }
    }

    @Test fun `primary color css variables`() {
        expect("--lumo-primary-color-10pct") { KLumoPrimaryColor.`10pct`.varName }
        expect("--lumo-primary-color-50pct") { KLumoPrimaryColor.`50pct`.varName }
        expect("--lumo-primary-color") { KLumoPrimaryColor.`100pct`.varName }
        expect("--lumo-primary-text-color") { KLumoPrimaryColor.Text.varName }
        expect("--lumo-primary-contrast-color") { KLumoPrimaryColor.Contrast.varName }
    }

    @Test fun `text color css variables`() {
        expect("--lumo-header-text-color") { KLumoTextColor.Heading.varName }
        expect("--lumo-body-text-color") { KLumoTextColor.Body.varName }
        expect("--lumo-secondary-text-color") { KLumoTextColor.Secondary.varName }
        expect("--lumo-tertiary-text-color") { KLumoTextColor.Tertiary.varName }
        expect("--lumo-disabled-text-color") { KLumoTextColor.Disabled.varName }
    }

    @Test fun `cssValue wraps varName in var()`() {
        expect("var(--lumo-font-size-m)") { KLumoFontSize.M.cssValue }
        expect("var(--lumo-line-height-xs)") { KLumoLineHeight.XS.cssValue }
        expect("var(--lumo-primary-color)") { KLumoPrimaryColor.`100pct`.cssValue }
        expect("var(--lumo-body-text-color)") { KLumoTextColor.Body.cssValue }
    }

    @Test fun `unaryPlus operators yield kotlinx-css values backed by var()`() {
        expect("var(--lumo-font-size-m)") { (+KLumoFontSize.M).value }
        expect("var(--lumo-line-height-xs)") { (+KLumoLineHeight.XS).value }
        expect("var(--lumo-primary-color)") { (+KLumoPrimaryColor.`100pct`).value }
        expect("var(--lumo-body-text-color)") { (+KLumoTextColor.Body).value }
    }
}
