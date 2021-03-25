package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.FooterRow
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.HeaderRow
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.data.selection.SelectionEvent
import com.vaadin.flow.data.selection.SelectionModel
import com.vaadin.flow.shared.util.SharedUtil
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

@VaadinDsl
public inline fun <reified T : Any?> (@VaadinDsl HasComponents).grid(
        dataProvider: DataProvider<T, *>? = null,
        noinline block: (@VaadinDsl Grid<T>).() -> Unit = {}
): Grid<T> {
    return grid(T::class.java, dataProvider, block)
}

@VaadinDsl
public fun <T : Any> (@VaadinDsl HasComponents).grid(
    klass: KClass<T>?,
    dataProvider: DataProvider<T, *>? = null,
    block: (@VaadinDsl Grid<T>).() -> Unit = {}
): Grid<T> {
    return grid(klass?.java, dataProvider, block)
}

@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).grid(
    clazz: Class<T>?,
    dataProvider: DataProvider<T, *>? = null,
    block: (@VaadinDsl Grid<T>).() -> Unit = {}
): Grid<T> {
    val grid = if (clazz == null) Grid() else Grid(clazz, false)
    if (dataProvider != null) {
        grid.dataProvider = dataProvider
    }
    grid.hotfixMissingHeaderRow()
    return init(grid, block)
}

/**
 * Workaround for https://github.com/vaadin/vaadin-grid-flow/issues/912
 *
 * Internal, do not use. Automatically called from [grid] and [treeGrid].
 */
public fun Grid<*>.hotfixMissingHeaderRow() {
    // don't do this yet: it's not yet possible to remove the header row: https://github.com/vaadin/vaadin-grid-flow/issues/480
    // also https://github.com/vaadin/vaadin-grid-flow/issues/912
/*
    if (headerRows.size == 0) {
        appendHeaderRow()
    }
*/
}

@VaadinDsl
public inline fun <reified T : Any?> (@VaadinDsl HasComponents).treeGrid(
        dataProvider: HierarchicalDataProvider<T, *>? = null,
        noinline block: (@VaadinDsl TreeGrid<T>).() -> Unit = {}
): TreeGrid<T> {
    return treeGrid(T::class.java, dataProvider, block)
}

@VaadinDsl
public fun <T : Any> (@VaadinDsl HasComponents).treeGrid(
    klass: KClass<T>?,
    dataProvider: HierarchicalDataProvider<T, *>? = null,
    block: (@VaadinDsl TreeGrid<T>).() -> Unit = {}
): TreeGrid<T> {
    return treeGrid(klass?.java, dataProvider, block)
}

public fun <T : Any?> @VaadinDsl HasComponents.treeGrid(
    clazz: Class<T>?,
    dataProvider: HierarchicalDataProvider<T, *>? = null,
    block: (@VaadinDsl TreeGrid<T>).() -> Unit = {}
): TreeGrid<T> {
    val grid = if (clazz == null) TreeGrid() else TreeGrid(clazz)
    grid.removeAllColumns() // workaround for https://github.com/vaadin/vaadin-grid-flow/issues/973
    if (dataProvider != null) {
        (grid as Grid<T>).dataProvider = dataProvider
    }
    grid.hotfixMissingHeaderRow()
    return init(grid, block)
}

/**
 * Refreshes the Grid and re-polls for data.
 */
public fun (@VaadinDsl Grid<*>).refresh() {
    dataProvider.refreshAll()
}

public val Grid<*>.isMultiSelect: Boolean get() = selectionModel is SelectionModel.Multi<*, *>
public val Grid<*>.isSingleSelect: Boolean get() = selectionModel is SelectionModel.Single<*, *>
public val SelectionEvent<*, *>.isSelectionEmpty: Boolean get() = !firstSelectedItem.isPresent

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
public fun <T, V> (@VaadinDsl Grid<T>).addColumnFor(
    property: KProperty1<T, V?>,
    sortable: Boolean = true,
    converter: (V?) -> Any? = { it },
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
    addColumn { converter(property.get(it)) }.apply {
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
public fun <T, V> (@VaadinDsl Grid<T>).addColumnFor(
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
public var <T> Grid.Column<T>.sortProperty: KProperty1<T, *>
    @Deprecated("Cannot read this property", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException("Unsupported")
    set(value) {
        setSortProperty(value.name)
    }

/**
 * Retrieves the column for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @throws IllegalArgumentException if no such column exists.
 */
public fun <T> Grid<T>.getColumnBy(property: KProperty1<T, *>): Grid.Column<T> =
    getColumnByKey(property.name)
            ?: throw IllegalArgumentException("No column with key $property; available column keys: ${columns.map { it.key }.filterNotNull()}")

/**
 * Returns a [Comparator] which compares values of given property name.
 */
public fun <T> Class<T>.getPropertyComparator(propertyName: String): Comparator<T> {
    val getter: Method = getGetter(propertyName)
    return compareBy { if (it == null) null else getter.invoke(it) as Comparable<*> }
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
public inline fun <reified T, reified V> Grid<T>.addColumnFor(
    propertyName: String,
    sortable: Boolean = true,
    noinline converter: (V?) -> Any? = { it },
    block: Grid.Column<T>.() -> Unit = {}
): Grid.Column<T> {
    val getter: Method = T::class.java.getGetter(propertyName)
    val column: Grid.Column<T> = addColumn { converter(V::class.java.cast(getter.invoke(it))) }
    return column.apply {
        key = propertyName
        if (sortable) {
            setSortProperty(propertyName)
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
 * @param V the value that the column will display, deduced from the type of the [propertyName].
 * @return the newly created column
 */
public inline fun <reified T, reified V> Grid<T>.addColumnFor(
    propertyName: String,
    renderer: Renderer<T>,
    sortable: Boolean = true,
    block: Grid.Column<T>.() -> Unit = {}
): Grid.Column<T> =
    addColumn(renderer).apply {
        key = propertyName
        if (sortable) {
            setSortProperty(propertyName)
        }
        setHeader(SharedUtil.camelCaseToHumanFriendly(propertyName))
        block()
    }

/**
 * Returns `com.vaadin.flow.component.grid.AbstractColumn`
 */
@Suppress("ConflictingExtensionProperty")  // conflicting property is "protected"
internal val HeaderRow.HeaderCell.column: Any
    get() {
        val getColumn: Method = abstractCellClass.getDeclaredMethod("getColumn")
        getColumn.isAccessible = true
        return getColumn.invoke(this)
    }

private val abstractCellClass: Class<*> = Class.forName("com.vaadin.flow.component.grid.AbstractRow\$AbstractCell")
private val abstractColumnClass: Class<*> = Class.forName("com.vaadin.flow.component.grid.AbstractColumn")

/**
 * Returns `com.vaadin.flow.component.grid.AbstractColumn`
 */
@Suppress("ConflictingExtensionProperty")  // conflicting property is "protected"
private val FooterRow.FooterCell.column: Any
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
public fun HeaderRow.getCell(property: KProperty1<*, *>): HeaderRow.HeaderCell {
    val cell: HeaderRow.HeaderCell? = cells.firstOrNull { it.column.columnKey == property.name }
    require(cell != null) { "This grid has no property named ${property.name}: $cells" }
    return cell
}

private val Any.columnKey: String?
get() {
    abstractColumnClass.cast(this)
    val method: Method = abstractColumnClass.getDeclaredMethod("getBottomLevelColumn")
    method.isAccessible = true
    val gridColumn: Grid.Column<*> = method.invoke(this) as Grid.Column<*>
    return gridColumn.key
}

/**
 * Retrieves the cell for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @return the corresponding cell
 * @throws IllegalArgumentException if no such column exists.
 */
public fun FooterRow.getCell(property: KProperty1<*, *>): FooterRow.FooterCell {
    val cell: FooterRow.FooterCell? = cells.firstOrNull { it.column.columnKey == property.name }
    require(cell != null) { "This grid has no property named ${property.name}: $cells" }
    return cell
}

public val HeaderRow.HeaderCell.renderer: Renderer<*>?
    get() {
        val method: Method = abstractColumnClass.getDeclaredMethod("getHeaderRenderer")
        method.isAccessible = true
        val renderer = method.invoke(column)
        return renderer as Renderer<*>?
    }

public val FooterRow.FooterCell.renderer: Renderer<*>?
    get() {
        val method: Method = abstractColumnClass.getDeclaredMethod("getFooterRenderer")
        method.isAccessible = true
        val renderer = method.invoke(column)
        return renderer as Renderer<*>?
    }

public var FooterRow.FooterCell.component: Component?
    get() {
        val cr: ComponentRenderer<*, *> = (renderer as? ComponentRenderer<*, *>) ?: return null
        return cr.createComponent(null)
    }
    set(value) {
        setComponent(value)
    }

private val gridSorterComponentRendererClass: Class<*>? = try {
    Class.forName("com.vaadin.flow.component.grid.GridSorterComponentRenderer")
} catch (e: ClassNotFoundException) {
    // Vaadin 18.0.3+ and Vaadin 14.5.0+ doesn't contain this class anymore and simply uses ComponentRenderer
    null
}

/**
 * Returns or sets the component in grid's header cell. Returns `null` if the cell contains String, something else than a component or nothing at all.
 */
@Suppress("UNCHECKED_CAST")
public var HeaderRow.HeaderCell.component: Component?
    get() {
        val r: Renderer<*>? = renderer
        if (gridSorterComponentRendererClass != null && gridSorterComponentRendererClass.isInstance(r)) {
            val componentField = gridSorterComponentRendererClass.getDeclaredField("component")
            componentField.isAccessible = true
            return componentField.get(r) as Component?
        }
        if (r is ComponentRenderer<*, *>) {
            return (r as ComponentRenderer<*, Any?>).createComponent(null)
        }
        return null
    }
    set(value) {
        setComponent(value)
    }
