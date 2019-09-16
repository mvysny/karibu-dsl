package com.github.mvysny.karibudsl.v8

import com.vaadin.data.SelectionModel
import com.vaadin.data.provider.DataProvider
import com.vaadin.data.provider.HierarchicalDataProvider
import com.vaadin.event.selection.SelectionEvent
import com.vaadin.shared.util.SharedUtil
import com.vaadin.ui.Grid
import com.vaadin.ui.Grid.Column
import com.vaadin.ui.HasComponents
import com.vaadin.ui.TreeGrid
import com.vaadin.ui.components.grid.FooterCell
import com.vaadin.ui.components.grid.FooterRow
import com.vaadin.ui.components.grid.HeaderCell
import com.vaadin.ui.components.grid.HeaderRow
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
@VaadinDsl
fun <T: Any> (@VaadinDsl HasComponents).grid(itemClass: KClass<T>? = null, caption: String? = null, dataProvider: DataProvider<T, *>? = null, block: (@VaadinDsl Grid<T>).() -> Unit = {}) =
        init(if (itemClass == null) Grid() else Grid<T>(itemClass.java)) {
            this.caption = caption
            if (dataProvider != null) this.dataProvider = dataProvider
            block()
        }

/**
 * Creates a tree grid.
 */
@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).treeGrid(dataProvider: HierarchicalDataProvider<T, *>? = null, block: (@VaadinDsl TreeGrid<T>).() -> Unit = {}) = init(
    TreeGrid<T>()
) {
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
@VaadinDsl
fun <T, V> (@VaadinDsl Grid<T>).column(prop: KProperty1<T, V>, block: (@VaadinDsl Grid.Column<T, V>).() -> Unit = {}): Grid.Column<T, V> =
        getColumnBy(prop).apply { block() }

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
fun <T, V> (@VaadinDsl Grid<T>).addColumnFor(property: KProperty1<T, V>, block: (@VaadinDsl Grid.Column<T, V>).() -> Unit = {}): Grid.Column<T, V> =
        addColumn(property).apply {
            id = property.name
            caption = SharedUtil.propertyIdToHumanFriendly(property.name)
            block()
        }

/**
 * Retrieves the column for given [property]; it matches the column ID to [KProperty1.name].
 * @throws IllegalArgumentException if no such column exists.
 */
@Suppress("UNCHECKED_CAST")
fun <T, V> Grid<T>.getColumnBy(property: KProperty1<T, V>): Grid.Column<T, V> = getColumnBy(property.name) as Grid.Column<T, V>

/**
 * Retrieves the column for given [columnId].
 * @throws IllegalArgumentException if no such column exists.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Grid<T>.getColumnBy(columnId: String): Grid.Column<T, *> =
        getColumn(columnId) as Grid.Column<T, *>?
                ?: throw IllegalArgumentException("No column with ID $columnId; available column IDs: ${columns.map { it.id }.filterNotNull()}")

enum class VAlign { Left, Middle, Right }

/**
 * Aligns the text in the columns, by using [Column.setStyleGenerator] to set either `v-align-center` or `v-align-right` style.
 */
var Grid.Column<*, *>.align: VAlign
    @Deprecated("Cannot read this property", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException("Cannot read this property")
    set(value) {
        when (value) {
            VAlign.Left -> setStyleGenerator { null }
            // these styles are provided by the Vaadin Valo theme out-of-the-box, no need to add then to the app's stylesheet
            VAlign.Middle -> setStyleGenerator { "v-align-center" }
            VAlign.Right -> setStyleGenerator { "v-align-right" }
        }
    }

/**
 * Returns the cell on this row corresponding to the given property.
 * @param property the id of the column whose header cell to get, not null
 * @return the header cell
 * @throws IllegalArgumentException if there is no such column in the grid
 */
fun HeaderRow.getCell(property: KProperty1<*, *>): HeaderCell = getCell(property.name)

/**
 * Returns the cell on this row corresponding to the given property.
 * @param property the id of the column whose footer cell to get, not null
 * @return the footer cell
 * @throws IllegalArgumentException if there is no such column in the grid
 */
fun FooterRow.getCell(property: KProperty1<*, *>): FooterCell = getCell(property.name)
