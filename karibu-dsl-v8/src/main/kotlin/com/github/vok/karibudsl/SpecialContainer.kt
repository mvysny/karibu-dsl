package com.github.vok.karibudsl

import com.vaadin.ui.Component
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.HasComponents

/**
 * A specialized version of [ComponentContainer], for certain special containers. The DSL's
 * [init] method will invoke [addComponent] method with the components being registered.
 *
 * For example, there may be a special container (say, a ticker) which does not attach the components as its Vaadin
 * children immediately - rather it only remembers the components added via [addComponent] in a special list and
 * Vaadin-attaches them once every 10 seconds, one at a time. This way, you can use the DSL to define all children (or
 * pages) of this special component, without having them attached immediately as Vaadin children.
 */
interface SpecialContainer : HasComponents {
    /**
     * Adds the component into this container. Called by [init] when DSL-adding children to this container.
     *
     * Note that there is no `removeComponent()` method nor any sort of support for listing of the components added via this
     * method. This is because the DSL only needs to add the components; the component list itself highly depends on the implementation
     * of the component and might cause confusion with the [HasComponents] list of attached components.
     * Therefore, I'm keeping this interface as dumb as possible.
     * @param component the component to be added.
     */
    fun addComponent(component: Component)
}
