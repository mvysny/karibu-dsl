package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs

public class NavTabs : Tabs() {

    // The returned map preserves the entry iteration order.
    public val tabsClickHandlers: MutableMap<Tab, () -> Unit> = mutableMapOf()

}

/**
 *
 * Example of usage:
 * ```kotlin
 * navTabs {
 *  tab("People") {
 *      icon(VaadinIcon.USERS)
 *      onClick {
 *          content {
 *              span("click in the menu ;-) , you will see me never again..")
 *          }
 *      }
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).navTabs(
    orientation: Tabs.Orientation? = null,
    initialTabIndex: Int = 0,
    block: (@VaadinDsl NavTabs).() -> Unit = {}
): NavTabs {
    val component = NavTabs()
    if (orientation != null) {
        component.orientation = orientation
    }
    return init(component, block).apply {
        addSelectedChangeListener {
            tabsClickHandlers[it.selectedTab]!!.invoke()
        }
        tabsClickHandlers[getTabAt(initialTabIndex)]?.invoke()
    }
}

@VaadinDsl
public fun (@VaadinDsl Tab).onClick(clickHandler: () -> Unit) {

    with (parent.get() as NavTabs) {
        tabsClickHandlers[this@onClick] = clickHandler
    }
}
