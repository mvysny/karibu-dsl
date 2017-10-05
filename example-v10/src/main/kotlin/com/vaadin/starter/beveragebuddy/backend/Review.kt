package com.vaadin.starter.beveragebuddy.backend

import java.io.Serializable
import java.time.LocalDate

/**
 * Represents a beverage review.
 */
// must be open - Flow requires it to create ModelProxy
open class Review(open var id: Long? = null, var score: Int = 1, var name: String = "", var date: LocalDate = LocalDate.now(),
                  var category: Category = Category.UNDEFINED, var count: Int = 1) : Serializable {
    override fun toString() = "Review(id=$id, score=$score, name='$name', date=$date, category=$category, count=$count)"

    fun copy() = Review(id, score, name, date, category.copy(), count)
}
