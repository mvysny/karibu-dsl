package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.FooterRow
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.HeaderRow
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.data.selection.SelectionEvent
import com.vaadin.flow.data.selection.SelectionModel
import com.vaadin.flow.shared.util.SharedUtil
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KProperty1

fun <T : Any?> (@VaadinDsl HasComponents).grid(dataProvider: DataProvider<T, *>? = null, block: (@VaadinDsl Grid<T>).() -> Unit = {}) =
    init(Grid<T>()) {
        if (dataProvider != null) this.dataProvider = dataProvider
        block()
    }

/**
 * Refreshes the Grid and re-polls for data.
 */
fun (@VaadinDsl Grid<*>).refresh() = dataProvider.refreshAll()

val Grid<*>.isMultiSelect: Boolean get() = selectionModel is SelectionModel.Multi<*, *>
val Grid<*>.isSingleSelect: Boolean get() = selectionModel is SelectionModel.Single<*, *>
val SelectionEvent<*, *>.isSelectionEmpty: Boolean get() = !firstSelectedItem.isPresent

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
fun <T, V : Comparable<V>> (@VaadinDsl Grid<T>).addColumnFor(
    property: KProperty1<T, V?>,
    sortable: Boolean = true,
    converter: (V?) -> Any? = { it },
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
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
fun <T, V : Comparable<V>> (@VaadinDsl Grid<T>).addColumnFor(
    property: KProperty1<T, V?>,
    renderer: Renderer<T>,
    sortable: Boolean = true,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
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
        // need to set the comparator as well: https://github.com/vaadin/flow/issues/3759
        setComparator(compareBy { value.get(it) as Comparable<*> })
    }

/**
 * Retrieves the column for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @throws IllegalArgumentException if no such column exists.
 */
fun <T> Grid<T>.getColumnBy(property: KProperty1<T, *>): Grid.Column<T> =
    getColumnByKey(property.name)
            ?: throw IllegalArgumentException("No column with key $property; available column keys: ${columns.map { it.key }.filterNotNull()}")

/**
 * Returns a [Comparator] which compares values of given property name.
 */
fun <T> Class<T>.getPropertyComparator(propertyName: String): Comparator<T> {
    val getter = getGetter(propertyName)
    return compareBy { it: T? -> if (it == null) null else getter.invoke(it) as Comparable<*> }
}

/**
 * Adds a column for given [propertyName]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to the property name, converted from camelCase to Human Friendly.
 *
 * This method should only be used when you have a Grid backed by a Java class which does not have properties exposed as [KProperty1]; for Kotlin
 * class-backed Grids you should use `addColumnFor(KProperty1)`
 * @param converter optionally converts the property value [V] to something else, typically to a String. Use this for formatting of the value.
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display.
 * @return the newly created column
 */
inline fun <reified T, reified V> Grid<T>.addColumnFor(
    propertyName: String,
    sortable: Boolean = true,
    noinline converter: (V?) -> Any? = { it },
    block: Grid.Column<T>.() -> Unit = {}
): Grid.Column<T> {
    val getter: Method = T::class.java.getGetter(propertyName)
    return addColumn({ it: T -> converter(V::class.java.cast(getter.invoke(it))) }).apply {
        key = propertyName
        if (sortable) {
            setSortProperty(propertyName)
            // need to set the comparator as well: https://github.com/vaadin/flow/issues/3759
            setComparator(T::class.java.getPropertyComparator(propertyName))
        }
        setHeader(SharedUtil.camelCaseToHumanFriendly(propertyName))
        block()
    }
}

/**
 * Adds a column for given [propertyName], using given [renderer]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to the property name, converted from camelCase to Human Friendly.
 *
 * This method should only be used when you have a Grid backed by a Java class which does not have properties exposed as [KProperty1]; for Kotlin
 * class-backed Grids you should use `addColumnFor(KProperty1)`
 * @param renderer
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
inline fun <reified T, reified V> Grid<T>.addColumnFor(
    propertyName: String,
    renderer: Renderer<T>,
    sortable: Boolean = true,
    block: Grid.Column<T>.() -> Unit = {}
): Grid.Column<T> =
    addColumn(renderer).apply {
        key = propertyName
        if (sortable) {
            setSortProperty(propertyName)
            // need to set the comparator as well: https://github.com/vaadin/flow/issues/3759
            setComparator(T::class.java.getPropertyComparator(propertyName))
        }
        setHeader(SharedUtil.camelCaseToHumanFriendly(propertyName))
        block()
    }

/**
 * Returns `com.vaadin.flow.component.grid.AbstractColumn`
 */
internal val HeaderRow.HeaderCell.column: Any
    get() {
        val getColumn = abstractCellClass.getDeclaredMethod("getColumn")
        getColumn.isAccessible = true
        return getColumn.invoke(this)
    }

private val abstractCellClass = Class.forName("com.vaadin.flow.component.grid.AbstractRow\$AbstractCell")
private val abstractColumnClass = Class.forName("com.vaadin.flow.component.grid.AbstractColumn")

/**
 * Returns `com.vaadin.flow.component.grid.AbstractColumn`
 */
internal val FooterRow.FooterCell.column: Any
    get() {
        val getColumn = abstractCellClass.getDeclaredMethod("getColumn")
        getColumn.isAccessible = true
        return getColumn.invoke(this)
    }

/**
 * Retrieves the cell for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @return the corresponding cell
 * @throws IllegalArgumentException if no such column exists.
 */
fun HeaderRow.getCell(property: KProperty1<*, *>): HeaderRow.HeaderCell {
    val cell = cells.firstOrNull { (it.column as Grid.Column<*>).key == property.name }
    require(cell != null) { "This grid has no property named ${property.name}: $cells" }
    return cell!!
}

/**
 * Retrieves the cell for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @return the corresponding cell
 * @throws IllegalArgumentException if no such column exists.
 */
fun FooterRow.getCell(property: KProperty1<*, *>): FooterRow.FooterCell {
    val cell = cells.firstOrNull { (it.column as Grid.Column<*>).key == property.name }
    require(cell != null) { "This grid has no property named ${property.name}: $cells" }
    return cell!!
}

val HeaderRow.HeaderCell.renderer: Renderer<*>?
    get() {
        val method = abstractColumnClass.getDeclaredMethod("getHeaderRenderer")
        method.isAccessible = true
        val renderer = method.invoke(column)
        return renderer as Renderer<*>?
    }

val FooterRow.FooterCell.renderer: Renderer<*>?
    get() {
        val method = abstractColumnClass.getDeclaredMethod("getFooterRenderer")
        method.isAccessible = true
        val renderer = method.invoke(column)
        return renderer as Renderer<*>?
    }

var FooterRow.FooterCell.component: Component?
    get() {
        val cr = (renderer as? ComponentRenderer<*, *>) ?: return null
        return cr.createComponent(null)
    }
    set(value) {
        setComponent(value)
    }

private val gridSorterComponentRendererClass = Class.forName("com.vaadin.flow.component.grid.GridSorterComponentRenderer")

var HeaderRow.HeaderCell.component: Component?
    get() {
        val r = renderer
        if (!gridSorterComponentRendererClass.isInstance(r)) return null
        val componentField = gridSorterComponentRendererClass.getDeclaredField("component")
        componentField.isAccessible = true
        return componentField.get(r) as Component
    }
    set(value) {
        setComponent(value)
    }

/**
 * Get rid of retarded Optional
 */
val Grid.Column<*>._id: String? get() = id.orElse(null)
