package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.data.renderer.LitRenderer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * [KLitRendererBuilder] is a DSL builder implementation to create [LitRenderer] from [templateExpression], [properties] and [functions]
 * See [litRenderer]
 *
 * Sources of information with examples of LitRenderer usage:
 * * [Renderers](https://vaadin.com/docs/latest/components/grid/renderers)
 * * [Display LitRenderer grid cell button as a link in Vaadin 24] (https://stackoverflow.com/questions/76984432/display-litrenderer-grid-cell-button-as-a-link-in-vaadin-24)
 * * [Vaadin LitRenderer bean exposure to the client] (https://stackoverflow.com/questions/73101886/vaadin-litrenderer-bean-exposure-to-the-client)
 * * [How do I implement different row height in a grid - Vaadin Cookbook] (https://cookbook.vaadin.com/grid-row-height)
 * * [Dynamically render an image using LitRenderer] (https://cookbook.vaadin.com/dynamically-render-an-image-using-litrenderer)
 * @param TSource the type of the item in the grid
 */
public class KLitRendererBuilder<TSource>() {

    public data class Property<TSource>(
        val name: String,
        val provider: (TSource) -> String,
    ) {
        public val litItem: String get() = "\${item.$name}"
    }

    /**
     * A function callable from JavaScript. Will be passed to  [LitRenderer.withFunction].
     * @param name This name must be a valid JavaScript function name. It must be alphanumeric and not null, must not be one of the JavaScript reserved words (https://www.w3schools.com/js/js_reserved.asp)
     * @param TSource the type of the item in the grid
     */
    public data class Function<TSource>(
        val name: String,
        val handler: (TSource) -> Unit,
    ) {

        /**
         * NOTE: Rules to validate name of function are created by ChatGPT :)
         */
        private companion object {
            // JavaScript identifier rules
            val identifierRegex = Regex("^[a-zA-Z_$][a-zA-Z0-9_$]*$")

            // Reserved keywords in JavaScript
            val reservedKeywords = setOf(
                "break", "case", "catch", "class", "const", "continue", "debugger", "default",
                "delete", "do", "else", "enum", "export", "extends", "false", "finally",
                "for", "function", "if", "import", "in", "instanceof", "new", "null", "return",
                "super", "switch", "this", "throw", "true", "try", "typeof", "var", "void",
                "while", "with", "yield", "let", "await", "static", "implements", "package",
                "protected", "interface", "private", "public"
            )
        }

        init {
            require(identifierRegex.matches(name)) {
                "'$name' is not valid JavaScript identifier"
            }

            require(name !in reservedKeywords) {
                "'$name' is reserved keyword in JavaScript"
            }
        }

        public val litItem: String get() = "\${$name}"
    }

    private var templateExpression = ""

    private val properties = mutableMapOf<String, (TSource) -> String>()
    private val functions = mutableMapOf<String, (TSource) -> Unit>()

    @VaadinDsl
    public fun templateExpression(templateExpression: String) {
        this.templateExpression = templateExpression.trimIndent()
    }

    /**
     * Builds the HTML template, for example:
     * ```
     * templateExpression {
     *   verticalLayout(style { lineHeight = +KLumoLineHeight.XS })
     * }
     * ```
     */
    @VaadinDsl
    public fun templateExpression(initBlock: KLitRendererTagsBuilder<TSource>.() -> Unit) {
        templateExpression(KLitRendererTagsBuilder.Nodes(this).apply(initBlock).toString())
    }

    public val litRenderer: LitRenderer<TSource>
        get() =
            LitRenderer.of<TSource>(templateExpression).apply {
                properties.forEach { (name, provider) ->
                    withProperty(name, provider)
                }
                functions.forEach { (name, handler) ->
                    withFunction(name, handler)
                }
            }


    @VaadinDsl
    public operator fun String.invoke(provider: (TSource) -> String) =
        Property(
            name = this,
            provider = provider,
        ).also {
            properties[it.name] = provider
        }

    @VaadinDsl
    public fun property(provider: (TSource) -> String): ReadOnlyProperty<Any?, Property<TSource>> =
        object : ReadOnlyProperty<Any?, Property<TSource>> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): Property<TSource> =
                property.name(provider)
        }

    @VaadinDsl
    public fun function(name: String, handler: (TSource) -> Unit): Function<TSource> =
        Function(
            name = name,
            handler = handler,
        ).also {
            functions[it.name] = handler
        }

    @VaadinDsl
    public fun function(handler: (TSource) -> Unit): ReadOnlyProperty<Any?, Function<TSource>> =
        object : ReadOnlyProperty<Any?, Function<TSource>> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): Function<TSource> =
                function(property.name, handler)
        }

    @VaadinDsl
    public fun propertyName(property: Property<TSource>): String {
        val provider = properties[property.name]
        require(provider != null) { "${property.name} !in ${properties.keys}" }

        return property.litItem
    }

    @VaadinDsl
    public fun functionName(function: Function<TSource>): String {

        val handler = functions[function.name]
        require(handler != null) { "${function.name} !in ${functions.keys}" }

        return function.litItem
    }
}

/**
 * @param TSource the type of the item in the grid
 */
@Suppress("unused")
public fun <TSource> buildLitRenderer(
    initBlock: KLitRendererBuilder<TSource>.() -> Unit
): LitRenderer<TSource> =
    KLitRendererBuilder<TSource>().apply(initBlock).litRenderer
