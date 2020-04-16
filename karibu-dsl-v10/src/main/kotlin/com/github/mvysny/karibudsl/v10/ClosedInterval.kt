package com.github.mvysny.karibudsl.v10

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
        get() = isBound && start!!.compareTo(endInclusive!!) == 0

    /**
     * True if the interval is bound (both [start] and [endInclusive] are not null).
     */
    val isBound: Boolean
        get() = start != null && endInclusive != null
}