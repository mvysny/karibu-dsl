package com.github.karibu.testing

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasText
import com.vaadin.flow.component.HasValue
import java.util.ArrayList
import kotlin.streams.toList

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
            for (child in root.children) {
                result.children.add(ofVaadin(child))
            }
            return result
        }
    }
}

fun Component.toPrettyTree(): String = PrettyPrintTree.ofVaadin(this).print()

fun Component.toPrettyString(): String {
    val list = ArrayList<String>()
    if (id.isPresent) {
        list.add("#${id.get()}")
    }
    if (!isVisible) {
        list.add("INVIS")
    }
    if (this is HasValue<*, *> && this.isReadOnly) {
        list.add("RO")
    }
    if (element.style.names.toList().isNotEmpty()) {
        list.add(element.style.names.toList().joinToString(" ") { "$it:${element.style[it]}" })
    }
    if (label.isNotBlank()) {
        list.add("label='$label'")
    }
    if (this is HasText && !text.isNullOrBlank()) {
        list.add("text='$text'")
    }
    if (this is HasValue<*, *>) {
        list.add("value='${this.value}'")
    }
    var name = javaClass.simpleName
    if (name.isEmpty()) {
        // anonymous classes
        name = javaClass.name
    }
    return name + list
}
