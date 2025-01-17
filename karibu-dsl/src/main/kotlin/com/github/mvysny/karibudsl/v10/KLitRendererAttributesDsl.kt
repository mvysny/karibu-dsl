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
 * [buildInlineStyleText] builds text value representation of the [style] attribute.
 * Example of usage:
 * ```
 * verticalLayout(style { lineHeight = +KLumoLineHeight.XS; }) {
 *   ...
 * }
 * ```
 * Generated html code:
 * ```
 * <vaadin-vertical-layout style="line-height: var(----lumo-font-size-xs);"></vaadin-vertical-layout>
 * ```
 */
private fun CssBuilder.buildInlineStyleText() =
    declarations.asSequence().joinToString(separator = "; ") { (key, value) ->
        "${key.hyphenize()}: $value"
    }

public fun <TSource> KLitRendererTagsBuilderA<TSource>.style(value: String) =
    KLitRendererAttribute("style", value)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.style(block: CssBuilder.() -> Unit) =
    KLitRendererAttribute("style", CssBuilder().apply(block).buildInlineStyleText())

public fun <TSource> KLitRendererTagsBuilderA<TSource>.theme(vararg names: KLitRendererTheme) =
    KLitRendererAttribute("theme", names.joinToString(separator = " ") { it.name })

public fun <TSource> KLitRendererTagsBuilderA<TSource>.themeVariant(vararg names: ThemeVariant) =
    KLitRendererAttribute("theme", names.joinToString(separator = " ") { it.variantName })

public fun <TSource> KLitRendererTagsBuilderA<TSource>.cssClass(vararg names: String) =
    KLitRendererAttribute("class", names.joinToString(separator = " "))

public fun <TSource> KLitRendererTagsBuilderA<TSource>.id(value: String) =
    KLitRendererAttribute("id", value)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.name(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("name", value.litItem)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.src(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("src", value.litItem)

/**
 * <vaadin-icon icon="vaadin:edit" slot="prefix"></vaadin-icon>
 *
 * Example of use:
 * ```kotlin
 * val columnIcon by property { row ->
 *                         if (row.isNew)
 *                             VaadinIcon.PLUS_CIRCLE.createIcon().icon
 *                         else
 *                             VaadinIcon.EDIT.createIcon().icon
 *                     }
 *  ...
 *  icon(+columnIcon)
 *  ...
 * ```
 */
public fun <TSource> KLitRendererTagsBuilderA<TSource>.icon(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("icon", value.litItem)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.icon(value: Icon) =
    KLitRendererAttribute("icon", value.icon)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.img(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("img", value.litItem)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: String) =
    KLitRendererAttribute("href", value)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("href", value.litItem)


public fun <TSource> KLitRendererTagsBuilderA<TSource>.click(value: KLitRendererBuilderA.Function<TSource>) =
    KLitRendererAttribute("@click", value.litItem)
