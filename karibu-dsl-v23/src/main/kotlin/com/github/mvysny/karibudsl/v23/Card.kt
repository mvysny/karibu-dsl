package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.DummyHasComponents
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.buildSingleComponentOrNull
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.card.Card
import com.vaadin.flow.component.html.Div

/**
 * Creates the [Card](https://vaadin.com/docs/latest/components/card) component:
 * ```kotlin
 * card("Lapland", "The Exotic North") {
 *   headerPrefix { avatar("Lapland") }
 *   headerSuffix { badge("Arctic", BadgeVariant.SUCCESS) }
 *   add("Lapland is the northern-most region of Finland and an active outdoor destination.")
 *   footer {
 *     button("Book Vacation")
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).card(
    title: String? = null,
    subTitle: String? = null,
    block: (@VaadinDsl Card).() -> Unit = {}
): Card {
    val card = Card()
    if (title != null) {
        card.setTitle(title)
    }
    if (subTitle != null) {
        card.subtitle = Div(subTitle)
    }
    return init(card, block)
}

/**
 * Add components to [Card]'s footer:
 * ```kotlin
 * card("Lapland") {
 *   ...
 *   footer {
 *     button("Book Vacation")
 *   }
 * }
 */
@VaadinDsl
public fun (@VaadinDsl Card).footer(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val hc = object : DummyHasComponents() {
        override fun add(vararg components: Component) {
            addToFooter(*components)
        }
    }
    hc.block()
}

/**
 * Add components to [Card]'s header prefix:
 * ```kotlin
 * card("Lapland") {
 *   ...
 *   headerPrefix {
 *     avatar("Lapland")
 *   }
 * }
 */
@VaadinDsl
public fun (@VaadinDsl Card).headerPrefix(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    headerPrefix = buildSingleComponentOrNull("Card's header prefix", block)
}

/**
 * Add components to [Card]'s header suffix:
 * ```kotlin
 * card("Lapland") {
 *   ...
 *   headerSuffix {
 *     badge("Arctic", BadgeVariant.SUCCESS)
 *   }
 * }
 */
@VaadinDsl
public fun (@VaadinDsl Card).headerSuffix(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    headerSuffix = buildSingleComponentOrNull("Card's header suffix", block)
}
