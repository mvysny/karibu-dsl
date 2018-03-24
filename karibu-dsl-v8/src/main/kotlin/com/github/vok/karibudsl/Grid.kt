package com.github.vok.karibudsl

import com.vaadin.data.SelectionModel
import com.vaadin.data.provider.DataProvider
import com.vaadin.event.selection.SelectionEvent
import com.vaadin.shared.util.SharedUtil
import com.vaadin.ui.Grid
import com.vaadin.ui.HasComponents
import com.vaadin.ui.renderers.TextRenderer
import elemental.json.Json
import elemental.json.JsonValue
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Creates a grid.
 * @param itemClass host items of this class. If not null, columns are created automatically for this class. This is not recommended:
 * https://github.com/mvysny/karibu-dsl/issues/4
 */
fun <T: Any> (@VaadinDsl HasComponents).grid(itemClass: KClass<T>? = null, caption: String? = null, dataProvider: DataProvider<T, *>? = null, block: (@VaadinDsl Grid<T>).() -> Unit = {}) =
        init(if (itemClass == null) Grid() else Grid<T>(itemClass.java)) {
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
 * Allows you to re-configure a particular column in a Grid. E.g.:
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

/**
 * Removes column showing given [property].
 */
fun <T, V> (@VaadinDsl Grid<T>).removeColumn(property: KProperty1<T, V>) = removeColumn(property.name)

/**
 * Refreshes the Grid and re-polls for data.
 */
fun (@VaadinDsl Grid<*>).refresh() = dataProvider.refreshAll()

val Grid<*>.isMultiSelect: Boolean get() = selectionModel is SelectionModel.Multi<*>
val Grid<*>.isSingleSelect: Boolean get() = selectionModel is SelectionModel.Single<*>
val SelectionEvent<*>.isSelectionEmpty: Boolean get() = !firstSelectedItem.isPresent

/**
 * Adds a new column for given [property] which is by default sortable. The [Grid.Column.setId]
 * is set to property name, the column caption is set by converting camelCase to Human Friendly.
 */
fun <T, V> (@VaadinDsl Grid<T>).addColumnFor(property: KProperty1<T, V>, block: Grid.Column<T, V>.() -> Unit = {}): Grid.Column<T, V> =
        addColumn(property).apply {
            id = property.name
            caption = SharedUtil.propertyIdToHumanFriendly(property.name)
            block()
        }

/**
 * Retrieves the column for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @throws IllegalArgumentException if no such column exists.
 */
@Suppress("UNCHECKED_CAST")
fun <T, V> Grid<T>.getColumnBy(property: KProperty1<T, V>): Grid.Column<T, V> =
        getColumn(property.name) as Grid.Column<T, V>?
                ?: throw IllegalArgumentException("No column with key $property; available column keys: ${columns.map { it.id }.filterNotNull()}")
