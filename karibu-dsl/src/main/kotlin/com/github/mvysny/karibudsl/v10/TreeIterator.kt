package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import java.util.*

/**
 * Given a tree root and a closure which computes children, iterates recursively over a tree of objects.
 * Iterator works breadth-first: initially the root itself is offered, then its children, then the child's children etc.
 * @property root the root object of the tree
 * @property children for given node, returns a list of its children.
 */
public class TreeIterator<out T>(private val root: T, private val children: (T) -> Iterator<T>) : Iterator<T> {
    /**
     * The items to iterate over. Gradually filled with children, until there are no more items to iterate over.
     */
    private val queue: Queue<T> = LinkedList<T>(listOf(root))

    override fun hasNext(): Boolean = !queue.isEmpty()

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val result = queue.remove()
        children(result).forEach { queue.add(it) }
        return result
    }
}

/**
 * Walks over this component and all descendants of this component, breadth-first.
 * @return iterable which iteratively walks over this component and all of its descendants.
 */
public fun (@VaadinDsl Component).walk(): Iterable<Component> = Iterable {
    TreeIterator(this) { component -> component.children.iterator() }
}
