package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents

/**
 * When introducing extensions for your custom components, just call this in your method. For example:
 *
 * `fun HasComponents.shinyComponent(caption: String? = null, block: ShinyComponent.()->Unit = {}) = init(ShinyComponent(caption), block)`
 *
 * Adds [component] to receiver, see [HasComponents.add] for details.
 *
 * @param component the component to attach
 * @param block optional block to run over the component, allowing you to add children to the [component]
 */
fun <T : Component> (@VaadinDsl HasComponents).init(component: T, block: T.()->Unit = {}): T {
    add(component)
    component.block()
    return component
}
