package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import kotlin.reflect.KProperty1

fun <T: Any?> (@VaadinDsl HasComponents).grid(block: (@VaadinDsl Grid<T>).() -> Unit = {})
        = init(Grid(), block)

/**
 * Sets the property by which this column will sort. Setting this property will automatically make the column sortable. Also
 * sets the column key.
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
        key = value.name
    }
