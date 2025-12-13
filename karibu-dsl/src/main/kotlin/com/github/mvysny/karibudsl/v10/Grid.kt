package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributools.addColumnFor
import com.github.mvysny.kaributools.addHierarchyColumnFor
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.function.ValueProvider
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * [Vaadin Grid](https://vaadin.com/docs/latest/components/grid)
 * is a component for displaying tabular data, including various enhancements to grid renderings.
 */
@VaadinDsl
public inline fun <reified T> (@VaadinDsl HasComponents).grid(
        dataProvider: DataProvider<T, *>? = null,
        noinline block: (@VaadinDsl Grid<T>).() -> Unit = {}
): Grid<T> {
    return grid(T::class.java, dataProvider, block)
}

/**
 * [Vaadin Grid](https://vaadin.com/docs/latest/components/grid)
 * is a component for displaying tabular data, including various enhancements to grid renderings.
 */
@VaadinDsl
public fun <T : Any> (@VaadinDsl HasComponents).grid(
    klass: KClass<T>?,
    dataProvider: DataProvider<T, *>? = null,
    block: (@VaadinDsl Grid<T>).() -> Unit = {}
): Grid<T> {
    return grid(klass?.java, dataProvider, block)
}

/**
 * [Vaadin Grid](https://vaadin.com/docs/latest/components/grid)
 * is a component for displaying tabular data, including various enhancements to grid renderings.
 */
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
    return init(grid, block)
}

/**
 * [Tree Grid](https://vaadin.com/docs/latest/components/tree-grid) is a component for displaying hierarchical tabular data grouped into expandable nodes.
 */
@VaadinDsl
public inline fun <reified T : Any?> (@VaadinDsl HasComponents).treeGrid(
        dataProvider: HierarchicalDataProvider<T, *>? = null,
        noinline block: (@VaadinDsl TreeGrid<T>).() -> Unit = {}
): TreeGrid<T> {
    return treeGrid(T::class.java, dataProvider, block)
}

/**
 * [Tree Grid](https://vaadin.com/docs/latest/components/tree-grid) is a component for displaying hierarchical tabular data grouped into expandable nodes.
 */
@VaadinDsl
public fun <T : Any> (@VaadinDsl HasComponents).treeGrid(
    klass: KClass<T>?,
    dataProvider: HierarchicalDataProvider<T, *>? = null,
    block: (@VaadinDsl TreeGrid<T>).() -> Unit = {}
): TreeGrid<T> {
    return treeGrid(klass?.java, dataProvider, block)
}

/**
 * [Tree Grid](https://vaadin.com/docs/latest/components/tree-grid) is a component for displaying hierarchical tabular data grouped into expandable nodes.
 */
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
    return init(grid, block)
}

/**
 * Adds a column for given [property]. The column [key] is set to the property name, so that you can look up the column
 * using `Grid.getColumnBy()`. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise.
 *
 * The header title is set to the property name, converted from camelCase to Human Friendly.
 *
 * WARNING: if an in-memory data provider is used, the sorting will be performed according to the
 * outcome of the [converter]. This may not be wanted, e.g. when the converter converts
 * date to a string. In this case, it's better to use the `columnFor(Renderer)`.
 * @param converter optionally converts the property value [V] to something else, typically to a String. Use this for formatting of the value.
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
@VaadinDsl
public fun <T, V> (@VaadinDsl Grid<T>).columnFor(
    property: KProperty1<T, V?>,
    sortable: Boolean = true,
    key: String = property.name,
    converter: (V?) -> Any? = { it },
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
    addColumnFor(property, sortable, key, converter).apply {
        block()
    }

/**
 * Adds a column for given [property], using given [renderer]. The column [key] is set to the property name, so that you can look up the column
 * using `Grid.getColumnBy`. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to the property name, converted from camelCase to Human Friendly.
 * @param renderer
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
@VaadinDsl
public fun <T, V> (@VaadinDsl Grid<T>).columnFor(
    property: KProperty1<T, V?>,
    renderer: Renderer<T>,
    sortable: Boolean = true,
    key: String = property.name,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
    addColumnFor(property, renderer, sortable, key).apply {
        block()
    }

/**
 * Adds a column for given [propertyName]. The column key is set to the property name, so that you can look up the column
 * using `Grid.getColumnBy`. The column is also by default set to sortable
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
@VaadinDsl
public inline fun <reified T, reified V> (@VaadinDsl Grid<T>).columnFor(
    propertyName: String,
    sortable: Boolean = true,
    key: String = propertyName,
    noinline converter: (V?) -> Any? = { it },
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
    addColumnFor(propertyName, sortable, key, converter).apply { block() }

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
@VaadinDsl
public inline fun <reified T, reified V> (@VaadinDsl Grid<T>).columnFor(
    propertyName: String,
    renderer: Renderer<T>,
    sortable: Boolean = true,
    key: String = propertyName,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
    addColumnFor<T, V>(propertyName, renderer, sortable, key).apply { block() }

/**
 * Adds a new text column to this [Grid] with a value provider and
 * default column factory. The value is converted to String when sent to the
 * client by using [java.lang.String.valueOf].
 *
 * *NOTE:* For displaying components, see
 * {@link #addComponentColumn(ValueProvider)}. For using build-in renderers,
 * see {@link #addColumn(Renderer)}.
 *
 * Every added column sends data to the client side regardless of its
 * visibility state. Don't add a new column at all or use
 * [Grid.removeColumn] to avoid sending extra data.
 *
 * Example of use:
 * ```kotlin
 * column({ it.getReviewCount() }) {
 *   setHeader("Beverages")
 * }
 * ```
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @return the newly created column
 */
@VaadinDsl
public fun <T> (@VaadinDsl Grid<T>).column(
    valueProvider: ValueProvider<T, *>,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> {
    val column = addColumn(valueProvider)
    column.block()
    return column
}

/**
 * Adds a new column that shows components.
 *
 * This is a shorthand for [Grid.addColumn] with a [ComponentRenderer].
 *
 * *NOTE:* Using [ComponentRenderer] is not as efficient as the
 * built-in renderers or using `LitRenderer`.
 *
 * Every added column sends data to the client side regardless of its
 * visibility state. Don't add a new column at all or use
 * [Grid.removeColumn] to avoid sending extra data.
 *
 * Example of use:
 * ```kotlin
 * componentColumn({ category -> button(category.name) {...} }) {
 *   flexGrow = 0; key = "edit"
 * }
 * ```
 * Note that you use the DSL functions inside the [componentProvider] block, to create
 * the component rendered in this column. The DSL function is run once for every cell.
 * @param componentProvider a value provider that builds the column contents in DSL fashion.
 * @param <V> the component type
 * @return the new column
 */
@VaadinDsl
public fun <T> (@VaadinDsl Grid<T>).componentColumn(
    componentProvider: HasComponents.(T) -> Unit,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> {
    val column = addComponentColumn {
        buildSingleComponentOrNull("Grid column") {
            componentProvider(it)
        }
    }
    column.block()
    return column
}

/**
 * Adds a new column that shows components using [Renderer].
 *
 * This is a shorthand for [Grid.addColumn] with a [Renderer].
 *
 * *NOTE:* Using [Renderer] (built-in renderers or `LitRenderer`) is more efficient
 * than [ComponentRenderer]
 *
 * Example of use:
 * ```kotlin
 * column(buildLitRenderer<Category> { ... }) {
 *   flexGrow = 0; key = "edit"
 * }
 * ```
 * @param renderer a (built-in renderer or `LitRenderer`) used to create the grid cell structure
 * @return the new column
 */
@VaadinDsl
public fun <T> (@VaadinDsl Grid<T>).column(
    renderer: Renderer<T>,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> {
    val column = addColumn(renderer)
    column.block()
    return column
}

/**
 * Adds a column for given [property]. The column [key] is set to the property name, so that you can look up the column
 * using `Grid.getColumnBy()`. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise.
 *
 * The header title is set to the property name, converted from camelCase to Human Friendly.
 *
 * WARNING: if an in-memory data provider is used, the sorting will be performed according to the
 * outcome of the [converter]. This may not be wanted, e.g. when the converter converts
 * date to a string. In this case, it's better to use the `columnFor(Renderer)`.
 * @param converter optionally converts the property value [V] to something else, typically to a String. Use this for formatting of the value.
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
@VaadinDsl
public fun <T, V> (@VaadinDsl TreeGrid<T>).hierarchyColumnFor(
    property: KProperty1<T, V?>,
    sortable: Boolean = true,
    key: String = property.name,
    converter: (V?) -> Any? = { it },
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
    addHierarchyColumnFor(property, sortable, key, converter).apply {
        block()
    }

/**
 * Adds a column for given [propertyName]. The column key is set to the property name, so that you can look up the column
 * using `Grid.getColumnBy`. The column is also by default set to sortable
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
@VaadinDsl
public inline fun <reified T, reified V> (@VaadinDsl TreeGrid<T>).hierarchyColumnFor(
    propertyName: String,
    sortable: Boolean = true,
    key: String = propertyName,
    noinline converter: (V?) -> Any? = { it },
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> =
    addHierarchyColumnFor(propertyName, sortable, key, converter).apply { block() }

/**
 * Adds a new text column to this [Grid] with a value provider and
 * default column factory. The value is converted to String when sent to the
 * client by using [java.lang.String.valueOf].
 *
 * *NOTE:* For displaying components, see
 * {@link #addComponentColumn(ValueProvider)}. For using build-in renderers,
 * see {@link #addColumn(Renderer)}.
 *
 * Every added column sends data to the client side regardless of its
 * visibility state. Don't add a new column at all or use
 * [Grid.removeColumn] to avoid sending extra data.
 *
 * Example of use:
 * ```kotlin
 * column({ it.getReviewCount() }) {
 *   setHeader("Beverages")
 * }
 * ```
 * @param block runs given block on the column.
 * @param T the type of the bean stored in the Grid
 * @return the newly created column
 */
@VaadinDsl
public fun <T> (@VaadinDsl TreeGrid<T>).hierarchyColumn(
    valueProvider: ValueProvider<T, *>,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> {
    val column = addHierarchyColumn(valueProvider)
    column.block()
    return column
}

/**
 * Adds a new column that shows components.
 *
 * This is a shorthand for [Grid.addColumn] with a [ComponentRenderer].
 *
 * *NOTE:* Using [ComponentRenderer] is not as efficient as the
 * built-in renderers or using `LitRenderer`.
 *
 * Every added column sends data to the client side regardless of its
 * visibility state. Don't add a new column at all or use
 * [Grid.removeColumn] to avoid sending extra data.
 *
 * Example of use:
 * ```kotlin
 * componentColumn({ category -> createEditButton(category) }) {
 *   flexGrow = 0; key = "edit"
 * }
 * ```
 * @param componentProvider a value provider that will return a component for the given item
 * @param <V> the component type
 * @return the new column
 */
@VaadinDsl
public fun <T, V : Component?> (@VaadinDsl TreeGrid<T>).componentHierarchyColumn(
    componentProvider: ValueProvider<T, V>,
    block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
): Grid.Column<T> {
    val column = addComponentHierarchyColumn(componentProvider)
    column.block()
    return column
}
