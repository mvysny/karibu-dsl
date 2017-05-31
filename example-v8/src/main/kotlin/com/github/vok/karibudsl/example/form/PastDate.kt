package com.github.vok.karibudsl.example.form

import java.time.LocalDate
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = arrayOf(PastDateValidator::class))
@MustBeDocumented
annotation class PastDate(val message: String = "Must be in the past", val groups: Array<KClass<*>> = arrayOf(), val payload: Array<KClass<out Payload>> = arrayOf())
class PastDateValidator : ConstraintValidator<PastDate, LocalDate?> {

    override fun initialize(constraintAnnotation: PastDate) {}

    override fun isValid(`object`: LocalDate?, constraintContext: ConstraintValidatorContext): Boolean {
        if (`object` == null) {
            return true
        }
        return `object` < LocalDate.now()
    }
}