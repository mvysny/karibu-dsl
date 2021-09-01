package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributools.addColumnFor
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider
import com.vaadin.flow.data.renderer.Renderer
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

@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).treeGrid(
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
    block: (@VaadinDsl Grid.Column<T>).() -> Unit
): Grid.Column<T> =
    addColumnFor(property, sortable, converter).apply {
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
    block: (@VaadinDsl Grid.Column<T>).() -> Unit
): Grid.Column<T> =
    addColumnFor(property, renderer, sortable).apply {
        block()
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
    block: Grid.Column<T>.() -> Unit
): Grid.Column<T> =
    addColumnFor(propertyName, sortable, converter).apply { block() }

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
    block: Grid.Column<T>.() -> Unit
): Grid.Column<T> =
    addColumnFor<T, V>(propertyName, renderer, sortable).apply { block() }
