package com.github.vok.karibudsl

import com.vaadin.data.Binder
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import org.junit.Before
import org.junit.Test
import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.test.expect

class BinderUtilsTest {
    @Before
    fun initVaadin() = MockVaadin.setup()

    @Test
    fun testReadBeanWithNullFields() {
        // https://github.com/vaadin/framework/issues/8664
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        binder.readBean(Person())
        expect("") { form.fullName.value }
    }

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

    @Test
    fun `test validation errors`() {
        val binder = beanValidationBinder<Person>()
        val form = Form(binder)
        form.fullName.value = ""
        val person = Person()
        expect(false) { binder.writeBeanIfValid(person) }
        expect(Person()) { person }  // make sure that no value has been written
    }
}

private class Form(binder: Binder<Person>): VerticalLayout() {
    val fullName: TextField
    init {
        fullName = textField("Full Name:") {
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
                  var fullName: String? = null,

                  @field:NotNull
                  @field:PastDate
                  var dateOfBirth: LocalDate? = null,

                  @field:NotNull
                  var alive: Boolean? = null,

                  var comment: String? = null
) : Serializable
