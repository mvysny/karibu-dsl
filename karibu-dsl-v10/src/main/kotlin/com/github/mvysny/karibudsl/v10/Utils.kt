package com.github.mvysny.karibudsl.v10

import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.reflect.Method

fun String.containsWhitespace(): Boolean = any { it.isWhitespace() }

private val messages: Map<String, String> = mapOf("cantConvertToInteger" to "Can't convert to integer",
        "cantConvertToDecimal" to "Can't convert to decimal number",
        "from" to "From:",
        "to" to "To:",
        "set" to "Set",
        "clear" to "Clear",
        "all" to "All"
)
/**
 * Change this function to provide a proper i18n for your apps. For a list of all keys used by Karibu-DSL see [messages].
 */
var karibuDslI18n: (key: String) -> String = { key -> messages[key] ?: key }

/**
 * Returns the getter method for given property name; fails if there is no such getter.
 */
fun Class<*>.getGetter(propertyName: String): Method {
    val descriptors: Array<out PropertyDescriptor> = Introspector.getBeanInfo(this).propertyDescriptors
    val descriptor: PropertyDescriptor? = descriptors.firstOrNull { it.name == propertyName }
    requireNotNull(descriptor) { "No such field '$propertyName' in $this; available properties: ${descriptors.joinToString { it.name }}" }
    val getter: Method = requireNotNull(descriptor.readMethod) { "The $this.$propertyName property does not have a getter: $descriptor" }
    return getter
}

/**
 * Represents a (potentially unbounded) range of values (for example, numbers or characters).
 * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/ranges.html) for more information.
 *
 * The [start] or [endInclusive] (or both) may be null, to tell that there is no lower or upper boundary.
 * That may not make the interval infinite since some types are limited in value (for example LocalDate
 * has a minimum and a maximum value).
 */
interface ClosedInterval<T: Comparable<T>> {
    /**
     * The minimum value in the range.
     */
    val start: T?

    /**
     * The maximum value in the range (inclusive).
     */
    val endInclusive: T?

    /**
     * If true then this range accepts any value. True when both [start] and [endInclusive] are null.
     */
    val isUniversalSet: Boolean get() = start == null && endInclusive == null

    /**
     * Checks whether the specified [value] belongs to the range.
     */
    operator fun contains(value: T): Boolean {
        val s: T? = start
        val e: T? = endInclusive
        val matchesLowerBoundary: Boolean = s == null || s <= value
        if (!matchesLowerBoundary) {
            return false
        }
        val matchesUpperBoundary: Boolean = e == null || value <= e
        return matchesUpperBoundary
    }

    /**
     * Checks whether the range is empty.
     */
    fun isEmpty(): Boolean {
        val s: T = start ?: return false
        val e: T = endInclusive ?: return false
        return s > e
    }

    /**
     * True if the interval consists of single number only (is degenerate).
     */
    val isSingleItem: Boolean
        get() = start != null && endInclusive != null && start!!.compareTo(endInclusive!!) == 0
}
