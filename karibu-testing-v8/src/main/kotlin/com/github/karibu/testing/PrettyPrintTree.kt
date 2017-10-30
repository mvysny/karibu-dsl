package com.github.karibu.testing

import com.vaadin.data.HasValue
import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents

import java.util.ArrayList

class PrettyPrintTree(val name: String, val children: MutableList<PrettyPrintTree>) {

    fun print(): String {
        val sb = StringBuilder()
        print(sb, "", true)
        return sb.toString()
    }

    private fun print(sb: StringBuilder, prefix: String, isTail: Boolean) {
        sb.append(prefix + (if (isTail) "└── " else "├── ") + name + "\n")
        for (i in 0 until children.size - 1) {
            children[i].print(sb, prefix + if (isTail) "    " else "│   ", false)
        }
        if (children.size > 0) {
            children[children.size - 1]
                    .print(sb, prefix + if (isTail) "    " else "│   ", true)
        }
    }

    companion object {

        fun ofVaadin(root: Component): PrettyPrintTree {
            val result = PrettyPrintTree(root.toPrettyString(), mutableListOf())
            if (root is HasComponents) {
                for (child in root) {
                    result.children.add(ofVaadin(child))
                }
            }
            return result
        }
    }
}

fun Component.toPrettyTree(): String = PrettyPrintTree.ofVaadin(this).print()

fun Component.toPrettyString(): String {
    val list = ArrayList<String>()
    if (id != null) {
        list.add("#$id")
    }
    if (!isVisible) {
        list.add("INVIS")
    }
    if (this is HasValue<*> && this.isReadOnly) {
        list.add("RO")
    }
    if (!isEnabled) {
        list.add("DISABLED")
    }
    if (!styleName.isNullOrBlank()) {
        list.add(this.styleName.split("\\s+".toRegex())
                .map { it.trim() }
                .filter { it.isNotBlank() }.joinToString(" ") { ".$it" }
        )
    }
    if (caption != null) {
        list.add("caption='$caption'")
    }
    if (this is HasValue<*>) {
        list.add("value='${this.value}'")
    }
    var name = javaClass.simpleName
    if (name.isEmpty()) {
        // anonymous classes
        name = javaClass.name
    }
    return name + list
}
