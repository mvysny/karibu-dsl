package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.shared.ThemeVariant
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

public fun <TSource> KLitRendererTagsBuilderA<TSource>.themeVariant(vararg names: ThemeVariant) =
    KLitRendererAttribute("theme", names.joinToString(separator = " ") { it.variantName })

public fun <TSource> KLitRendererTagsBuilderA<TSource>.cssClass(vararg names: String) =
    KLitRendererAttribute("class", names.joinToString(separator = " "))

public fun <TSource> KLitRendererTagsBuilderA<TSource>.id(value: String) =
    KLitRendererAttribute("id", value)

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

/**
 * <vaadin-icon icon="vaadin:edit" slot="prefix"></vaadin-icon>
 *
 * Example of use:
 * ```kotlin
 * val columnIcon = "columnName" { row ->
 *                         if (row.isNew)
 *                             VaadinIcon.PLUS_CIRCLE.createIcon().icon
 *                         else
 *                             VaadinIcon.EDIT.createIcon().icon
 *                     }
 *  +columnIcon
 * ```
 */
public fun <TSource> KLitRendererTagsBuilderA<TSource>.icon(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("icon", value.litItem)

/**
 * Example of use:
 * ```kotlin
 *  icon(VaadinIcon.EDIT.create())
 * ```
 */
public fun <TSource> KLitRendererTagsBuilderA<TSource>.icon(value: Icon) =
    KLitRendererAttribute("icon", value.icon)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.img(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("img", value.litItem)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: String) =
    KLitRendererAttribute("href", value)

public fun <TSource> KLitRendererTagsBuilderA<TSource>.href(value: KLitRendererBuilderA.Property<TSource>) =
    KLitRendererAttribute("href", value.litItem)


/*
LitRenderer.of("<button @click=${handleClick}>Click me</button>")
 */
public fun <TSource> KLitRendererTagsBuilderA<TSource>.click(value: KLitRendererBuilderA.Function<TSource>) =
    KLitRendererAttribute("@click", value.litItem)
