package com.github.karibu.testing

import com.github.vok.karibudsl.flow.walk
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasClickListeners
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.router.InternalServerError

/**
 * A criterion for matching components. The component must match all of non-null fields.
 * @property clazz the class of the component we are searching for.
 * @property id the required [Component.getId]; if null, no particular id is matched.
 * @property caption the required [Component.caption]; if null, no particular caption is matched.
 * @property text the [com.vaadin.flow.dom.Element.getText]
 * @property predicates the predicates the component needs to match, not null. May be empty - in such case it is ignored.
 */
class SearchSpec<T: Component>(val clazz: Class<T>,
                 var id: String? = null,
                 var caption: String? = null,
                 var text: String? = null,
                 var predicates: List<(Component)->Boolean> = listOf()) {

    override fun toString(): String {
        val list = mutableListOf<String>(if (clazz.simpleName.isBlank()) clazz.name else clazz.simpleName)
        if (id != null) list.add("id='$id'")
        if (caption != null) list.add("caption='$caption'")
        if (text != null) list.add("text='$text'")
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
    val spec = SearchSpec(clazz)
    spec.block()
    val result = find(spec.toPredicate()).filterIsInstance(clazz)
    if (result.isEmpty()) {
        throw IllegalArgumentException("No visible ${clazz.simpleName} in ${toPrettyString()} matching $spec. Component tree:\n${toPrettyTree()}")
    }
    if (result.size > 1) {
        throw IllegalArgumentException("Too many components in ${toPrettyString()} matching $spec: ${result.joinToString { it.toPrettyString() }}. Component tree:\n${toPrettyTree()}")
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
    return result.filterIsInstance(clazz)
}

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
    if (this is HasValue<*, *> && this.isReadOnly) {
        throw IllegalArgumentException("The button ${toPrettyString()} is read-only")
    }
    // click()  // can't call this since this calls JS method on the browser... but we're server-testing and there is no browser and this call would do nothing.
    _fireEvent(HasClickListeners.ClickEvent(this, false))
}

private fun Component.isEffectivelyVisible(): Boolean = isVisible && (!parent.isPresent || parent.get().isEffectivelyVisible())

private fun Component.find(predicate: (Component)->Boolean): List<Component> {
    val descendants = walk()
    val error: InternalServerError? = descendants.filterIsInstance<InternalServerError>().firstOrNull()
    if (error != null) throw AssertionError("An internal server error occurred; check log for the actual stack-trace. Error text: ${error.element.text}\n${UI.getCurrent().toPrettyTree()}")
    return descendants.filter { predicate(it) }
}

private fun <T: Component> Iterable<(T)->Boolean>.and(): (T)->Boolean = { component -> all { it(component) } }
