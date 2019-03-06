package com.github.mvysny.karibudsl.v8

import com.vaadin.ui.*
import java.lang.IllegalStateException
import java.lang.reflect.Method

@VaadinDsl
fun (@VaadinDsl HasComponents).verticalLayout(block: (@VaadinDsl VerticalLayout).()->Unit = {}) = init(VerticalLayout(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).horizontalLayout(block: (@VaadinDsl HorizontalLayout).()->Unit = {}) = init(HorizontalLayout(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).formLayout(block: (@VaadinDsl FormLayout).()->Unit = {}) = init(FormLayout(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).absoluteLayout(block: (@VaadinDsl AbsoluteLayout).()->Unit = {}) = init(AbsoluteLayout(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).cssLayout(caption: String? = null, block: (@VaadinDsl CssLayout).()->Unit = {}) = init(CssLayout()) {
    this.caption = caption
    block()
}

@VaadinDsl
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
fun (@VaadinDsl HasComponents).addChild(child: Component) {
    @Suppress("DEPRECATION")
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
        is Composite -> compositeSetCompositionRoot(this, child)
        else -> throw IllegalArgumentException("Unsupported component container $this")
    }
}

/**
 * Removes [child] from this container. Does nothing if child has no parent or it is nested in some other container.
 */
fun (@VaadinDsl HasComponents).removeChild(child: Component) {
    @Suppress("DEPRECATION")
    when (this) {
        is ComponentContainer -> removeComponent(child)
        is SpecialContainer -> removeComponent(child)
        is SingleComponentContainer -> {
            if (content == child) {
                content = null
            }
        }
        is PopupView -> if (popupComponent == child) popupComponent = Label("")
        is AbstractSplitPanel -> when {
            firstComponent == child -> firstComponent = null
            secondComponent == child -> secondComponent = null
        }
        is Composite -> throw IllegalStateException("Cannot set root to null in Composite")
        else -> throw IllegalArgumentException("Unsupported component container $this")
    }
}

/**
 * Removes the component from its parent. Does nothing if the component is not currently nested inside of a parent.
 */
fun Component.removeFromParent() {
    parent?.removeChild(this)
}

/**
 * Workaround around [Composite.setCompositionRoot] being protected, so that [addChild] can insert components into [Composite].
 */
private val compositeSetCompositionRoot: Method = Composite::class.java.getDeclaredMethod("setCompositionRoot", Component::class.java).apply {
    isAccessible = true
}

/**
 * Removes all components from given container.
 */
fun (@VaadinDsl HasComponents).removeAllComponents() {
    @Suppress("DEPRECATION")
    when (this) {
        is ComponentContainer -> removeAllComponents()
        is SpecialContainer -> removeAllComponents()
        is SingleComponentContainer -> {
            content = null
        }
        is AbstractSplitPanel -> {
            firstComponent = null
            secondComponent = null
        }
        is PopupView -> popupComponent = Label("")
        is Composite -> throw IllegalStateException("Cannot set root to null in Composite")
        else -> throw IllegalArgumentException("Unsupported component container $this")
    }
}

/**
 * Returns the child component count for this container. O(1) for [ComponentContainer] and [SingleComponentContainer]; falls back to
 * counting items in [HasComponents.iterator] otherwise - O(n).
 */
fun HasComponents.getComponentCount() = when (this) {
    is ComponentContainer -> componentCount
    is SingleComponentContainer -> componentCount
    else -> count()
}

/**
 * Returns true if this container hosts no child components. Optimized for [ComponentContainer] and [SingleComponentContainer]. O(1).
 */
val HasComponents.isEmpty: Boolean get() = when (this) {
    is ComponentContainer -> componentCount == 0
    is SingleComponentContainer -> componentCount == 0
    else -> !iterator().hasNext()
}

/**
 * Returns true if this container contains any components. Optimized for [ComponentContainer] and [SingleComponentContainer]. O(1).
 */
val HasComponents.isNotEmpty: Boolean get() = !isEmpty

/**
 * Sets the expand ratio of this component with respect to its parent layout. See [AbstractOrderedLayout.setExpandRatio] for more details.
 *
 * Fails if this component is not nested inside [AbstractOrderedLayout].
 */
var (@VaadinDsl Component).expandRatio: Float
    get() = parent.asAbstractOrderedLayout().getExpandRatio(this)
    set(value) = parent.asAbstractOrderedLayout().setExpandRatio(this, value)

private fun HasComponents?.asAbstractOrderedLayout(): AbstractOrderedLayout {
    check(this != null) { "Setting expandRatio of a component only works when the component is already nested in the parent layout" }
    check(this !is FormLayout) { "Setting expandRatio of a component nested inside of a FormLayout does nothing (see the FormLayout class for more details), and it is therefore discouraged." }
    check(this is AbstractOrderedLayout) { "You're attempting to set expandRatio of a component but that's only supported when the component is nested inside VerticalLayout and HorizontalLayout, not for ${this.javaClass.simpleName}."}
    return this
}

/**
 * Sets the expand ratio of this component with respect to its parent layout. See [AbstractOrderedLayout.setExpandRatio] for more details.
 *
 * Fails if this component is not nested inside [AbstractOrderedLayout].
 */
var (@VaadinDsl Component).isExpanded: Boolean
    get() = expandRatio > 0f
    set(value) { expandRatio = if (value) 1f else 0f }

/**
 * Sets or gets alignment for this component with respect to its parent layout. Use
 * predefined alignments from Alignment class. Fails if the component is not nested inside Layout, that implement [Layout.AlignmentHandler]
 */
var (@VaadinDsl Component).alignment: Alignment
    get() = (parent as Layout.AlignmentHandler).getComponentAlignment(this)
    set(value) = (parent as Layout.AlignmentHandler).setComponentAlignment(this, value)

/**
 * Sets [AbsoluteLayout.ComponentPosition.zIndex]. Fails if this component is not nested inside [AbsoluteLayout]
 */
var (@VaadinDsl Component).zIndex: Int
    get() = absolutePosition.zIndex
    set(value) {
        absolutePosition.zIndex = value
    }

private fun HasComponents?.asAbsoluteLayout(): AbsoluteLayout {
    check(this != null) { "Setting absolutePosition of a component only works when the component is already nested in the parent layout" }
    check(this is AbsoluteLayout) { "You're attempting to set absolutePosition of a component but that's only supported when the component is nested inside VerticalLayout and HorizontalLayout, not for ${this.javaClass.simpleName}."}
    return this
}

/**
 * Returns the [AbsoluteLayout.ComponentPosition] of this component. Fails if this component is not nested inside [AbsoluteLayout]
 */
val (@VaadinDsl Component).absolutePosition: AbsoluteLayout.ComponentPosition
    get() = parent.asAbsoluteLayout().getPosition(this)
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
val (@VaadinDsl ComponentContainer).indices: IntRange get() = 0 until componentCount

/**
 * Removes components with given indices from the container.
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

/**
 * Removes [components] from the container.
 */
fun (@VaadinDsl ComponentContainer).removeAll(components: Iterable<Component>) = components.forEach { removeComponent(it) }
