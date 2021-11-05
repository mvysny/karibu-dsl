package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributools.HtmlSpan
import com.github.mvysny.kaributools.LabelWrapper
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import org.intellij.lang.annotations.Language

/**
 * Populates its contents with given html snippet. The advantage over [com.vaadin.flow.component.Html]
 * is that any html is accepted - it doesn't have to be wrapped in a single root element.
 *
 * Note that it is the developer's responsibility to sanitize and remove any
 * dangerous parts of the HTML before sending it to the user through this
 * component. Passing raw input data to the user will possibly lead to
 * cross-site scripting attacks.
 *
 * This component does not expand the HTML fragment into a server side DOM tree
 * so you cannot traverse or modify the HTML on the server. The root element can
 * be accessed through [Component.element] and the inner HTML through
 * [innerHTML].
 * @param innerHTML the HTML snippet to populate the span with.
 */
public fun (@VaadinDsl HasComponents).htmlSpan(
    @Language("html") innerHTML: String = "",
    block: (@VaadinDsl HtmlSpan).() -> Unit = {}
): HtmlSpan = init(HtmlSpan(innerHTML), block)

/**
 * Used to show a label on top of a component which doesn't show a [Component.label] itself.
 *
 * **Used in highly specific situations only:** for example if you need to add labeled
 * components into a `HorizontalLayout`.
 *
 * Whenever possible, you should add your components into a `FormLayout`
 * instead, via the `FormLayout.addFormItem()` which
 * supports labels. Alternatively, you can emulate labels by wrapping labels in a `H2`/`H3`/`H4`/`H5`/`H6`
 * then styling them accordingly.
 */
public fun (@VaadinDsl HasComponents).labelWrapper(
    label: String,
    block: (@VaadinDsl LabelWrapper).() -> Unit = {}
): LabelWrapper = init(LabelWrapper(label), block)
