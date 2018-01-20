package com.github.karibu.testing

import com.github.vok.karibudsl.hasStyleName
import com.github.vok.karibudsl.walk
import com.vaadin.data.HasValue
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.UI

/**
 * A criterion for matching components. The component must match all of non-null fields.
 * @property clazz the class of the component we are searching for.
 * @property id the required [Component.getId]; if null, no particular id is matched.
 * @property caption the required [Component.caption]; if null, no particular caption is matched.
 * @property styles if not null, the component must match all of these styles. Space-separated.
 * @property count expected count of matching components, defaults to `0..Int.MAX_VALUE`
 * @property predicates the predicates the component needs to match, not null. May be empty - in such case it is ignored.
 */
class SearchSpec<T : Component>(
    val clazz: Class<T>,
    var id: String? = null,
    var caption: String? = null,
    var styles: String? = null,
    var count: IntRange = 0..Int.MAX_VALUE,
    var predicates: List<(Component) -> Boolean> = listOf()
) {

    override fun toString(): String {
        val list = mutableListOf<String>(if (clazz.simpleName.isBlank()) clazz.name else clazz.simpleName)
        if (id != null) list.add("id='$id'")
        if (caption != null) list.add("caption='$caption'")
        if (!styles.isNullOrBlank()) list.add("styles='$styles'")
        if (count != (0..Int.MAX_VALUE)) list.add("count=$count")
        list.addAll(predicates.map { it.toString() })
        return list.joinToString(" and ")
    }

    fun toPredicate(): (Component) -> Boolean {
        val p = mutableListOf<(Component)->Boolean>()
        p.add({ component -> clazz.isInstance(component)} )
        if (id != null) p.add({ component -> component.id == id })
        if (caption != null) p.add({ component -> component.caption == caption })
        if (!styles.isNullOrBlank()) p.add({ component -> component.hasStyleName(styles!!) })
        p.addAll(predicates)
        return p.and()
    }
}

/**
 * Finds a VISIBLE component of given type which matches given [block]. This component and all of its descendants are searched.
 * @param block the search specification
 * @return the only matching component, never null.
 * @throws IllegalArgumentException if no component matched, or if more than one component matches.
 */
inline fun <reified T: Component> Component._get(noinline block: SearchSpec<T>.()->Unit = {}): T = this._get(T::class.java, block)

/**
 * Finds a VISIBLE component of given [clazz] which matches given [block]. This component and all of its descendants are searched.
 * @param clazz the component must be of this class.
 * @param block the search specification
 * @return the only matching component, never null.
 * @throws IllegalArgumentException if no component matched, or if more than one component matches.
 */
fun <T: Component> Component._get(clazz: Class<T>, block: SearchSpec<T>.()->Unit = {}): T {
    val result = _find(clazz) {
        count = 1..1
        block()
    }
    return clazz.cast(result.single())
}

/**
 * Finds a VISIBLE component in the current UI of given type which matches given [block]. The [UI.getCurrent] and all of its descendants are searched.
 * @return the only matching component, never null.
 * @throws IllegalArgumentException if no component matched, or if more than one component matches.
 */
inline fun <reified T: Component> _get(noinline block: SearchSpec<T>.()->Unit = {}): T = _get(T::class.java, block)

/**
 * Finds a VISIBLE component in the current UI of given [clazz] which matches given [block]. The [UI.getCurrent] and all of its descendants are searched.
 * @param clazz the component must be of this class.
 * @param block the search specification
 * @return the only matching component, never null.
 * @throws IllegalArgumentException if no component matched, or if more than one component matches.
 */
fun <T: Component> _get(clazz: Class<T>, block: SearchSpec<T>.()->Unit = {}): T = UI.getCurrent()._get(clazz, block)

/**
 * Finds a list of VISIBLE components of given [clazz] which matches [block]. This component and all of its descendants are searched.
 * @return the list of matching components, may be empty.
 */
fun <T: Component> Component._find(clazz: Class<T>, block: SearchSpec<T>.()->Unit = {}): List<T> {
    val spec = SearchSpec(clazz)
    spec.block()
    val result = find(spec.toPredicate())
    if (result.size !in spec.count) {
        val message = when {
            result.isEmpty() -> "No visible ${clazz.simpleName}"
            result.size < spec.count.first -> "Too few (${result.size}) visible ${clazz.simpleName}s"
            else -> "Too many visible ${clazz.simpleName}s (${result.size})"
        }
        throw IllegalArgumentException("$message in ${toPrettyString()} matching $spec: ${result.joinToString { it.toPrettyString() }}. Component tree:\n${toPrettyTree()}")
    }
    return result.filterIsInstance(clazz)
}

/**
 * Finds a list of VISIBLE components of given type which matches [block]. This component and all of its descendants are searched.
 * @return the list of matching components, may be empty.
 */
inline fun <reified T: Component> Component._find(noinline block: SearchSpec<T>.()->Unit = {}): List<T> = this._find(T::class.java, block)

/**
 * Finds a list of VISIBLE components in the current UI of given type which matches given [block]. The [UI.getCurrent] and all of its descendants are searched.
 * @param block the search specification
 * @return the list of matching components, may be empty.
 */
inline fun <reified T: Component> _find(noinline block: SearchSpec<T>.()->Unit = {}): List<T> = _find(T::class.java, block)

/**
 * Finds a list of VISIBLE components of given [clazz] which matches [block]. The [UI.getCurrent] and all of its descendants are searched.
 * @return the list of matching components, may be empty.
 */
fun <T: Component> _find(clazz: Class<T>, block: SearchSpec<T>.()->Unit = {}): List<T> =
        UI.getCurrent()._find(clazz, block)

/**
 * Clicks the button, but only if it is actually possible to do so by the user. If the button is read-only or disabled, it throws an exception.
 * @throws IllegalArgumentException if the button was not visible, not enabled, read-only or if no button (or too many buttons) matched.
 */
fun Button._click() {
    if (!isEffectivelyVisible()) {
        throw IllegalArgumentException("The button ${toPrettyString()} is not effectively visible - either it is hidden, or its ascendant is hidden")
    }
    if (!isEnabled) {
        throw IllegalArgumentException("The button ${toPrettyString()} is not enabled")
    }
    if (!isConnectorEnabled) {
        throw IllegalArgumentException("The button ${toPrettyString()} is nested in a disabled component")
    }
    if (this is HasValue<*> && this.isReadOnly) {
        throw IllegalArgumentException("The button ${toPrettyString()} is read-only")
    }
    click()
}

private fun Component.isEffectivelyVisible(): Boolean = isVisible && (parent == null || parent.isEffectivelyVisible())

private fun Component.find(predicate: (Component)->Boolean): List<Component> = walk().filter { predicate(it) }

private fun <T: Component> Iterable<(T)->Boolean>.and(): (T)->Boolean = { component -> all { it(component) } }
