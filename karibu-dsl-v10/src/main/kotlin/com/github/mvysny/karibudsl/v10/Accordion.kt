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
 *     content { label("dolor sit amet") }
 *   }
 *   panel {
 *     summary { checkBox("More Lorem Ipsum?") }
 *     content { label("dolor sit amet") }
 *   }
 * }
 *  ```
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).accordion(block: (@VaadinDsl Accordion).() -> Unit = {}) = init(Accordion(), block)

/**
 * Adds a new [AccordionPanel] to this [Accordion].
 */
@VaadinDsl
fun (@VaadinDsl Accordion).panel(summaryText: String? = null, block: (@VaadinDsl AccordionPanel).() -> Unit = {}): AccordionPanel = add(AccordionPanel()).apply {
    if (summaryText != null) this.summaryText = summaryText
    block()
}
