package com.vaadin.starter.beveragebuddy.backend

import java.io.Serializable

/**
 * Represents a beverage category.
 * @property id
 * @property name the category name
 */
// must be open - Flow requires that to do some ModelProxy class enhancement dark magic
open class Category(open var id: Long? = null, open var name: String = "") : Serializable {

    companion object {
        val UNDEFINED = Category(name = "(undefined)")
    }

    override fun toString() = "Category(id=$id, name='$name')"


    fun copy() = Category(id, name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Category
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}
