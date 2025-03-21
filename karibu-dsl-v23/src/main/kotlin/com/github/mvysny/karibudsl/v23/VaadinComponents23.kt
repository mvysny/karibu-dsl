package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.combobox.MultiSelectComboBox
import com.vaadin.flow.component.popover.Popover
import com.vaadin.flow.component.popover.PopoverPosition
import com.vaadin.flow.component.virtuallist.VirtualList
import com.vaadin.flow.data.provider.DataProvider

/**
 * Creates a [Vaadin Combo Box](https://vaadin.com/elements/vaadin-combo-box). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun <T : Any?> (@VaadinDsl HasComponents).multiSelectComboBox(label: String? = null, block: (@VaadinDsl MultiSelectComboBox<T>).() -> Unit = {}): MultiSelectComboBox<T>
        = init(MultiSelectComboBox(label), block)

/**
 * [Virtual List](https://vaadin.com/docs/latest/components/virtual-list) allows you to render a long list of items inside a scrollable container without sacrificing performance. Each item is rendered on the fly as the user scrolls the list.
 * ```
 * virtualList<String> {
 *   setItems("a", "b", "c")
 *   setRenderer(ComponentRenderer(SerializableFunction { Span(it) }))
 * }
 * ```
 */
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

/**
 * Creates a [Popover] targeted to this component. You can further populate
 * the Popover with contents.
 * ```
 * button("hello") {
 *   popover {
 *     isOpenOnFocus = true
 *     verticalLayout {
 *       span("Hi!")
 *     }
 *   }
 * }
 * ```
 * @param position the optional position of the popover, defaults to null.
 * @receiver [Popover.target] will be set to this.
 */
@VaadinDsl
public fun (@VaadinDsl Component).popover(position: PopoverPosition? = null, block: (@VaadinDsl Popover).() -> Unit = {}): Popover {
    val popover = Popover()
    popover.target = this
    if (position != null) {
        popover.position = position
    }
    popover.block()
    return popover
}
