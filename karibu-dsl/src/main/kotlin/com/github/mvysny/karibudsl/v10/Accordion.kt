package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.accordion.Accordion
import com.vaadin.flow.component.accordion.AccordionPanel

/**
 * Creates a [Accordion](https://vaadin.com/components/vaadin-accordion). Code example:
 *
 * ```
 * accordion {
 *   panel("lorem ipsum") {
 *     content { span("dolor sit amet") }
 *   }
 * }
 * ```
 * or
 * ```
 * accordion {
 *   panel {
 *     summary { checkBox("More Lorem Ipsum?") }
 *     content { span("dolor sit amet") }
 *   }
 * }
 *  ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).accordion(block: (@VaadinDsl Accordion).() -> Unit = {}): @VaadinDsl Accordion =
        init(Accordion(), block)

/**
 * Adds a new [AccordionPanel] to this [Accordion].
 */
@VaadinDsl
public fun (@VaadinDsl Accordion).panel(summaryText: String? = null, block: (@VaadinDsl AccordionPanel).() -> Unit = {}): AccordionPanel {
    val panel: AccordionPanel = add(AccordionPanel())
    if (summaryText != null) {
        panel.summaryText = summaryText
    }
    panel.block()
    return panel
}
