package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.combobox.MultiSelectComboBox
import com.vaadin.flow.component.virtuallist.VirtualList
import com.vaadin.flow.data.provider.DataProvider

/**
 * Creates a [Vaadin Combo Box](https://vaadin.com/elements/vaadin-combo-box). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).multiSelectComboBox(label: String? = null, block: (@VaadinDsl MultiSelectComboBox<T>).() -> Unit = {}): MultiSelectComboBox<T>
        = init(MultiSelectComboBox(label), block)

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

@VaadinDsl
public fun (@VaadinDsl HasComponents).avatar(name: String? = null, imageUrl: String? = null, block: (@VaadinDsl Avatar).() -> Unit = {}): Avatar {
    val avatar = Avatar()
    if (name != null) {
        avatar.name = name
    }
    if (imageUrl != null) {
        avatar.image = imageUrl
    }
    return init(avatar, block)
}
