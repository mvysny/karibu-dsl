package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.card.Card
import com.vaadin.flow.component.html.Div

/**
 * Creates the [Card](https://vaadin.com/docs/latest/components/card) component.
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
