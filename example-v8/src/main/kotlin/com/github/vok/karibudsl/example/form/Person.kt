package com.github.vok.karibudsl.example.form

import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.*

/**
 * A simple information about a person. This example shows also the validation annotations which are automatically picked
 * up by Vaadin's [BeanValidationBinder]. See the @todo form for details.
 */
data class Person(
        @field:NotNull
        val id: Long? = null,

        @field:NotNull
        @field:Size(min = 3, max = 200)
        val fullName: String? = null,

        @field:NotNull
        @field:Past
        val dateOfBirth: LocalDate? = null
) : Serializable
