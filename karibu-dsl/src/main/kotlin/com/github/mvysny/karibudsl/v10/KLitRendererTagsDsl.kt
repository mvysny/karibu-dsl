package com.github.mvysny.karibudsl.v10

import com.github.mvysny.karibudsl.v10.KLitRendererBuilder.Property
import com.vaadin.flow.data.renderer.LitRenderer

/**
 * Builds the [LitRenderer] template expression.
 * @param TSource the item rendered by the [LitRenderer].
 */
@VaadinDsl
public sealed class KLitRendererTagsBuilder<TSource>(
    protected val litRendererBuilder: KLitRendererBuilder<TSource>
) {

    /**
     * @param selfClosing [What are Self Closing Tags in HTML?](https://www.scaler.com/topics/self-closing-tags-in-html/)
     * A traditional HTML tag such as <p>, <div>, <section>, etc., had an opening tag and a closing tag
     * However, due to their fundamental structure, void components in HTML, such as images and links,
     * do not technically require closing tags.
     * Images and links cannot have content - they are pointers to an element installed on the website.
     * See also [img]
     */
    public class Tag<TSource>(
        litRendererBuilder: KLitRendererBuilder<TSource>,
        private val tagName: String,
        private val attributes: String,
        private val selfClosing: Boolean,
    ) : KLitRendererTagsBuilder<TSource>(litRendererBuilder) {
        override fun toString(): String = if (!selfClosing)
                    "<$tagName $attributes>${children.joinToString(separator = "")}</$tagName>"
                else
                    "<$tagName $attributes/>"
    }

    public class Prop<TSource>(
        litRendererBuilder: KLitRendererBuilder<TSource>,
        private val propertyName: String
    ) : KLitRendererTagsBuilder<TSource>(litRendererBuilder) {
        override fun toString(): String = propertyName
    }

    public class Text<TSource>(
        litRendererBuilder: KLitRendererBuilder<TSource>,
        private val literal: String
    ) : KLitRendererTagsBuilder<TSource>(litRendererBuilder) {
        override fun toString(): String = literal
    }

    public class Nodes<TSource>(
        litRendererBuilder: KLitRendererBuilder<TSource>
    ) : KLitRendererTagsBuilder<TSource>(litRendererBuilder) {
        override fun toString(): String = children.joinToString(separator = "")
    }

    public val spacing: KLitRendererTheme get() = KLitRendererTheme.spacing
    public val padding: KLitRendererTheme get() = KLitRendererTheme.padding

    protected val children: MutableList<KLitRendererTagsBuilder<TSource>> = mutableListOf<KLitRendererTagsBuilder<TSource>>()

    @VaadinDsl
    public operator fun Property<TSource>.unaryPlus() {
        children.add(
            Prop(
                litRendererBuilder,
                litRendererBuilder.propertyName(this)
            )
        )
    }

    @VaadinDsl
    public operator fun KLitRendererBuilder.Function<TSource>.unaryPlus() {
        children.add(
            Prop(
                litRendererBuilder,
                litRendererBuilder.functionName(this)
            )
        )
    }

    /**
     * Adds a text to the contents of this element.
     */
    @VaadinDsl
    public operator fun String.unaryPlus() {
        children.add(Text(litRendererBuilder, this))
    }

    /**
     * Adds a HTML element with given [tagName], [attributes]
     * @param selfClosing [What are Self Closing Tags in HTML?](https://www.scaler.com/topics/self-closing-tags-in-html/)
     * A traditional HTML tag such as <p>, <div>, <section>, etc., had an opening tag and a closing tag
     * However, due to their fundamental structure, void components in HTML, such as images and links,
     * do not technically require closing tags.
     * Images and links cannot have content - they are pointers to an element installed on the website.
     * See also [img]
     */
    public fun addTag(
        tagName: String,
        attributes: List<KLitRendererAttribute>,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit,
        selfClosing: Boolean = false,
    ) {
        children.add(
            Tag(
                litRendererBuilder,
                tagName,
                attributes.joinToString(separator = " "),
                selfClosing = selfClosing
            ).apply(block)
        )
    }

    @VaadinDsl
    public fun horizontalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-horizontal-layout", attributes.toList(), block)
    }

    @VaadinDsl
    public fun verticalLayout(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-vertical-layout", attributes.toList(), block)
    }

    @VaadinDsl
    public fun span(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("span", attributes.toList(), block)
    }

    @VaadinDsl
    public fun div(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("div", attributes.toList(), block)
    }

    public fun anchor(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("a", attributes.toList(), block)
    }

    @VaadinDsl
    public fun icon(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-icon", attributes.toList(), block)
    }

    /**
     * A `<vaadin-button>` element.
     */
    @VaadinDsl
    public fun button(
        vararg attributes: KLitRendererAttribute,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-button", attributes.toList(), block)
    }

    /**
     * [Tag.selfClosing] = true
     *
     * [The img tag](https://www.understandingcode.com/image-tag) : `<img />`
     *
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
    public fun img(
        vararg attributes: KLitRendererAttribute
    ) {
        addTag("img", attributes.toList(), { }, selfClosing = true)
    }

    public fun avatar(
        vararg attributes: KLitRendererAttribute
    ) {
        addTag("vaadin-avatar", attributes.toList(), { })
    }
}
