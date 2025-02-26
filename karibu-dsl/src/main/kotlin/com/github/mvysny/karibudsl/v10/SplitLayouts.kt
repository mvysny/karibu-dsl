package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.dom.Element

/**
 * Creates a [Split Layout](https://vaadin.com/elements/vaadin-split-layout). See the HTML Examples link for a list
 * of possible alternative themes.
 * ```
 * splitLayout {
 *   primary {
 *     button("left side")
 *   }
 *   secondary {
 *     span("right side")
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).splitLayout(
    orientation: SplitLayout.Orientation = SplitLayout.Orientation.HORIZONTAL,
    block: (@VaadinDsl SplitLayout).() -> Unit = {}
): SplitLayout = init(SplitLayout(orientation), block)

/**
 * Adds components to the first slot. See [splitLayout] for an example.
 * Current limitation: Can not be called repeatedly.
 */
@VaadinDsl
public fun (@VaadinDsl SplitLayout).primary(
    block: (@VaadinDsl HasComponents).() -> Unit = {}
) {
    val primarySlot = object : DummyHasComponents {
        override fun add(components: Collection<Component>) {
            if (components.isEmpty()) return
            check(primaryComponent == null) { "Primary component is already populated, can not add multiple primary components" }
            addToPrimary(*components.toTypedArray())
        }
    }
    primarySlot.block()
}

/**
 * Adds components to the second slot. See [splitLayout] for an example.
 * Current limitation: Can not be called repeatedly.
 */
@VaadinDsl
public fun (@VaadinDsl SplitLayout).secondary(
    block: (@VaadinDsl HasComponents).() -> Unit = {}
) {
    val secondarySlot = object : DummyHasComponents {
        override fun add(components: Collection<Component>) {
            if (components.isEmpty()) return
            check(secondaryComponent == null) { "Primary component is already populated, can not add multiple primary components" }
            addToSecondary(*components.toTypedArray())
        }
    }
    secondarySlot.block()
}
