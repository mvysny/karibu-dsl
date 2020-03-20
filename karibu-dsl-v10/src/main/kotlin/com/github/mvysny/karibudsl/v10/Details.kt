package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.details.Details
import com.vaadin.flow.component.html.Div
import kotlin.streams.toList

internal fun Component.checkOneChildAndGet(producerName: String): Component {
    val count: Long = children.count()
    check(count == 1L) { "$producerName was expected to produce 1 component but it produced $count" }
    return children.findFirst().get()
}

/**
 * Creates a [Details](https://vaadin.com/components/vaadin-details). Code example:
 *
 * ```
 * details("Hello") {
 *   content { button("hi!") }
 * }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).details(summaryText: String? = null, block: (@VaadinDsl Details).() -> Unit = {}): Details {
    val details = Details()
    if (summaryText != null) details.summaryText = summaryText
    return init(details, block)
}

/**
 * Allows you to set the summary of this [Details] as a component:
 * ```
 * details {
 *   summary { button("learn more") }
 * }
 * ```
 */
@VaadinDsl
fun (@VaadinDsl Details).summary(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    val div = Div() // throwaway div, will be used to grab components produced by block
    div.block()
    val child: Component = div.checkOneChildAndGet("block()")
    this.summary = child
}

/**
 * Removes all [content][Details.getContent] components from this [Details].
 */
fun Details.clearContent() = setContent(null)

/**
 * Clears previous contents and re-populates the content of this [Details].
 */
@VaadinDsl
fun (@VaadinDsl Details).content(block: (@VaadinDsl HasComponents).() -> Unit = {}) {
    clearContent()
    val div = Div() // throwaway div, will be used to grab components produced by block
    div.block()
    addContent(*div.children.toList().toTypedArray())
}
