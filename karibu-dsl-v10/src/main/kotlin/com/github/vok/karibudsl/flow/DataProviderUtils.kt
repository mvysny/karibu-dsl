package com.github.vok.karibudsl.flow

import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import java.util.stream.Stream
import kotlin.reflect.KProperty1
import kotlin.streams.toList

/**
 * Returns all items provided by this data provider as an eager list. Careful with larger data!
 */
fun <T: Any, F> DataProvider<T, F>.getAll(): List<T> = fetch(Query()).toList()

/**
 * Creates a new data provider which delegates to [delegate] but always appends given list of sort orders.
 * This class can be used to specify the default ordering if the Grid is currently unsorted.
 */
class AppendSortDataProvider<T, F> (private val append: List<QuerySortOrder>, private val delegate: DataProvider<T, F>) : DataProvider<T, F> by delegate {
    init {
        require(!delegate.isInMemory) { "$delegate is in-memory which is unsupported" }
    }
    override fun fetch(query: Query<T, F>): Stream<T> {
        val q = Query(query.offset, query.limit, query.sortOrders + append, query.inMemorySorting, query.filter.orElse(null))
        return delegate.fetch(q)
    }
}

/**
 * Returns a new data provider which delegates to receiver but always appends given list of [sort] orders.
 * This class can be used to specify the default ordering if the Grid is currently unsorted.
 *
 * Cannot be used on in-memory data provider - this function will throw [IllegalArgumentException] if receiver is an in-memory data provider.
 *
 * Example of usage: `grid.dataProvider = Person.dataProvider.sortedBy(Person::name.asc)`
 * @receiver delegate all data fetching calls to here
 * @param sort append these sort criteria. May be empty - in that case just returns the receiver.
 */
fun <T, F> DataProvider<T, F>.sortedBy(vararg sort: QuerySortOrder): DataProvider<T, F> = when {
    sort.isEmpty() -> this
    else -> AppendSortDataProvider(sort.toList(), this)
}

val <T> KProperty1<T, *>.asc: QuerySortOrder get() = QuerySortOrder(name, SortDirection.ASCENDING)
val <T> KProperty1<T, *>.desc: QuerySortOrder get() = QuerySortOrder(name, SortDirection.DESCENDING)
