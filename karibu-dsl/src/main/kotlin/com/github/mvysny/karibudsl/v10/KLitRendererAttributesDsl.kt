package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.icon.VaadinIcon
import kotlinx.css.CssBuilder
import kotlinx.css.hyphenize

public data class KLitRendererAttribute(
    val name: String,
    val value: String
) {
    override fun toString(): String = "$name=\"$value\""
}

/**
 * style="align-items: center;"
 * style="line-height: var(--lumo-line-height-m);"
 * style="font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);"
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

/**
 * --- Renderers https://vaadin.com/docs/latest/components/grid/renderers
 * "<vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>"
 */
public fun <TSource> KLitRendererTagsBuilderA<TSource>.name(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("name", value.litItem)

/**
 * "<img src=${item.image} />"
 * "<div><img style='height: 80px; width: 80px;' src=${item.image} alt=${item.name}></div>"
 */
public fun <TSource> KLitRendererTagsBuilderA<TSource>.src(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("src", value.litItem)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.img(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("img", value.litItem)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: String) =
    KLitRendererAttribute("href", value)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("href", value.litItem)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.icon(value: VaadinIcon) =
    KLitRendererAttribute("icon", value.create().icon)
