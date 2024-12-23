package com.github.mvysny.karibudsl.v10

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.KLitRendererBuilderA.Property
import kotlin.apply
import kotlin.collections.asSequence
import kotlin.collections.joinToString
import kotlin.sequences.joinToString
import kotlin.text.isNotEmpty
import kotlin.text.trimIndent

@VaadinDsl
interface KLitRendererTagsBuilderA<TSource> {

    operator fun Property<TSource>.unaryPlus()

    operator fun String.unaryPlus()

    val spacing get() = KLitRendererTheme.spacing
    val padding get() = KLitRendererTheme.padding

    fun horizontalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    fun verticalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    fun span(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    fun div(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    fun anchor(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    fun vaadinIcon(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )
}

class KLitRendererTagsBuilder<TSource>(

    private val litRendererBuilder: KLitRendererBuilderA<TSource>,

    private val tagName: String = "",
    private val attributes: String = "",
    private val propertyName: String = "",
) :
    KLitRendererTagsBuilderA<TSource> {

    private val children = mutableListOf<KLitRendererTagsBuilderA<TSource>>()

    override fun toString(): String {

        return when {
            tagName.isNotEmpty() -> """
            <$tagName $attributes>
            ${children.joinToString(separator = "")}
            </$tagName>
        """.trimIndent()

            propertyName.isNotEmpty() -> propertyName

            else -> children.joinToString(separator = "")
        }

    }

    override fun Property<TSource>.unaryPlus() {
        children.add(
            KLitRendererTagsBuilder(
                litRendererBuilder,
                propertyName = (litRendererBuilder as KLitRendererBuilder).propertyName(this)
            )
        )
    }

    override fun String.unaryPlus() {
        children.add(
            KLitRendererTagsBuilder(
                litRendererBuilder,
                propertyName = this
            )
        )
    }

    private fun addTag(
        tagName: String,
        attributes: Sequence<KLitRendererAttribute>,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        children.add(
            KLitRendererTagsBuilder(
                litRendererBuilder,
                tagName,
                attributes.joinToString(separator = " ")
            ).apply(block)
        )
    }

    override fun horizontalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) = addTag("vaadin-horizontal-layout", attributes.asSequence(), block)

    override fun verticalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) = addTag("vaadin-vertical-layout", attributes.asSequence(), block)

    override fun span(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) = addTag("span", attributes.asSequence(), block)

    override fun div(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) = addTag("div", attributes.asSequence(), block)

    override fun anchor(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) = addTag("a", attributes.asSequence(), block)

    override fun vaadinIcon(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) = addTag("vaadin-icon", attributes.asSequence(), block)

}

