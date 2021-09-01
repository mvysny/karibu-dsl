package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributools.findAncestor
import com.github.mvysny.kaributools.findAncestorOrSelf
import com.vaadin.flow.component.*
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.dom.Element
import com.vaadin.flow.shared.Registration

/**
 * A TabSheet - shows both the [Tabs] component and the tab contents.
 *
 * You can add and populate tabs in two ways:
 * * eagerly, by calling either [tab] or [addTab] function,
 * * lazily, by calling [addLazyTab].
 *
 * Example code:
 * ```
 * tabSheet {
 *   tab("DSL-style tab") {
 *     span("Hello")
 *   }
 *   addTab("Old-school style", Span("Hi"))
 *   addLazyTab("Populated when first selected") { Span("Lazy") }
 * }
 * ```
 */
public class TabSheet : KComposite(), HasStyle, HasSize {
    private lateinit var tabsComponent: Tabs
    private lateinit var tabsContainer: Div

    /**
     * Maps [Tab] to the contents of the tab.
     */
    private val tabsToComponents: MutableMap<Tab, Component?> = mutableMapOf()

    /**
     * Maps [Tab] to the provider of the contents of the tab.
     */
    private val tabsToComponentProvider: MutableMap<Tab, ()->Component> = mutableMapOf()

    private val root = ui {
        verticalLayout(false, false) {
            setWidthFull(); content { align(stretch, top) }; addClassName("tabsheet")

            tabsComponent = tabs()
            tabsContainer = div {
                setWidthFull()
                // when TabSheet's height is defined, the following rules allow the container to grow or shrink as necessary.
                isExpand = true; flexShrink = 1.0; minHeight = "0px"
                element.classList.add("tabsheet-container")
            }
        }
    }

    init {
        tabsComponent.addSelectedChangeListener { update() }
    }

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
    public fun tab(label: String? = null, block: (@VaadinDsl HasComponents).() -> Component? = { null }): Tab {
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
        val content: Component? = dummy.block()
        return addTab(label, content)
    }

    /**
     * Adds a new tab to the tab host, with optional [label] and optional [contents].
     *
     * You can either provide the tab contents eagerly, or you can populate the tab
     * later on, by calling [setTabContents] or
     * [Tab.contents]. To make the tab populate itself automatically when it's shown
     * for the first time, use [addLazyTab].
     */
    public fun addTab(label: String? = null, contents: Component? = null): Tab {
        val tab: Tab = tabsComponent.tab(label)
        tabsToComponents[tab] = contents
        update()
        return tab
    }

    /**
     * Adds a new tab to the tab host, with optional [label]. The tab contents is
     * constructed lazily when the tab is selected for the first time.
     */
    public fun addLazyTab(label: String? = null, contentsProvider: ()->Component): Tab {
        val tab: Tab = tabsComponent.tab(label)
        tabsToComponents[tab] = null
        tabsToComponentProvider[tab] = contentsProvider
        update()
        return tab
    }

    /**
     * Sets the contents of given [tab] to [newContents].
     */
    public fun setTabContents(tab: Tab, newContents: Component?) {
        checkOurTab(tab)
        tabsToComponents[tab] = newContents
        tabsToComponentProvider.remove(tab)
        update()
    }

    /**
     * Finds a tab containing given [contents]. Returns null if there is no
     * such tab.
     */
    public fun findTabWithContents(contents: Component): Tab? =
            tabsToComponents.entries.firstOrNull { it.value == contents } ?.key

    /**
     * Finds a tab which transitively contains given [component].
     */
    public fun findTabContaining(component: Component): Tab? {
        val contentComponents: Set<Component> = tabsToComponents.values.filterNotNull().toSet()
        val contents: Component = component.findAncestorOrSelf { contentComponents.contains(it) } ?: return null
        return findTabWithContents(contents)
    }

    /**
     * Returns the contents of given [tab].
     */
    public fun getTabContents(tab: Tab): Component? {
        checkOurTab(tab)
        return tabsToComponents[tab]
    }

    private fun checkOurTab(tab: Tab) {
        require(tabsToComponents.containsKey(tab)) {
            "Tab $tab is not hosted in this TabSheet"
        }
    }

    /**
     * Removes a [tab]. If the tab is selected, another tab is selected automatically (if possible).
     */
    public fun remove(tab: Tab) {
        tabsToComponents.remove(tab)
        tabsComponent.remove(tab)
        update()
    }

    /**
     * Currently selected tab. Defaults to null since by default there are no tabs.
     */
    public var selectedTab: Tab?
        get() = tabsComponent.selectedTab
        set(value) {
            tabsComponent.selectedTab = value
        }

    /**
     * Returns the 0-based index of the currently selected tab.
     */
    public var selectedIndex: Int
        get() = tabsComponent.selectedIndex
        set(value) {
            tabsComponent.selectedIndex = value
        }

    /**
     * Returns the current number of tabs.
     */
    public val tabCount: Int get() = tabsToComponents.keys.count()

    /**
     * The [orientation] of this tab sheet. Defaults to [Tabs.Orientation.HORIZONTAL].
     */
    public var orientation: Tabs.Orientation
        get() = tabsComponent.orientation
        set(value) {
            tabsComponent.orientation = value
        }

    private fun update() {
        val currentTabComponent: Component? = tabsContainer.children.findFirst().orElse(null)
        val selectedTab1: Tab? = selectedTab

        var newTabComponent: Component?
        if (selectedTab1 == null) {
            newTabComponent = null
        } else {
            newTabComponent = tabsToComponents[selectedTab1]
            if (newTabComponent == null) {
                val provider = tabsToComponentProvider[selectedTab1]
                if (provider != null) {
                    newTabComponent = provider()
                    tabsToComponentProvider.remove(selectedTab1)
                    tabsToComponents[selectedTab1] = newTabComponent
                }
            }
        }

        if (currentTabComponent != newTabComponent) {
            tabsContainer.removeAll()
            if (newTabComponent != null) {
                tabsContainer.add(newTabComponent)
            }
        }
    }

    /**
     * Removes all tabs.
     */
    public fun removeAll() {
        tabsToComponents.clear()
        tabsComponent.removeAll()
        update()
    }

    /**
     * Returns a live list of all tabs. The list is read-only but live: it reflects changes when tabs are added or removed.
     */
    public val tabs: List<Tab> = object : AbstractList<Tab>() {
        override val size: Int
            get() = tabCount

        override fun get(index: Int): Tab = tabsComponent.getComponentAt(index) as Tab
    }

    public fun addSelectedChangeListener(listener: ComponentEventListener<Tabs.SelectedChangeEvent>): Registration =
            tabsComponent.addSelectedChangeListener(listener)
}

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

/**
 * Returns or sets this tab contents in the [TabSheet]. Works only for tabs nested in a [TabSheet].
 */
public var Tab.contents: Component?
    get() = ownerTabSheet.getTabContents(this)
    set(value) {
        ownerTabSheet.setTabContents(this, value)
    }


/**
 * Creates a [TabSheet] component which shows both the list of tabs, and the tab contents itself.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).tabSheet(block: (@VaadinDsl TabSheet).() -> Unit = {}): TabSheet
        = init(TabSheet(), block)
