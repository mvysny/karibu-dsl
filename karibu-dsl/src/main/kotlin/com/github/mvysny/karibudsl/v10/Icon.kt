package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.IronIcon
import java.io.Serializable

/**
 * Creates a [Iron Icon](https://vaadin.com/elements/vaadin-icons/). See the HTML Examples link for a list
 * of possible alternative themes for the button.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).icon(icon: VaadinIcon, block: (@VaadinDsl Icon).() -> Unit = {}) = init(Icon(icon), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).icon(collection: String, icon: String, block: (@VaadinDsl Icon).() -> Unit = {}) = init(Icon(collection, icon), block)
@VaadinDsl
fun (@VaadinDsl HasComponents).ironIcon(collection: String, icon: String, block: (@VaadinDsl IronIcon).() -> Unit = {}) = init(IronIcon(collection, icon), block)

/**
 * Represents an icon name and a collection from which the icon came.
 */
data class IconName(val collection: String, val name: String) : Serializable {
    init {
        require(collection.isNotBlank()) { "$collection: collection is blank" }
        require(name.isNotBlank()) { "$name: name is blank" }
    }

    /**
     * Creates a new [Icon] component for this icon.
     */
    fun toIcon() = Icon(collection, name)

    /**
     * Checks whether this icon represents a [VaadinIcon]. Use [asVaadinIcon] to obtain
     * the original icon.
     */
    val isVaadinIcon get() = collection == "vaadin"

    /**
     * If this icon represents a [VaadinIcon], return the appropriate [VaadinIcon]
     * constant, else return null.
     */
    fun asVaadinIcon(): VaadinIcon? {
        if (!isVaadinIcon) {
            return null
        }
        val enumName: String = name.toUpperCase().replace('-', '_')
        return VaadinIcon.valueOf(enumName)
    }

    /**
     * Returns this icon name as a string in the format `collection:name`.
     */
    override fun toString() = "$collection:$name"

    companion object {
        /**
         * Gets the icon name from given [vaadinIcon].
         */
        fun of(vaadinIcon: VaadinIcon) = IconName("vaadin", vaadinIcon.name.toLowerCase().replace('_', '-'))

        /**
         * Parses the [toString] string representation. Returns null if the [iconName] is not in the expected format.
         * @param iconName string representation in the form of `collection:name`.
         */
        fun fromString(iconName: String): IconName? {
            val iconPair = iconName.split(':')
            return if (iconPair.size == 2) IconName(iconPair[0], iconPair[1]) else null
        }
    }
}

/**
 * Returns the icon name and collection from the [Icon] component. Returns null
 * if no icon is set.
 */
var Icon.iconName: IconName?
    get() {
        val icon: String = element.getAttribute("icon")
        return IconName.fromString(icon)
    }
    set(value: IconName?) {
        element.setAttribute("icon", value?.toString() ?: "")
    }

/**
 * Returns the icon name and collection from the [IronIcon] component. Returns null
 * if no icon is set.
 */
var IronIcon.iconName: IconName?
    get() {
        val icon: String = element.getAttribute("icon")
        return IconName.fromString(icon)
    }
    set(value: IconName?) {
        element.setAttribute("icon", value?.toString() ?: "")
    }
