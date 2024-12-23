package com.github.mvysny.karibudsl.v10

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.vaadin.flow.data.renderer.LitRenderer
import com.github.mvysny.karibudsl.v10.KLitRendererBuilderA.Property
import kotlin.also
import kotlin.apply
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.text.trimIndent


/**
 * PRB: \${item.itemName} -> isn't interpreted by [LitRenderer]
 * FAIL: $\{item.itemName}
 * --- Templates escaping in Kotlin multiline strings https://stackoverflow.com/questions/32993586/templates-escaping-in-kotlin-multiline-strings
 * Solution: ${ "\${item.itemName}" }
 */
public val String.litItem get() = "\${item.$this}"

@Suppress("unused")
@VaadinDsl
public interface KLitRendererBuilderA<TSource> {

    public data class Property<TSource>(
        val name: String,
        val provider: (TSource) -> String,
    ) {
        public val litItem get() = name.litItem
    }

    public operator fun String.invoke(provider: (TSource) -> String): Property<TSource>

    public fun templateExpression(templateExpression: String)

    public fun templateExpression(initBlock: KLitRendererTagsBuilderA<TSource>.() -> Unit)

}

/**
 * --- Renderers https://vaadin.com/docs/latest/components/grid/renderers
 * --- Display LitRenderer grid cell button as a link in Vaadin 24 https://stackoverflow.com/questions/76984432/display-litrenderer-grid-cell-button-as-a-link-in-vaadin-24
 * --- Vaadin LitRenderer bean exposure to the client https://stackoverflow.com/questions/73101886/vaadin-litrenderer-bean-exposure-to-the-client
 * --- How do I implement different row height in a grid - Vaadin Cookbook https://cookbook.vaadin.com/grid-row-height
 * --- Dynamically render an image using LitRenderer https://cookbook.vaadin.com/dynamically-render-an-image-using-litrenderer
 */
public class KLitRendererBuilder<TSource>() : KLitRendererBuilderA<TSource> {

    private var templateExpression = ""

    private val properties = mutableMapOf<String, (TSource) -> String>()

    override fun templateExpression(templateExpression: String) {
        this.templateExpression = templateExpression.trimIndent()
    }

    override fun templateExpression(initBlock: KLitRendererTagsBuilderA<TSource>.() -> Unit) {
        templateExpression(KLitRendererTagsBuilder(this).apply(initBlock).toString())
    }

    public val litRenderer: LitRenderer<TSource>
        get() =
            LitRenderer.of<TSource>(templateExpression).apply {
                properties.forEach { (name, provider) ->
                    withProperty(name, provider)
                }
            }


    override fun String.invoke(provider: (TSource) -> String) =
        Property(
            name = this,
            provider = provider,
        ).also {
            properties[it.name] = provider
        }

    public fun propertyName(property : Property<TSource>) : String {

        val provider = properties[property.name]
        require(provider != null) { "${property.name} !in ${properties.keys}" }

        return property.litItem
    }

}

@Suppress("unused")
public fun <TSource> buildLitRenderer(
    initBlock: KLitRendererBuilderA<TSource>.() -> Unit
): LitRenderer<TSource> =
    KLitRendererBuilder<TSource>().apply(initBlock).litRenderer
