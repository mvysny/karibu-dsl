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
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit,
        block: KLitRendererTagsBuilder<TSource>.() -> Unit,
        selfClosing: Boolean = false,
    ) {
        children.add(
            Tag(
                litRendererBuilder,
                tagName,
                KLitRendererAttributeBuilder<TSource>().apply(attributes).toString(),
                selfClosing = selfClosing
            ).apply(block)
        )
    }

    @VaadinDsl
    public fun horizontalLayout(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-horizontal-layout", attributes, block)
    }

    /**
     * Adds `<vaadin-vertical-layout>`, for example:
     * ```
     * verticalLayout({ style { lineHeight = +KLumoLineHeight.XS } })
     * ```
     */
    @VaadinDsl
    public fun verticalLayout(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-vertical-layout", attributes, block)
    }

    @VaadinDsl
    public fun span(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("span", attributes, block)
    }

    @VaadinDsl
    public fun div(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("div", attributes, block)
    }

    public fun anchor(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("a", attributes, block)
    }

    @VaadinDsl
    public fun icon(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-icon", attributes, block)
    }

    /**
     * A `<vaadin-button>` element:
     * ```
     * button({
     *   cssClass("category__edit")
     *   themeVariant(ButtonVariant.LUMO_TERTIARY)
     *   click(onButtonClick)
     * }) {
     *   icon(icon(VaadinIcon.TRASH.create()))
     *   +"Delete"
     * }
     * ```
     */
    @VaadinDsl
    public fun button(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
        block: KLitRendererTagsBuilder<TSource>.() -> Unit = {}
    ) {
        addTag("vaadin-button", attributes, block)
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
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
    ) {
        addTag("img", attributes, {}, selfClosing = true)
    }

    public fun avatar(
        attributes: KLitRendererAttributeBuilder<TSource>.() -> Unit = {},
    ) {
        addTag("vaadin-avatar", attributes, {})
    }
}
