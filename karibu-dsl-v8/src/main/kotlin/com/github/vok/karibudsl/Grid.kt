package com.github.vok.karibudsl

import com.vaadin.data.provider.DataProvider
import com.vaadin.ui.Grid
import com.vaadin.ui.HasComponents
import com.vaadin.ui.renderers.TextRenderer
import elemental.json.Json
import elemental.json.JsonValue
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

fun <T: Any> (@VaadinDsl HasComponents).grid(clazz: KClass<T>, caption: String? = null, dataProvider: DataProvider<T, *>? = null, block: (@VaadinDsl Grid<T>).() -> Unit = {}) =
        init(Grid<T>(clazz.java)) {
            this.caption = caption
            if (dataProvider != null) this.dataProvider = dataProvider
            block()
        }

/**
 * Causes the Grid to only show given set of columns, and in given order.
 * @param ids show only this properties.
 */
fun <T> (@VaadinDsl Grid<T>).showColumns(vararg ids: KProperty1<T, *>) = setColumns(*ids.map { it.name }.toTypedArray())

/**
 * Allows you to configure a particular column in a Grid. E.g.:
 * ```
 * grid(...) {
 *   showColumns(Person::name, Person::age)
 *   column(Person::age) { isSortable = false }
 * }
 * ```
 * @param prop the bean property for which to retrieve the column
 * @param block run this block with the column as a receiver
 */
@Suppress("UNCHECKED_CAST")
fun <T, V> (@VaadinDsl Grid<T>).column(prop: KProperty1<T, V>, block: (@VaadinDsl Grid.Column<T, V>).() -> Unit = {}): Grid.Column<T, V> =
        (getColumn(prop.name) as Grid.Column<T, V>).apply { block() }

@Suppress("UNCHECKED_CAST")
class ConvertingRenderer<V>(private val converter: (V)->String) : TextRenderer() {
    override fun encode(value: Any?): JsonValue {
        return if (value == null) super.encode(value) else Json.create(converter(value as V))
    }
}

/**
 * Removes column showing given [property].
 */
fun <T, V> (@VaadinDsl Grid<T>).removeColumn(property: KProperty1<T, V>) = removeColumn(property.name)

/**
 * Utility method which adds a column showing given [property]; important difference is that
 * instead of a [Renderer] the value is converted to String using [converter]. Example of use:
 *
 * ```
 * grid.addColumn(Person::dateOfBirth, { it -> SimpleDateFormat("yyyy-MM-dd").format(it) }) {
 *     caption = "Date of Birth"
 * }
 * ```
 */
@Suppress("UNCHECKED_CAST")
fun <T, V> (@VaadinDsl Grid<T>).addColumn(property: KProperty1<T, V>, converter: (V)->String, block: Grid.Column<T, V>.() -> Unit = {}): Grid.Column<T, V> =
        (addColumn(property.name, ConvertingRenderer(converter)) as Grid.Column<T, V>).apply { block() }
