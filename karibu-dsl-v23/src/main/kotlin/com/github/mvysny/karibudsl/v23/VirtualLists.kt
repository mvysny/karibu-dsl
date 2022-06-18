package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.virtuallist.VirtualList
import com.vaadin.flow.data.provider.DataProvider

@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).virtualList(
    dataProvider: DataProvider<T, *>? = null,
    block: (@VaadinDsl VirtualList<T>).() -> Unit = {}
): VirtualList<T> {
    val vl = VirtualList<T>()
    if (dataProvider != null) {
        vl.dataProvider = dataProvider
    }
    return init(vl, block)
}
