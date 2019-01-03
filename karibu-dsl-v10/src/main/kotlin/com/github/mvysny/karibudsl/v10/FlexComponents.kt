package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout

/**
 * Creates a [Flex Layout](https://vaadin.com/elements/vaadin-ordered-layout/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).flexLayout(block: (@VaadinDsl FlexLayout).() -> Unit = {})
        = init(FlexLayout(), block)

/**
 * Creates a [Vertical Layout](https://vaadin.com/elements/vaadin-ordered-layout/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 *
 * The default content align is set to `content { align(left, top) }`.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).verticalLayout(block: (@VaadinDsl VerticalLayout).() -> Unit = {}) = init(VerticalLayout(), {
    content { align(left, top) }
    block()
})
/**
 * Creates a [Horizontal Layout](https://vaadin.com/elements/vaadin-ordered-layout/). See the HTML Examples link for a list
 * of possible alternative themes for the button; use [themes] to add more themes.
 *
 * The default content align is set to `content { align(left, baseline) }`.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).horizontalLayout(block: (@VaadinDsl HorizontalLayout).() -> Unit = {}) = init(HorizontalLayout(), {
    content { align(left, baseline) }
    block()
})

/**
 * Sets the component's [FlexComponent.getFlexGrow]. Only works when the component is nested in a [FlexComponent].
 *
 * This defines the ability for a flex item to grow if necessary. It accepts a unitless value that serves as a proportion. It dictates what amount of the available space inside the flex container the item should take up.
 *
 * If all items have flex-grow set to 1, the remaining space in the container will be distributed equally to all children. If one of the children has a value of 2, the remaining space would take up twice as much space as the others (or it will try to, at least).
 *
 * Negative numbers are invalid.
 *
 * Get more information at [Guide to Flexbox](https://css-tricks.com/snippets/css/a-guide-to-flexbox/)
 *
 * Warning: in case of [Grid.Column] it returns/sets the value of [Grid.Column.setFlexGrow].
 */
var (@VaadinDsl Component).flexGrow: Double
    get() {
        if (this is Grid.Column<*>) return this.flexGrow.toDouble()
        val value = element.style.get("flexGrow")
        return if (value.isNullOrBlank()) 0.0 else value.toDouble()
    }
    set(value) {
        when {
            this is Grid.Column<*> -> this.flexGrow = value.toInt()
            value == 0.0 -> element.style.remove("flexGrow")
            value > 0.0 -> element.style.set("flexGrow", value.toString())
            else -> throw IllegalArgumentException("Flex grow property cannot be negative: $flexGrow")
        }
    }

/**
 * Checks if the component expands when nested in [FlexComponent]. Alias for setting [flexGrow] to 1.0; see [flexGrow] for more information.
 */
var (@VaadinDsl Component).isExpand: Boolean
    get() = flexGrow > 0
    set(value) { flexGrow = if (value) 1.0 else 0.0 }

/**
 * The `flex-shrink` CSS property specifies the flex shrink factor of a flex item. Flex items will shrink to fill the container according
 * to the `flex-shrink` number, when the default size of flex items is larger than the flex container.
 *
 * Get more information at [flex-shrink](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-shrink)
 */
var (@VaadinDsl Component).flexShrink: Double
    get() = element.style.get("flexShrink")?.toDouble() ?: 1.0
    set(value) {
        when (value) {
            1.0 -> element.style.remove("flexShrink")
            else -> element.style.set("flexShrink", value.toString())
        }
    }

/**
 * This defines the default size of an element before the remaining space is distributed. It can be a length (e.g. `20%`, `5rem`, etc.) or a keyword.
 * The `auto` keyword means "look at my width or height property".
 * The `content` keyword means "size it based on the item's content" - this keyword isn't well supported yet, so it's hard to test and harder
 * to know what its brethren `max-content`, `min-content`, and `fit-content` do.
 *
 * If set to `0`, the extra space around content isn't factored in. If set to `auto`, the extra space is distributed based on its flex-grow value.
 */
var (@VaadinDsl Component).flexBasis: String?
    get() = element.style.get("flexBasis")
    set(value) { element.style.set("flexBasis", value) }

/**
 * This allows the default alignment (or the one specified by [FlexComponent.getAlignItems] to be overridden for individual flex items.
 *
 * Please see the [FlexComponent.getAlignItems] explanation to understand the available values.
 */
var (@VaadinDsl Component).alignSelf: FlexComponent.Alignment
    get() = (parent.get() as FlexComponent<*>).getAlignSelf(this)
    set(value) { (parent.get() as FlexComponent<*>).setAlignSelf(value, this) }

/**
 * This aligns a flex container's lines within when there is extra space in the cross-axis, similar to how [FlexComponent.getJustifyContentMode] aligns individual
 * items within the main-axis.
 */
var (@VaadinDsl FlexLayout).alignContent: AlignContent
    get() = (this as FlexComponent<*>).alignContent
    set(value) { (this as FlexComponent<*>).alignContent = value }

// private since it would confuse users of VerticalLayout and HorizontalLayout which also implement FlexComponent.
private var (@VaadinDsl FlexComponent<*>).alignContent: AlignContent
    get() = AlignContent.fromFlexValue(element.style.get("alignContent"))
    set(value) { element.style.set("alignContent", value.flexValue) }

enum class AlignContent(val flexValue: String) {
    /**
     * lines packed to the start of the container
     */
    Start("flex-start"),
    /**
     * lines packed to the end of the container
     */
    End("flex-end"),
    /**
     * lines packed to the center of the container
     */
    Center("center"),
    /**
     * lines evenly distributed; the first line is at the start of the container while the last one is at the end
     */
    SpaceBetween("space-between"),
    /**
     * lines evenly distributed with equal space around each line
     */
    SpaceAround("space-around"),
    /**
     * (default): lines stretch to take up the remaining space
     */
    Stretch("stretch");

    companion object {
        fun fromFlexValue(flexValue: String?): AlignContent = values().firstOrNull { it.flexValue == flexValue } ?: Stretch
    }
}

/**
 * Only to be used with children of [HorizontalLayout]: Gets or sets a vertical alignment for individual components inside the [HorizontalLayout].
 * This individual alignment for the component overrides any alignment set
 * at the [HorizontalLayout.setDefaultVerticalComponentAlignment].
 *
 * It effectively sets the `"alignSelf"` style value.
 *
 * The default alignment for individual components is
 * [FlexComponent.Alignment.AUTO].
 *
 * It's the same as the [Component.alignSelf] property.
 *
 * Note: this only works with [HorizontalLayout] - it will throw an exception if this component is nested in any other type of layout.
 */
var (@VaadinDsl Component).verticalAlignSelf: FlexComponent.Alignment
    get() = (parent.get() as HorizontalLayout).getVerticalComponentAlignment(this)
    set(value) { (parent.get() as HorizontalLayout).setVerticalComponentAlignment(value, this) }

/**
 * Only to be used with children of [VerticalLayout]: gets or sets a horizontal alignment for individual components inside the [VerticalLayout].
 * This individual alignment for the component overrides any alignment set
 * at the [VerticalLayout.setDefaultHorizontalComponentAlignment].
 *
 * The default alignment for individual components is [FlexComponent.Alignment.AUTO].
 *
 * It's the same as the [FlexComponent.setAlignSelf] method.
 *
 * Note: this only works with [VerticalLayout] - it will throw an exception if this component is nested in any other type of layout.
 */
var (@VaadinDsl Component).horizontalAlignSelf: FlexComponent.Alignment
    get() = (parent.get() as VerticalLayout).getHorizontalComponentAlignment(this)
    set(value) { (parent.get() as VerticalLayout).setHorizontalComponentAlignment(value, this) }

class HorizontalLayoutContent(private val owner: HorizontalLayout) {
    /**
     * This aligns the container's components within when there is extra space. See [justify-content](https://css-tricks.com/snippets/css/a-guide-to-flexbox/#article-header-id-6)
     * for more details.
     *
     * Note: contrary to [verticalAlignment] this setting can not be overridden by individual children. This is a limitation of
     * the flex layout. Calling [horizontalAlignSelf] on children will throw an exception.
     */
    var horizontalAlignment: FlexComponent.JustifyContentMode
        get() = owner.justifyContentMode
        set(value) { owner.justifyContentMode = value }

    /**
     * The default vertical alignment to be used by all components without
     * individual alignments inside the layout. Individual components can be
     * aligned by using the [verticalAlignSelf] property.
     *
     * It effectively sets the `"alignItems"` style value.
     *
     * The default alignment is [top].
     *
     * It's the same as the [HorizontalLayout.getDefaultVerticalComponentAlignment] method.
     */
    var verticalAlignment: FlexComponent.Alignment
        get() = owner.defaultVerticalComponentAlignment
        set(value) { owner.defaultVerticalComponentAlignment = value }

    /**
     * Items are positioned to the top of the available space.
     */
    val top inline get() = FlexComponent.Alignment.START

    /**
     * Children are positioned to the bottom of the available space.
     */
    val bottom inline get() = FlexComponent.Alignment.END

    /**
     * Children are positioned vertically in the middle of the available space.
     */
    val middle inline get() = FlexComponent.Alignment.CENTER

    /**
     * Children are stretched to fit the container's height.
     */
    val stretch inline get() = FlexComponent.Alignment.STRETCH

    /**
     * Children are positioned at the baseline of the container.
     */
    val baseline inline get() = FlexComponent.Alignment.BASELINE

    /**
     * Children are positioned to the left of the available space.
     */
    val left inline get() = FlexComponent.JustifyContentMode.START

    /**
     * Children are positioned to the right of the available space.
     */
    val right inline get() = FlexComponent.JustifyContentMode.END

    /**
     * Children are positioned at the horizontal center of the container.
     */
    val center inline get() = FlexComponent.JustifyContentMode.CENTER

    /**
     * Children are positioned with space between the lines.
     */
    val between inline get() = FlexComponent.JustifyContentMode.BETWEEN

    /**
     * Children are positioned with space before, between, and after the lines.
     */
    val around inline get() = FlexComponent.JustifyContentMode.AROUND

    /**
     * Children have equal space around them.
     */
    val evenly inline get() = FlexComponent.JustifyContentMode.EVENLY

    /**
     * Centers all children inside of the layout. Equal to setting [center] and [middle].
     */
    fun center() { align(center, middle) }

    /**
     * Align the children as specified by the [horizontalAlignment] and [verticalAlignment].
     */
    fun align(horizontalAlignment: FlexComponent.JustifyContentMode, verticalAlignment: FlexComponent.Alignment) {
        this.horizontalAlignment = horizontalAlignment
        this.verticalAlignment = verticalAlignment
    }
}

/**
 * Configures the general rules for positioning of child components inside of this [HorizontalLayout].
 *
 * Example of usage:
 * ```
 * horizontalLayout {
 *   content { align(right, middle) }
 * }
 * ```
 * Important notes:
 * * [HorizontalLayout] only supports one row of components; if you have multiple rows you need to use [FlexLayout].
 * * Never use [com.vaadin.flow.component.HasSize.setSizeFull] nor set the [com.vaadin.flow.component.HasSize.setWidth] to `100%` - it will
 * not work as you expect. With Vaadin 8 the child would fill the slot allocated by HorizontalLayout. However with Vaadin 10 and flex layout
 * there are no slots; setting the width to `100%` would make the component match the width of parent - it would set it to be as wide as
 * the HorizontalLayout is.
 *
 * To alter the layout further, call the following properties on children:
 *
 * * Most important: [flexGrow] (and its brother [isExpand]) expands that particular child to take up all of the remaining space. The child
 * is automatically enlarged.
 * * [verticalAlignSelf] to align child vertically; it is not possible to align particular child horizontally
 * * [flexShrink] - when there is not enough room for all children then they are shrank
 * * [flexBasis]
 */
@VaadinDsl
inline fun (@VaadinDsl HorizontalLayout).content(block: HorizontalLayoutContent.()->Unit) {
    // the only reason why this is done as a builder, is that the HorizontalLayoutContent.* constants are constrained to the block
    // and not defined as global variables.
    HorizontalLayoutContent(this).block()
}

@VaadinDsl
class VerticalLayoutContent(private val owner: VerticalLayout) {
    /**
     * The default horizontal alignment to be used by all components without
     * individual alignments inside the layout. Individual components can be
     * aligned by using the [horizontalAlignSelf] property.
     *
     * It effectively sets the `"alignItems"` style value.
     *
     * The default alignment is [left].
     *
     * It's the same as the [VerticalLayout.setDefaultHorizontalComponentAlignment] method.
     */
    var horizontalAlignment: FlexComponent.Alignment
        get() = owner.defaultHorizontalComponentAlignment
        set(value) { owner.defaultHorizontalComponentAlignment = value }

    /**
     * This aligns the container's components within when there is extra space. See [justify-content](https://css-tricks.com/snippets/css/a-guide-to-flexbox/#article-header-id-6)
     * for more details.
     *
     * Note: contrary to [horizontalAlignment] this setting can not be overridden by individual children. This is a limitation of
     * the flex layout. Calling [verticalAlignSelf] on children will throw an exception.
     */
    var verticalAlignment: FlexComponent.JustifyContentMode
        get() = owner.justifyContentMode
        set(value) { owner.justifyContentMode = value }

    /**
     * Children are positioned to the left of the available space.
     */
    val left inline get() = FlexComponent.Alignment.START

    /**
     * Children are positioned to the right of the available space.
     */
    val right inline get() = FlexComponent.Alignment.END

    /**
     * Children are positioned at the horizontal center of the container.
     */
    val center inline get() = FlexComponent.Alignment.CENTER

    /**
     * Children are stretched to fit the container's width.
     */
    val stretch inline get() = FlexComponent.Alignment.STRETCH

    /**
     * Items are positioned to the top of the available space.
     */
    val top inline get() = FlexComponent.JustifyContentMode.START

    /**
     * Children are positioned to the bottom of the available space.
     */
    val bottom inline get() = FlexComponent.JustifyContentMode.END

    /**
     * Children are positioned vertically in the middle of the available space.
     */
    val middle inline get() = FlexComponent.JustifyContentMode.CENTER

    /**
     * Items are positioned with space between the lines.
     */
    val between inline get() = FlexComponent.JustifyContentMode.BETWEEN

    /**
     * Items are positioned with space before, between, and after the lines.
     */
    val around inline get() = FlexComponent.JustifyContentMode.AROUND

    /**
     * Items have equal space around them.
     */
    val evenly inline get() = FlexComponent.JustifyContentMode.EVENLY

    /**
     * Centers all children inside of the layout. Equal to setting [center] and [middle].
     */
    fun center() { align(center, middle) }

    /**
     * Align the children as specified by the [horizontalAlignment] and [verticalAlignment].
     */
    fun align(horizontalAlignment: FlexComponent.Alignment, verticalAlignment: FlexComponent.JustifyContentMode) {
        this.horizontalAlignment = horizontalAlignment
        this.verticalAlignment = verticalAlignment
    }
}

/**
 * Configures the general rules for positioning of child components inside of this [VerticalLayout].
 *
 * Example of usage:
 * ```
 * verticalLayout {
 *   content { align(right, middle) }
 * }
 * ```
 * Important notes:
 * * [VerticalLayout] only supports one row of components; if you have multiple columns you need to use [FlexLayout].
 * * Never use [com.vaadin.flow.component.HasSize.setSizeFull] nor set the [com.vaadin.flow.component.HasSize.setHeight] to `100%` - it will
 * not work as you expect. With Vaadin 8 the child would fill the slot allocated by VerticalLayout. However with Vaadin 10 and flex layout
 * there are no slots; setting the height to `100%` would make the component match the height of parent - it would set it to be as tall as
 * the VerticalLayout is.
 *
 * To alter the layout further, call the following properties on children:
 *
 * * Most important: [flexGrow] (and its brother [isExpand]) expands that particular child to take up all of the remaining space. The child
 * is automatically enlarged.
 * * [verticalAlignSelf] to align child vertically; it is not possible to align particular child horizontally
 * * [flexShrink] - when there is not enough room for all children then they are shrank
 * * [flexBasis]
 */
@VaadinDsl
inline fun (@VaadinDsl VerticalLayout).content(block: VerticalLayoutContent.()->Unit) {
    // the only reason why this is done as a builder, is that the VerticalLayoutContent.* constants are constrained to the block
    // and not defined as global variables.
    VerticalLayoutContent(this).block()
}

/**
 * By default, flex items will all try to fit onto one line. You can change that and allow the items to wrap as needed with this property.
 */
enum class FlexWrap(val flexValue: String) {
    /**
     * nowrap (default): all flex items will be on one line
     */
    Nowrap("nowrap"),
    /**
     * wrap: flex items will wrap onto multiple lines, from top to bottom.
     */
    Wrap("wrap"),
    /**
     * wrap-reverse: flex items will wrap onto multiple lines from bottom to top.
     */
    WrapReverse("wrap-reverse");
    companion object {
        fun findByFlex(flexValue: String?) = when {
            flexValue == null -> null
            else -> values().firstOrNull { it.flexValue == flexValue }
        }
    }
}

/**
 * By default, flex items will all try to fit onto one line. You can change that and allow the items to wrap as needed with this property.
 */
var (@VaadinDsl FlexLayout).flexWrap: FlexWrap
    get() = FlexWrap.findByFlex(element.style.get("flexWrap")) ?: FlexWrap.Wrap
    set(value) {
        when (value) {
            FlexWrap.Nowrap -> element.style.remove("flexWrap")
            else -> element.style.set("flexWrap", value.flexValue)
        }
    }
