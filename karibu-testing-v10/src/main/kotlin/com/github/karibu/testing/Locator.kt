package com.github.karibu.testing

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.router.InternalServerError
import java.util.*
import java.util.stream.Collectors

/**
 * A criterion for matching components. The component must match all of non-null fields.
 * @property clazz the class of the component we are searching for.
 * @property id the required [Component.getId]; if null, no particular id is matched.
 * @property caption the required [Component.caption]; if null, no particular caption is matched.
 * @property text the [com.vaadin.flow.dom.Element.getText]
 * @property count expected count of matching components, defaults to `0..Int.MAX_VALUE`
 * @property predicates the predicates the component needs to match, not null. May be empty - in such case it is ignored.
 */
class SearchSpec<T : Component>(
    val clazz: Class<T>,
    var id: String? = null,
    var caption: String? = null,
    var text: String? = null,
    var count: IntRange = 0..Int.MAX_VALUE,
    var predicates: List<(Component) -> Boolean> = listOf()
) {

    override fun toString(): String {
        val list = mutableListOf<String>(if (clazz.simpleName.isBlank()) clazz.name else clazz.simpleName)
        if (id != null) list.add("id='$id'")
        if (caption != null) list.add("caption='$caption'")
        if (text != null) list.add("text='$text'")
        if (count != (0..Int.MAX_VALUE)) list.add("count=$count")
        list.addAll(predicates.map { it.toString() })
        return list.joinToString(" and ")
    }

    fun toPredicate(): (Component) -> Boolean {
        val p = mutableListOf<(Component)->Boolean>()
        p.add({ component -> clazz.isInstance(component)} )
        if (id != null) p.add({ component -> component.id_ == id })
        if (caption != null) p.add({ component -> component.caption == caption })
        if (text != null) p.add { component -> component.element.text == text }
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
        check(count == 1..1) { "You're calling _get which is supposed to return exactly 1 component, yet you tried to specify the count of $count" }
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

private fun Component.find(predicate: (Component)->Boolean): List<Component> {
    cleanupDialogs()
    val descendants = walk()
    val error: InternalServerError? = descendants.filterIsInstance<InternalServerError>().firstOrNull()
    if (error != null) throw AssertionError("An internal server error occurred; check log for the actual stack-trace. Error text: ${error.element.text}\n${UI.getCurrent().toPrettyTree()}")
    return descendants.filter { predicate(it) }
}

fun cleanupDialogs() {
    // @todo mavi remove when Flow will properly fire Dialog close listeners. See MockedUI for more details
    UI.getCurrent().children.collect(Collectors.toList()).forEach {
        if (it is Dialog && !it.isOpened) it.element.removeFromParent()
    }
}

private fun <T: Component> Iterable<(T)->Boolean>.and(): (T)->Boolean = { component -> all { it(component) } }

private class TreeIterator<out T>(root: T, private val children: (T) -> Iterator<T>) : Iterator<T> {
    private val queue: Queue<T> = LinkedList<T>(listOf(root))
    override fun hasNext() = !queue.isEmpty()
    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val result = queue.remove()
        children(result).forEach { queue.add(it) }
        return result
    }
}
private fun Component.walk(): Iterable<Component> = Iterable {
    TreeIterator(this, { component -> component.children.iterator() })
}
