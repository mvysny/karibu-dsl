package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.kaributools.findAncestor
import com.github.mvysny.kaributools.findAncestorOrSelf
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.dom.Element
import java.lang.reflect.Field

/**
 * Creates a [TabSheet] component which shows both the list of tabs, and the tab contents itself.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).tabSheet(block: (@VaadinDsl TabSheet).() -> Unit = {}): TabSheet
        = init(TabSheet(), block)

/**
 * Returns the current index of a tab within its [Tabs] container.
 */
public val Tab.index: Int
    get() = owner.indexOf(this)

/**
 * Returns the owner [Tabs] of this tab. Fails if the tab is not attached to any [Tabs] owner.
 */
public val Tab.owner: Tabs
    get() = checkNotNull((parent.orElse(null)) as Tabs?) { "tab $this is not attached to a parent" }

public val Tab.ownerTabSheet: TabSheet
    get() = checkNotNull(findAncestor { it is TabSheet }) { "tab $this is not attached to a TabSheet" } as TabSheet

private val __TabSheet_tabToContent: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val f = TabSheet::class.java.getDeclaredField("tabToContent")
    f.isAccessible = true
    f
}
@Suppress("UNCHECKED_CAST")
private val TabSheet.tabToContent: Map<Tab, Element>
    get() = __TabSheet_tabToContent.get(this) as Map<Tab, Element>

private val __TabSheet_tabs: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val f = TabSheet::class.java.getDeclaredField("tabs")
    f.isAccessible = true
    f
}
@Suppress("UNCHECKED_CAST")
private val TabSheet.tabsComponent: Tabs
    get() = __TabSheet_tabs.get(this) as Tabs

/**
 * Returns or sets this tab contents in the [TabSheet]. Works only for tabs nested in a [TabSheet].
 */
public val Tab.contents: Component? get() = ownerTabSheet.tabToContent[this]?.component?.orElse(null)

/**
 * Adds a new tab to the tab host, with optional [label] and optional contents.
 *
 * Example:
 * ```
 * tabSheet {
 *   tab("Hello") {
 *     verticalLayout {
 *       span("Hello, world!")
 *     }
 *   }
 * }
 * ```
 */
@VaadinDsl
public fun TabSheet.tab(label: String? = null, block: (@VaadinDsl HasComponents).() -> Unit): Tab {
    var root: Component? = null
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            require(components.size < 2) { "Too many components to add - tab can only host one! ${components.toList()}" }
            val component: Component = components.firstOrNull() ?: return
            check(root == null) { "The content has already been initialized!" }
            root = component
        }
    }
    dummy.block()
    return add(label, root)
}

/**
 * Returns the current number of tabs.
 */
public val TabSheet.tabCount: Int get() = tabsComponent.componentCount

/**
 * Removes all tabs.
 */
public fun TabSheet.removeAll() {
    tabs.toList().forEach { remove(it) }
}

/**
 * Returns a live list of all tabs. The list is read-only but live: it reflects changes when tabs are added or removed.
 */
public val TabSheet.tabs: List<Tab> get() = object : AbstractList<Tab>() {
    override val size: Int
        get() = tabCount

    override fun get(index: Int): Tab = tabsComponent.getComponentAt(index) as Tab
}

public fun TabSheet.findTabWithContents(content: Component): Tab? =
    tabs.firstOrNull { it.contents == content }

/**
 * Finds a tab which transitively contains given [component].
 */
public fun TabSheet.findTabContaining(component: Component): Tab? {
    val contentComponents: Set<Component> = tabToContent.values.mapNotNull {it.component.orElse(null) }.toSet()
    val contents: Component = component.findAncestorOrSelf { contentComponents.contains(it) } ?: return null
    return findTabWithContents(contents)
}
