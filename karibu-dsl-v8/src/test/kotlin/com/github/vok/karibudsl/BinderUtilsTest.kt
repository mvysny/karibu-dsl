package com.github.vok.karibudsl

import com.vaadin.data.Binder
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import org.junit.Before
import org.junit.Test
import java.io.Serializable
import java.time.LocalDate
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.reflect.KClass
import kotlin.test.expect

class BinderUtilsTest {
    @Before
    fun initVaadin() = MockVaadin.setup()

    @Test
    fun testSimpleBindings() {
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        expect(Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, "some comment")) { person }
    }

    @Test
    fun testValidatingBindings() {
        val binder = beanValidationBinder<Person>()
        val form = Form(binder)
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        expect(Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, "some comment")) { person }
    }
}

private class Form(binder: Binder<Person>): VerticalLayout() {
    init {
        textField("Full Name:") {
            // binding to a BeanValidationBinder will also validate the value automatically.
            // please read https://vaadin.com/docs/-/part/framework/datamodel/datamodel-forms.html for more information
            bind(binder).trimmingConverter().bind(Person::fullName)
            value = "Zaphod Beeblebrox"
        }
        dateField("Date of Birth:") {
            bind(binder).bind(Person::dateOfBirth)
            value = LocalDate.of(2010, 1, 25)
        }
        checkBox("Is Alive") {
            bind(binder).bind(Person::alive)
            value = false
        }
        textArea("Comment:") {
            bind(binder).bind(Person::comment)
            value = "some comment"
        }
    }
}

data class Person(@field:NotNull
                  @field:Size(min = 3, max = 200)
                  val fullName: String? = null,

                  @field:NotNull
                  @field:PastDate
                  val dateOfBirth: LocalDate? = null,

                  @field:NotNull
                  var alive: Boolean? = null,

                  var comment: String? = null
) : Serializable

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
