package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.ironlist.IronList
import com.vaadin.flow.data.provider.DataProvider

@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).ironList(
        dataProvider: DataProvider<T, *>? = null,
        block: (@VaadinDsl IronList<T>).() -> Unit = {}
): IronList<T> {
    val list = IronList<T>()
    if (dataProvider != null) {
        list.dataProvider = dataProvider
    }
    return init(list, block)
}

/**
 * Refreshes the component and re-polls for data.
 */
fun (@VaadinDsl IronList<*>).refresh() = dataProvider.refreshAll()
