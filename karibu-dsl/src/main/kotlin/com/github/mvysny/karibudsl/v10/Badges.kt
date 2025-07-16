package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributools.Badge
import com.github.mvysny.kaributools.BadgeVariant
import com.vaadin.flow.component.HasComponents

/**
 * Creates a [Badge](https://vaadin.com/docs/latest/components/badge). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).badge(
    text: String? = null,
    vararg variants: BadgeVariant,
    block: (@VaadinDsl Badge).() -> Unit = {}
): Badge {
    val badge = Badge(text)
    badge.addThemeVariants(*variants)
    return init(badge, block)
}
