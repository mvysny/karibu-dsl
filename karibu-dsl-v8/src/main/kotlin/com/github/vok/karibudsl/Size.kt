package com.github.vok.karibudsl

import com.vaadin.server.Sizeable
import com.vaadin.ui.Component
import java.io.Serializable

/**
 * Represents a Vaadin component width or height.
 * @param size the size, may be negative for undefined/wrapContent size.
 * @param units states the size units.
 */
data class Size(val size: Float, val units: Sizeable.Unit) : Serializable {
    /**
     * true if this size is set to 100% and the component fills its parent in this dimension.
     */
    val isFillParent: Boolean
        get() = size >= 100 && units == Sizeable.Unit.PERCENTAGE
    /**
     * Same as [isFillParent], it's here just to keep in sync with Vaadin terminology ([Component.setSizeFull]).
     */
    val isFull: Boolean
        get() = isFillParent
    /**
     * true if this component wraps its content in this dimension (size is -1px).
     */
    val isWrapContent: Boolean
        get() = size < 0
    /**
     * Same as [isWrapContent], it's here just to keep in sync with Vaadin terminology ([Component.setSizeUndefined]).
     */
    val isUndefined: Boolean
        get() = isWrapContent

    override fun toString() = "$size${units.symbol}"

    val isPixels: Boolean get() = units == Sizeable.Unit.PIXELS

    val isPercent: Boolean get() = units == Sizeable.Unit.PERCENTAGE

    /**
     * True if the component is of fixed size, e.g. 48px, 20em etc. When the size is fixed,
     * it cannot be [isWrapContent] nor [isFillParent]
     */
    val isFixed: Boolean get() = !isPercent && size >= 0
}

/**
 * Tells the component to wrap-content in particular direction. Typing `w = wrapContent` is equal to calling [Sizeable.setWidthUndefined]
 * or setWidth(null) or setWidth(-1, Sizeable.Unit.PIXELS).
 */
val wrapContent = (-1).px
/**
 * Tells the component to fill-parent in particular direction. Typing `w = fillParent` is equal to calling setWidth("100%").
 */
val fillParent = 100.perc

val Int.px: Size
    get() = toFloat().px
val Float.px: Size
    get() = Size(this, Sizeable.Unit.PIXELS)
val Int.perc: Size
    get() = toFloat().perc
val Float.perc: Size
    get() = Size(this, Sizeable.Unit.PERCENTAGE)
var Component.w: Size
    get() = Size(width, widthUnits)
    set(value) {
        setWidth(value.size, value.units)
    }
var (@VaadinDsl Component).h: Size
    get() = Size(height, heightUnits)
    set(value) = setHeight(value.size, value.units)

/**
 * true if both the component width and height is set to 100%
 */
val (@VaadinDsl Component).isSizeFull: Boolean
    get() = w.isFillParent && h.isFillParent
