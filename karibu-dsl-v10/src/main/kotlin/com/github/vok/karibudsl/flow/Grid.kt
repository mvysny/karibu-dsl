package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import kotlin.reflect.KProperty1

fun <T : Any?> (@VaadinDsl HasComponents).grid(block: (@VaadinDsl Grid<T>).() -> Unit = {}) = init(Grid(), block)

/**
 * Adds a column for given [property]. The column key is set to the property name; the column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise.
 * @return the newly created column
 */
fun <T, V : Comparable<V>> Grid<T>.addColumnFor(property: KProperty1<T, V?>, sortable: Boolean = true, block: Grid.Column<T>.() -> Unit = {}): Grid.Column<T> =
    addColumn(property).apply {
        key = property.name
        if (sortable) {
            sortProperty = property
        }
        block()
    }

/**
 * Adds a column for given [property]. The column key is set to the property name; the column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise.
 * @return the newly created column
 */
@JvmName("addColumnFor2")
fun <T, V : Comparable<V>> Grid<T>.addColumnFor(property: KProperty1<T, V>, sortable: Boolean = true, block: Grid.Column<T>.() -> Unit = {}): Grid.Column<T> =
    addColumn(property).apply {
        key = property.name
        if (sortable) {
            sortProperty = property
        }
        block()
    }

/**
 * Sets the property by which this column will sort. Setting this property will automatically make the column sortable.
 * You can use the [addColumnFor] which also sets the column by default to sortable.
 *
 * Example of usage:
 * ```
 * grid<Person> {
 *     addColumn(Person::name).apply {
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
