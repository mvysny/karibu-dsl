package com.github.mvysny.karibudsl.v8

import com.vaadin.ui.*
import com.vaadin.ui.components.grid.FooterCell
import com.vaadin.ui.components.grid.FooterRow
import com.vaadin.ui.components.grid.HeaderCell
import com.vaadin.ui.components.grid.HeaderRow
import java.io.Serializable

/**
 * Represents a mutable class list (or style names as Vaadin calls them).
 */
interface ClassList : MutableSet<String>, Serializable {

    /**
     * Sets or removes the given class name, based on the `set` parameter.
     *
     * @param className the class name to set or remove, cannot contain spaces.
     * @param set true to set the class name, false to remove it
     * @return true if the class list was modified (class name added or
     * removed), false otherwise
     */
    fun set(className: String, set: Boolean): Boolean {
        require(!className.containsWhitespace()) { "'$className' cannot contain whitespace" }
        return if (set) {
            add(className)
        } else {
            remove(className)
        }
    }

    /**
     * Toggles given [className] (removes it if it's present, or adds it if it's absent).
     * @param className the class name to toggle, cannot contain spaces.
     */
    fun toggle(className: String) {
        require(!className.containsWhitespace()) { "'$className' cannot contain whitespace" }
        set(className, !contains(className))
    }
}

abstract class AbstractStringBasedClassList : AbstractMutableSet<String>(), ClassList {
    /**
     * Returns a space-separated list of style names.
     */
    protected abstract fun getStyleName(): String

    /**
     * Sets new space-separated list of style names, overriding any previous value.
     */
    protected abstract fun setStyleName(styleNames: Set<String>)

    override val size: Int
        get() = styleNames.size

    private val styleNames: Set<String> get() = getStyleName().split(' ').filterNotBlank().toSet()

    override fun contains(element: String): Boolean {
        require(!element.containsWhitespace()) { "'$element' cannot contain whitespace" }
        return styleNames.contains(element)
    }

    override fun add(element: String): Boolean {
        require(!element.containsWhitespace()) { "'$element' cannot contain whitespace" }
        val names = styleNames
        if (!names.contains(element)) {
            setStyleName(names + element)
            return true
        }
        return false
    }

    override fun remove(element: String): Boolean {
        require(!element.containsWhitespace()) { "'$element' cannot contain whitespace" }
        val names = styleNames
        if (names.contains(element)) {
            setStyleName(names - element)
            return true
        }
        return false
    }

    private inner class ClassListIterator : MutableIterator<String> {
        private val delegate = styleNames.iterator()
        private lateinit var current: String
        override fun hasNext(): Boolean = delegate.hasNext()
        override fun next(): String {
            current = delegate.next()
            return current
        }

        override fun remove() {
            remove(current)
        }
    }

    override fun iterator(): MutableIterator<String> = ClassListIterator()

    override fun isEmpty(): Boolean = getStyleName().isBlank()

    override fun clear() {
        setStyleName(setOf())
    }
}

private class ComponentClassList(val component: Component) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = component.styleName
    override fun setStyleName(styleNames: Set<String>) {
        component.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl Component).styleNames: ClassList get() = ComponentClassList(this)

/**
 * Adds or removes given [style] from the component, depending on the value of the [isPresent] parameter.
 */
fun (@VaadinDsl Component).toggleStyleName(style: String, isPresent: Boolean) {
    if (isPresent) addStyleName(style) else removeStyleName(style)
}

/**
 * Checks whether the component has given [style] (or multiple styles if the string contains space).
 * @param style if contains a space, this is considered to be a list of styles. In such case, all styles must be present on the component.
 */
fun (@VaadinDsl Component).hasStyleName(style: String): Boolean {
    if (style.contains(' ')) return style.split(' ').filterNotBlank().all { hasStyleName(style) }
    return styleNames.contains(style)
}

private class NotificationClassList(val notification: Notification) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = notification.styleName
    override fun setStyleName(styleNames: Set<String>) {
        notification.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl Notification).styleNames: ClassList get() = NotificationClassList(this)

private class HasStylesClassList(val hasStyles: HasStyleNames) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = hasStyles.styleName
    override fun setStyleName(styleNames: Set<String>) {
        hasStyles.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl HasStyleNames).styleNames: ClassList get() = HasStylesClassList(this)

private class TabClassList(val tab: TabSheet.Tab) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = tab.styleName
    override fun setStyleName(styleNames: Set<String>) {
        tab.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl TabSheet.Tab).styleNames: ClassList get() = TabClassList(this)

private class MenuItemClassList(val menuItem: MenuBar.MenuItem) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = menuItem.styleName
    override fun setStyleName(styleNames: Set<String>) {
        menuItem.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl MenuBar.MenuItem).styleNames: ClassList get() = MenuItemClassList(this)

private class FooterCellClassList(val footerCell: FooterCell) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = footerCell.styleName
    override fun setStyleName(styleNames: Set<String>) {
        footerCell.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl FooterCell).styleNames: ClassList get() = FooterCellClassList(this)

private class FooterRowClassList(val footerRow: FooterRow) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = footerRow.styleName
    override fun setStyleName(styleNames: Set<String>) {
        footerRow.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl FooterRow).styleNames: ClassList get() = FooterRowClassList(this)

private class HeaderRowClassList(val headerRow: HeaderRow) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = headerRow.styleName
    override fun setStyleName(styleNames: Set<String>) {
        headerRow.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl HeaderRow).styleNames: ClassList get() = HeaderRowClassList(this)

private class HeaderCellClassList(val headerCell: HeaderCell) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = headerCell.styleName
    override fun setStyleName(styleNames: Set<String>) {
        headerCell.styleName = styleNames.joinToString(" ")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl HeaderCell).styleNames: ClassList get() = HeaderCellClassList(this)
