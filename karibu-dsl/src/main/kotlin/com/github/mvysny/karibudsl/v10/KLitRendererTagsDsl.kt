package com.github.mvysny.karibudsl.v10

import com.github.mvysny.karibudsl.v10.KLitRendererBuilderA.Property

@VaadinDsl
public interface KLitRendererTagsBuilderA<TSource> {

    public operator fun Property<TSource>.unaryPlus()
    public operator fun KLitRendererBuilderA.Function<TSource>.unaryPlus()

    public operator fun String.unaryPlus()

    public val spacing: KLitRendererTheme get() = KLitRendererTheme.spacing
    public val padding: KLitRendererTheme get() = KLitRendererTheme.padding

    public fun horizontalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    public fun verticalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    public fun span(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    public fun div(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    public fun anchor(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    public fun icon(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit = {}
    )

    public fun button(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    )

    public fun img(
        vararg attributes: KLitRendererAttribute,
    )

    public fun avatar(
        vararg attributes: KLitRendererAttribute,
    )

}

public class KLitRendererTagsBuilder<TSource>(
    private val litRendererBuilder: KLitRendererBuilderA<TSource>,
    private val tagName: String = "",
    private val attributes: String = "",
    private val propertyName: String = "",

    /**
     * [What are Self Closing Tags in HTML?] (https://www.scaler.com/topics/self-closing-tags-in-html/)
     *  A traditional HTML tag such as <p>, <div>, <section>, etc., had an opening tag and a closing tag
     *  However, due to their fundamental structure, void components in HTML, such as images and links,
     *  do not technically require closing tags.
     *  Images and links cannot have content - they are pointers to an element installed on the website.
     * See also [img]
     */
    private val selfClosing: Boolean = false,
) :
    KLitRendererTagsBuilderA<TSource> {

    private val children = mutableListOf<KLitRendererTagsBuilderA<TSource>>()

    override fun toString(): String = when {
        tagName.isNotEmpty() ->
            if (!selfClosing)
                "<$tagName $attributes>${children.joinToString(separator = "")}</$tagName>"
            else
                "<$tagName $attributes/>"
        propertyName.isNotEmpty() -> propertyName
        else -> children.joinToString(separator = "")
    }

    override fun Property<TSource>.unaryPlus() {
        children.add(
            KLitRendererTagsBuilder(
                litRendererBuilder,
                propertyName = (litRendererBuilder as KLitRendererBuilder).propertyName(this)
            )
        )
    }

    override fun KLitRendererBuilderA.Function<TSource>.unaryPlus() {
        children.add(
            KLitRendererTagsBuilder(
                litRendererBuilder,
                propertyName = (litRendererBuilder as KLitRendererBuilder).functionName(this)
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
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit,
        selfClosing: Boolean = false,
    ) {
        children.add(
            KLitRendererTagsBuilder(
                litRendererBuilder,
                tagName,
                attributes.joinToString(separator = " "),
                selfClosing = selfClosing
            ).apply(block)
        )
    }

    override fun horizontalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        addTag("vaadin-horizontal-layout", attributes.asSequence(), block)
    }

    override fun verticalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        addTag("vaadin-vertical-layout", attributes.asSequence(), block)
    }

    override fun span(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        addTag("span", attributes.asSequence(), block)
    }

    override fun div(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        addTag("div", attributes.asSequence(), block)
    }

    override fun anchor(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        addTag("a", attributes.asSequence(), block)
    }

    override fun icon(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        addTag("vaadin-icon", attributes.asSequence(), block)
    }

    override fun button(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilderA<TSource>.() -> Unit
    ) {
        addTag("vaadin-button", attributes.asSequence(), block)
    }

    /**
     * [selfClosing] = true
     *
     * [The Image Tag] (https://www.understandingcode.com/image-tag)
     * The <img /> Tag
     *  This tag is different from other tags, in that it has no closing tag.
     *  It is called a self-closing tag, which means that there is just a slash at the end of the opening tag (ex. <img />)
     * [img tag and / and a lie] (https://www.codecademy.com/forum_questions/5236c2c9f10c607ef4000bc0)
     *
     * Example of usage:
     * ```kotlin
     * img(src(photo))
     *   ...
     * }
     * ```
     * Generated html code:
     * ```
     * "<img src=${item.photo} />"
     * ```
     */
    override fun img(
        vararg attributes: KLitRendererAttribute,
    ) {
        addTag("img", attributes.asSequence(), { }, selfClosing = true)
    }

    override fun avatar(
        vararg attributes: KLitRendererAttribute,
    ) {
        addTag("vaadin-avatar", attributes.asSequence(), { })
    }

}

