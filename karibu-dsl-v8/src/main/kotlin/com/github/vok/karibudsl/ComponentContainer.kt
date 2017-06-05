package com.github.vok.karibudsl

import com.vaadin.ui.*

fun (@VaadinDsl HasComponents).verticalLayout(block: (@VaadinDsl VerticalLayout).()->Unit = {}) = init(VerticalLayout(), block)

fun (@VaadinDsl HasComponents).horizontalLayout(block: (@VaadinDsl HorizontalLayout).()->Unit = {}) = init(HorizontalLayout(), block)

fun (@VaadinDsl HasComponents).formLayout(block: (@VaadinDsl FormLayout).()->Unit = {}) = init(FormLayout(), block)

fun (@VaadinDsl HasComponents).absoluteLayout(block: (@VaadinDsl AbsoluteLayout).()->Unit = {}) = init(AbsoluteLayout(), block)

fun (@VaadinDsl HasComponents).cssLayout(block: (@VaadinDsl CssLayout).()->Unit = {}) = init(CssLayout(), block)

fun (@VaadinDsl HasComponents).gridLayout(columns: Int = 1, rows: Int = 1, block: (@VaadinDsl GridLayout).()->Unit = {}) = init(GridLayout(columns, rows), block)

/**
 * Adds a [child] to this component. Only concrete subclasses are supported:
 *
 * * [ComponentContainer]
 * * [SingleComponentContainer] (fails if the container already has a child)
 * * [PopupView]
 * * [AbstractSplitPanel]
 * * [SpecialContainer]
 *
 * The function will fail if the component
 * is already full (e.g. it is a split panel and it already contains two components).
 *
 * For custom containers just implement the [SpecialContainer] interface.
 */
fun HasComponents.addChild(child: Component) {
    when (this) {
        is ComponentContainer -> addComponent(child)
        is SpecialContainer -> addComponent(child)
        is SingleComponentContainer -> {
            if (componentCount >= 1) throw IllegalArgumentException("$this can only have one child")
            content = child
        }
        is PopupView -> popupComponent = child
        is AbstractSplitPanel -> when (componentCount) {
            0 -> firstComponent = child
            1 -> secondComponent = child
            else -> throw IllegalArgumentException("$this can only have 2 children")
        }
        else -> throw IllegalArgumentException("Unsupported component container $this")
    }
}

/**
 * Sets the expand ratio of this component with respect to its parent layout. See [AbstractOrderedLayout.setExpandRatio] for more details.
 *
 * Fails if this component is not nested inside [AbstractOrderedLayout].
 */
var (@VaadinDsl Component).expandRatio: Float
    get() = (parent as AbstractOrderedLayout).getExpandRatio(this)
    set(value) = (parent as AbstractOrderedLayout).setExpandRatio(this, value)

/**
 * Sets or gets alignment for this component with respect to its parent layout. Use
 * predefined alignments from Alignment class. Fails if the component is not nested inside [AbstractOrderedLayout]
 */
var (@VaadinDsl Component).alignment: Alignment
    get() = (parent as AbstractOrderedLayout).getComponentAlignment(this)
    set(value) = (parent as AbstractOrderedLayout).setComponentAlignment(this, value)

/**
 * Sets [AbsoluteLayout.ComponentPosition.zIndex]. Fails if this component is not nested inside [AbsoluteLayout]
 */
var (@VaadinDsl Component).zIndex: Int
    get() = absolutePosition.zIndex
    set(value) {
        absolutePosition.zIndex = value
    }

/**
 * Returns the [AbsoluteLayout.ComponentPosition] of this component. Fails if this component is not nested inside [AbsoluteLayout]
 */
val (@VaadinDsl Component).absolutePosition: AbsoluteLayout.ComponentPosition
    get() = (parent as AbsoluteLayout).getPosition(this)
var (@VaadinDsl AbsoluteLayout.ComponentPosition).top: Size
    get() = Size(topValue, topUnits)
    set(value) {
        topValue = value.size; topUnits = value.units
    }
var (@VaadinDsl AbsoluteLayout.ComponentPosition).bottom: Size
    get() = Size(bottomValue, bottomUnits)
    set(value) {
        bottomValue = value.size; bottomUnits = value.units
    }
var (@VaadinDsl AbsoluteLayout.ComponentPosition).left: Size
    get() = Size(leftValue, leftUnits)
    set(value) {
        leftValue = value.size; leftUnits = value.units
    }
var (@VaadinDsl AbsoluteLayout.ComponentPosition).right: Size
    get() = Size(rightValue, rightUnits)
    set(value) {
        rightValue = value.size; rightUnits = value.units
    }

/**
 * Sets all four margins to given value.
 */
var (@VaadinDsl AbstractOrderedLayout).isMargin: Boolean
    get() = margin.hasAll()
    set(value) { setMargin(value) }

/**
 * Returns component at given [index]. Optimized for [CssLayout] and [AbstractOrderedLayout]s, but works with any
 * [ComponentContainer].
 * @throws IndexOutOfBoundsException If the index is out of range.
 */
fun (@VaadinDsl ComponentContainer).getComponentAt(index: Int): Component = when (this) {
    is CssLayout -> this.getComponent(index)
    is AbstractOrderedLayout -> this.getComponent(index)
    else -> toList()[index]
}

/**
 * Removes a component at given [index] from this container. Optimized for [CssLayout] and [AbstractOrderedLayout]s, but works with any
 * [ComponentContainer].
 * @throws IndexOutOfBoundsException If the index is out of range.
 */
fun (@VaadinDsl ComponentContainer).removeComponentAt(index: Int) {
    removeComponent(getComponentAt(index))
}

/**
 * Returns an [IntRange] of the valid component indices for this container.
 */
val (@VaadinDsl ComponentContainer).indices: IntRange get() = 0..componentCount - 1

/**
 * Removes components with given indices.
 */
fun (@VaadinDsl ComponentContainer).removeComponentsAt(indexRange: IntRange) {
    if (indexRange.isEmpty()) {
    } else if (indexRange == indices) {
        removeAllComponents()
    } else if (this is CssLayout || this is AbstractOrderedLayout) {
        indexRange.reversed().forEach { removeComponentAt(it) }
    } else {
        removeAll(filterIndexed { index, _ -> index in indexRange })
    }
}

fun (@VaadinDsl ComponentContainer).removeAll(components: Iterable<Component>) = components.forEach { removeComponent(it) }
