package com.github.vok.karibudsl.example.form

import com.github.vok.karibudsl.PastDate
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A simple information about a person. This example shows also the validation annotations which are automatically picked
 * up by Vaadin's [BeanValidationBinder]. See the [FormView] for the UI code which edits instances of this class.
 * @property fullName the full name of the person, required
 * @property dateOfBirth the date of birth, must be in the past
 * @property maritalStatus marital status
 * @property alive if false, the person is deceased
 * @property comment optional comment
 */
data class Person(
        @field:NotNull
        @field:Size(min = 3, max = 200)
        var fullName: String? = null,

        @field:NotNull
        @field:PastDate
        var dateOfBirth: LocalDate? = null,

        @field:NotNull
        var maritalStatus: MaritalStatus? = null,

        @field:NotNull
        var alive: Boolean? = null,

        var comment: String? = null
) : Serializable {
    companion object {
        fun createRandom(): Person {
            val random = Random()
            return Person(fullName = "Random ${random.nextInt(100)}",
                    dateOfBirth = LocalDate.now().minusYears(random.nextInt(100).toLong()).plusDays(random.nextInt(365).toLong()),
                    maritalStatus = MaritalStatus.values().random,
                    alive = random.nextBoolean())
        }
    }
}

enum class MaritalStatus {
    Single,
    Married,
    Divorced,
    Widowed
}

val <T> Array<T>.random: T get() = get(Random().nextInt(size))
