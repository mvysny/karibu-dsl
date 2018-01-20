package com.github.karibu.testing

import com.vaadin.data.ValueProvider
import com.vaadin.data.provider.DataProvider
import com.vaadin.data.provider.Query
import com.vaadin.shared.MouseEventDetails
import com.vaadin.ui.Grid
import com.vaadin.ui.renderers.ClickableRenderer
import kotlin.streams.toList

/**
 * Returns the item on given row. Fails if the row index is invalid.
 * @param rowIndex the row, 0..size - 1
 * @return the item at given row, not null.
 */
fun <T : Any> DataProvider<T, *>._get(rowIndex: Int): T {
    @Suppress("UNCHECKED_CAST")
    val fetched = (this as DataProvider<T, Any?>).fetch(Query<T, Any?>(rowIndex, 1, null, null, null))
    return fetched.toList().first()
}

/**
 * Returns the number of items in this data provider.
 */
@Suppress("UNCHECKED_CAST")
fun DataProvider<*, *>._size(): Int =
        (this as DataProvider<Any?, Any?>).size(Query(null))

/**
 * Performs a click on a [ClickableRenderer] in given [grid] cell.
 * @receiver the grid, not null.
 * @param rowIndex the row index, 0 or higher.
 * @param columnId the column ID.
 */
fun <T : Any> Grid<T>._clickRenderer(rowIndex: Int, columnId: String) {
    val column = getColumn(columnId)!!
    @Suppress("UNCHECKED_CAST")
    val renderer = column.renderer as ClickableRenderer<T, *>
    val item = dataProvider._get(rowIndex)
    renderer._fireEvent(object : ClickableRenderer.RendererClickEvent<T>(this, item, column, MouseEventDetails()) {})
}

/**
 * Returns the formatted value as a String. Does not use renderer to render the value - simply calls value provider and presentation provider
 * and converts the result to string (even if the result is a [Component]).
 * @param rowIndex the row index, 0 or higher.
 * @param columnId the column ID.
 */
@Suppress("UNCHECKED_CAST")
fun <T: Any> Grid<T>._getFormatted(rowIndex: Int, columnId: String): String {
    val rowObject: T = dataProvider._get(rowIndex)
    val column: Grid.Column<T, *> = getColumn(columnId) ?: throw IllegalArgumentException("There is no column $columnId. Available columns: ${columns.map { it.id }}")
    return column._getFormatted(rowObject)
}

/**
 * Returns the formatted value as a String. Does not use renderer to render the value - simply calls value provider and presentation provider
 * and converts the result to string (even if the result is a [Component]).
 * @param rowIndex the row index, 0 or higher.
 */
@Suppress("UNCHECKED_CAST")
fun <T: Any> Grid.Column<T, *>._getFormatted(rowObject: T): String = "${getPresentationValue(rowObject)}"

fun <T: Any> Grid<T>._getFormattedRow(rowIndex: Int): List<String> {
    val rowObject: T = dataProvider._get(rowIndex)
    return columns.filterNot { it.isHidden } .map { it._getFormatted(rowObject) }
}

@Suppress("UNCHECKED_CAST")
val <V> Grid.Column<*, V>.presentationProvider: ValueProvider<V, *>
    get() =
        javaClass.getDeclaredField("presentationProvider").run {
            isAccessible = true
            get(this@presentationProvider) as ValueProvider<V, *>
        }

fun <T: Any, V> Grid.Column<T, V>.getPresentationValue(rowObject: T): Any? = presentationProvider.apply(valueProvider.apply(rowObject))

/**
 * Dumps the first [maxRows] rows of the Grid, formatting the values using the [_getFormatted] function. The output example:
 * ```
 * --[Name]--[Age]--[Occupation]--
 * 0: John, 25, Service Worker
 * 1: Fred, 40, Supervisor
 * --and 198 more
 * ```
 */
fun <T: Any> Grid<T>._dump(rows: IntRange = 0..10): String = buildString {
    val visibleColumns: List<Grid.Column<T, *>> = columns.filterNot { it.isHidden }
    visibleColumns.map { "[${it.caption}]" } .joinTo(this, prefix = "--", separator = "-", postfix = "--\n")
    val dsIndices: IntRange = 0 until dataProvider._size()
    val displayIndices = rows.intersect(dsIndices)
    for (i in displayIndices) {
        _getFormattedRow(i).joinTo(this, prefix = "$i: ", postfix = "\n")
    }
    val andMore = dsIndices.size - displayIndices.size
    if (andMore > 0) {
        append("--and $andMore more\n")
    }
}

fun Grid<*>.expectRows(count: Int) {
    if (dataProvider._size() != count) {
        throw AssertionError("${this.toPrettyString()}: expected $count rows\n${_dump()}")
    }
}

fun Grid<*>.expectRow(rowIndex: Int, vararg row: String) {
    val expected = row.toList()
    val actual = _getFormattedRow(rowIndex)
    if (expected != actual) {
        throw AssertionError("${this.toPrettyString()} at $rowIndex: expected $expected but got $actual\n${_dump()}")
    }
}
