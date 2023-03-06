package com.vaadin.starter.beveragebuddy.backend

import java.io.Serializable
import java.time.LocalDate
import jakarta.validation.constraints.*

/**
 * Represents a beverage review.
 */
class Review : Serializable {
    var id: Long? = null

    @field:NotNull
    @field:Min(1)
    @field:Max(5)
    var score: Int = 1

    @field:NotBlank
    @field:Size(min = 3)
    var name: String = ""

    @field:NotNull
    @field:PastOrPresent
    var date: LocalDate = LocalDate.now()

    @field:NotNull
    var category: Category? = null

    @field:NotNull
    @field:Min(1)
    @field:Max(99)
    var count: Int = 1

    override fun toString() = "Review(id=$id, score=$score, name='$name', date=$date, category=$category, count=$count)"

    fun copy() = Review().also {
        it.id = id
        it.score = score
        it.name = name
        it.date = date
        it.category = category?.copy()
        it.count = count
    }
}
