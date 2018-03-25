package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.data.selection.SelectionEvent
import com.vaadin.flow.data.selection.SelectionModel
import com.vaadin.flow.shared.util.SharedUtil
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.reflect.Method
import java.util.Comparator
import kotlin.reflect.KProperty1

fun <T : Any?> (@VaadinDsl HasComponents).grid(dataProvider: DataProvider<T, *>? = null, block: (@VaadinDsl Grid<T>).() -> Unit = {}) = init(Grid<T>()) {
    if (dataProvider != null) this.dataProvider = dataProvider
    block()
}

/**
 * Refreshes the Grid and re-polls for data.
 */
fun (@VaadinDsl Grid<*>).refresh() = dataProvider.refreshAll()

val Grid<*>.isMultiSelect: Boolean get() = selectionModel is SelectionModel.Multi<*>
val Grid<*>.isSingleSelect: Boolean get() = selectionModel is SelectionModel.Single<*>
val SelectionEvent<*>.isSelectionEmpty: Boolean get() = !firstSelectedItem.isPresent

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
        // need to set the comparator as well: https://github.com/vaadin/flow/issues/3759
        setComparator(compareBy { value.get(it) as Comparable<*> })
    }

/**
 * Retrieves the column for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @throws IllegalArgumentException if no such column exists.
 */
fun <T> Grid<T>.getColumnBy(property: KProperty1<T, *>): Grid.Column<T> =
    getColumnByKey(property.name) ?: throw IllegalArgumentException("No column with key $property; available column keys: ${columns.map { it.key } .filterNotNull()}")

/**
 * Returns the getter method for given property name; fails if there is no such getter.
 */
fun Class<*>.getGetter(propertyName: String): Method {
    val descriptors: Array<out PropertyDescriptor> = Introspector.getBeanInfo(this).propertyDescriptors
    val descriptor: PropertyDescriptor? = descriptors.firstOrNull { it.name == propertyName }
    requireNotNull(descriptor) { "No such field '$propertyName' in $this; available properties: ${descriptors.joinToString { it.name }}" }
    val getter: Method = requireNotNull(descriptor!!.readMethod) { "The $this.$propertyName property does not have a getter: $descriptor" }
    return getter
}

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
inline fun <reified T, reified V> Grid<T>.addColumnFor(propertyName: String,
                                                       sortable: Boolean = true,
                                                       noinline converter: (V?)->Any? = { it },
                                                       block: Grid.Column<T>.() -> Unit = {}): Grid.Column<T> {
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
inline fun <reified T, reified V> Grid<T>.addColumnFor(propertyName: String,
                                                       renderer: Renderer<T>,
                                                       sortable: Boolean = true,
                                                       block: Grid.Column<T>.() -> Unit = {}): Grid.Column<T> =
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
