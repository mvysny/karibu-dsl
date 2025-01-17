package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.shared.ThemeVariant
import kotlinx.css.CssBuilder
import kotlinx.css.hyphenize

/**
 * [KLitRendererAttribute] allows you to insert attribute of a html tag
 * [name] - the name of attribute
 * [value] - the value of attribute
 * [toString] - a html representation of the attribute
 * Example of usage:
 * ```kotlin
 * horizontalLayout(theme(spacing)) {
 *   ...
 * }
 * ```
 * Generated html code:
 * ```
 * <vaadin-horizontal-layout theme="spacing"></vaadin-horizontal-layout>
 * ```
 */
public data class KLitRendererAttribute(
    val name: String,
    val value: String
) {
    override fun toString(): String = "$name=\"$value\""
}

/**
 * @property TSource the item type in the grid/LitRenderer.
 */
@VaadinDsl
public class KLitRendererAttributeBuilder<TSource> {
    private val attributes: MutableList<KLitRendererAttribute> = mutableListOf<KLitRendererAttribute>()
    public val spacing: KLitRendererTheme get() = KLitRendererTheme.spacing
    public val padding: KLitRendererTheme get() = KLitRendererTheme.padding
    override fun toString(): String = attributes.joinToString(" ")
    public fun add(attribute: KLitRendererAttribute) {
        attributes.add(attribute)
    }
}

/**
 * [buildInlineStyleText] builds text value representation of the [style] attribute.
 * Example of usage:
 * ```
 * verticalLayout(style { lineHeight = +KLumoLineHeight.XS; }) {
 *   ...
 * }
 * ```
 * Generated html code:
 * ```
 * <vaadin-vertical-layout style="line-height: var(--lumo-line-height-xs)"></vaadin-vertical-layout>
 * ```
 */
private fun CssBuilder.buildInlineStyleText() =
    declarations.asSequence().joinToString(separator = "; ") { (key, value) ->
        "${key.hyphenize()}: $value"
    }

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.style(value: String) {
    add(KLitRendererAttribute("style", value))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.style(block: CssBuilder.() -> Unit) {
    add(
        KLitRendererAttribute(
            "style",
            CssBuilder().apply(block).buildInlineStyleText()
        )
    )
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.theme(vararg names: KLitRendererTheme) {
    add(KLitRendererAttribute(
        "theme",
        names.joinToString(separator = " ") { it.name }))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.themeVariant(vararg names: ThemeVariant) {
    add(KLitRendererAttribute(
        "theme",
        names.joinToString(separator = " ") { it.variantName }))
}

/**
 * A `class` attribute added to the parent element.
 */
@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.cssClass(vararg names: String) {
    add(KLitRendererAttribute("class", names.joinToString(separator = " ")))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.id(value: String) {
    add(KLitRendererAttribute("id", value))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.name(value: KLitRendererBuilder.Property<TSource>) {
    add(KLitRendererAttribute("name", value.litItem))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.src(value: KLitRendererBuilder.Property<TSource>) {
    add(KLitRendererAttribute("src", value.litItem))
}

/**
 * <vaadin-icon icon="vaadin:edit" slot="prefix"></vaadin-icon>
 *
 * Example of use:
 * ```kotlin
 * val columnIcon by property { row ->
 *   if (row.isNew)
 *     VaadinIcon.PLUS_CIRCLE.createIcon().icon
 *   else
 *     VaadinIcon.EDIT.createIcon().icon
 * }
 * ...
 * icon(+columnIcon)
 * ...
 * ```
 */
@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.icon(value: KLitRendererBuilder.Property<TSource>) {
    add(KLitRendererAttribute("icon", value.litItem))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.icon(value: Icon) {
    add(KLitRendererAttribute("icon", value.icon))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.img(value: KLitRendererBuilder.Property<TSource>) {
    add(KLitRendererAttribute("img", value.litItem))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.href(value: String) {
    add(KLitRendererAttribute("href", value))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.href(value: KLitRendererBuilder.Property<TSource>) {
    add(KLitRendererAttribute("href", value.litItem))
}

@VaadinDsl
public fun <TSource> KLitRendererAttributeBuilder<TSource>.click(value: KLitRendererBuilder.Function<TSource>) {
    add(KLitRendererAttribute("@click", value.litItem))
}
