package com.github.karibu.testing

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
