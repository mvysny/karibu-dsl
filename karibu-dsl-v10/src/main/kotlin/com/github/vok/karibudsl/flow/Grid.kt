package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.shared.util.SharedUtil
import kotlin.reflect.KProperty1

fun <T : Any?> (@VaadinDsl HasComponents).grid(block: (@VaadinDsl Grid<T>).() -> Unit = {}) = init(Grid(), block)

/**
 * Adds a column for given [property]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to the property name, converted from camelCase to Human Friendly.
 * @param converter optionally converts the property value [V] to something else, typically to a String. Use this for formatting of the value.
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
fun <T, V : Comparable<V>> Grid<T>.addColumnFor(property: KProperty1<T, V?>,
                                                sortable: Boolean = true,
                                                converter: (V?)->Any? = { it },
                                                block: Grid.Column<T>.() -> Unit = {}): Grid.Column<T> =
    addColumn({ it: T -> converter(property.get(it)) }).apply {
        key = property.name
        if (sortable) sortProperty = property
        setHeader(SharedUtil.camelCaseToHumanFriendly(property.name))
        block()
    }

/**
 * Adds a column for given [property], using given [renderer]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to the property name, converted from camelCase to Human Friendly.
 * @param renderer
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
fun <T, V : Comparable<V>> Grid<T>.addColumnFor(property: KProperty1<T, V?>,
                                                renderer: Renderer<T>,
                                                sortable: Boolean = true,
                                                block: Grid.Column<T>.() -> Unit = {}): Grid.Column<T> =
    addColumn(renderer).apply {
        key = property.name
        if (sortable) sortProperty = property
        setHeader(SharedUtil.camelCaseToHumanFriendly(property.name))
        block()
    }

/**
 * Sets the property by which this column will sort. Setting this property will automatically make the column sortable.
 * You can use the [addColumnFor] which also sets the column by default to sortable.
 *
 * Example of usage:
 * ```
 * grid<Person> {
 *     addColumn({ it.name }).apply {
 *         setHeader("Name")
 *         sortProperty = Person::name
 *     }
 * }
 * ```
 */
var <T> Grid.Column<T>.sortProperty: KProperty1<T, *>
    @Deprecated("Cannot read this property", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException("Unsupported")
    set(value) {
        setSortProperty(value.name)
    }

/**
 * Retrieves the column for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @throws IllegalArgumentException if no such column exists.
 */
fun <T> Grid<T>.getColumnBy(property: KProperty1<T, *>): Grid.Column<T> =
    getColumnByKey(property.name) ?: throw IllegalArgumentException("No column with key $property; available column keys: ${columns.map { it.key } .filterNotNull()}")
