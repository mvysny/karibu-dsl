package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.icon.VaadinIcon
import kotlin.apply
import kotlin.collections.asSequence
import kotlin.collections.joinToString
import kotlin.sequences.joinToString
import kotlinx.css.CssBuilder
import kotlinx.css.hyphenize

data class KLitRendererAttribute(
    val name: String,
    val value: String
) {
    override fun toString() = "$name=\"$value\""
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

fun <TSource> KLitRendererTagsBuilderA<TSource>.style(block: CssBuilder.() -> Unit) =
    KLitRendererAttribute("style", CssBuilder().apply(block).buildInlineStyleText())

fun <TSource> KLitRendererTagsBuilderA<TSource>.theme(vararg names: KLitRendererTheme) =
    KLitRendererAttribute("theme", names.joinToString(separator = " ") { it.name })

/**
 * --- Renderers https://vaadin.com/docs/latest/components/grid/renderers
 * "<vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>"
 */
fun <TSource> KLitRendererTagsBuilderA<TSource>.name(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("name", value.litItem)

fun <TSource> KLitRendererTagsBuilderA<TSource>.img(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("img", value.litItem)

fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: String) =
    KLitRendererAttribute("href", value)

fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("href", value.litItem)

fun <TSource> KLitRendererTagsBuilderA<TSource>.icon(value: VaadinIcon) =
    KLitRendererAttribute("icon", value.create().icon)
