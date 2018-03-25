package com.github.vok.karibudsl

import com.vaadin.data.provider.DataProvider
import com.vaadin.data.provider.Query
import com.vaadin.data.provider.QuerySortOrder
import com.vaadin.shared.data.sort.SortDirection
import kotlin.reflect.KProperty1
import kotlin.streams.toList

/**
 * Returns all items provided by this data provider as an eager list. Careful with larger data!
 */
fun <T: Any, F> DataProvider<T, F>.getAll(): List<T> = fetch(Query()).toList()

val <T> KProperty1<T, *>.asc: QuerySortOrder get() = QuerySortOrder(name, SortDirection.ASCENDING)
val <T> KProperty1<T, *>.desc: QuerySortOrder get() = QuerySortOrder(name, SortDirection.DESCENDING)
