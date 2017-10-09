package com.github.vok.karibudsl.flow

import com.vaadin.data.Binder
import com.vaadin.ui.checkbox.Checkbox
import com.vaadin.ui.common.HasValue
import com.vaadin.ui.datepicker.DatePicker
import com.vaadin.ui.layout.VerticalLayout
import com.vaadin.ui.textfield.TextField
import org.junit.Before
import org.junit.Test
import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size
import kotlin.test.expect

class BinderUtilsTest {

    @Test
    fun testReadBeanWithNullFields() {
        // https://github.com/vaadin/framework/issues/8664
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        form.clear()
        binder.readBean(Person())
        expect("") { form.fullName.value }
        expect(null) { form.dateOfBirth.value }
        expect(false) { form.isAlive.value }
        expect("") { form.comment.value }
        expect("") { form.testDouble.value }
        expect("") { form.testInt.value }
        expect("") { form.testBD.value }
        expect("") { form.testBI.value }
        expect("") { form.testLong.value }
    }

    @Test
    fun testWriteBeanWithNullFields() {
        // https://github.com/vaadin/framework/issues/8664
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        form.clear()
        val person = Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, null, "some comment",
                25.5, 5, 555L, BigDecimal("77.11"), BigInteger("123"))
        binder.writeBean(person)
        expect(Person(testBoolean = false)) { person }
    }

    @Test
    fun testSimpleBindings() {
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        form.testDouble.value = "25.5"
        form.testInt.value = "5"
        form.testLong.value = "555"
        form.testBI.value = "123"
        form.testBD.value = "77.11"
        form.testBoolean.value = true
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        expect(Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, true, "some comment",
                25.5, 5, 555L, BigDecimal("77.11"), BigInteger("123"))) { person }
    }

    @Test
    fun testValidatingBindings() {
        val binder = beanValidationBinder<Person>()
        val form = Form(binder)
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        expect(Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, false, "some comment")) { person }
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
    val dateOfBirth: DatePicker
    val isAlive: Checkbox
    val testBoolean: Checkbox
    val comment: TextField  // @todo use TextArea once it's available
    val testDouble: TextField
    val testInt: TextField
    val testLong: TextField
    val testBD: TextField
    val testBI: TextField
    init {
        fullName = textField("Full Name:") {
            // binding to a BeanValidationBinder will also validate the value automatically.
            // please read https://vaadin.com/docs/-/part/framework/datamodel/datamodel-forms.html for more information
            bind(binder).trimmingConverter().bind(Person::fullName)
            value = "Zaphod Beeblebrox"
        }
        dateOfBirth = datePicker("Date of Birth:") {
            bind(binder).bind(Person::dateOfBirth)
            value = LocalDate.of(2010, 1, 25)
        }
        isAlive = checkBox("Is Alive") {
            bind(binder).bindN(Person::alive)
        }
        testBoolean = checkBox("Test Boolean:") {
            bind(binder).bindN(Person::testBoolean)
        }
        comment = textField("Comment:") {
            bind(binder).bind(Person::comment)
            value = "some comment"
        }
        testDouble = textField("Test Double:") {
            bind(binder).toDouble().bind(Person::testDouble)
        }
        testInt = textField("Test Int:") {
            bind(binder).toInt().bind(Person::testInt)
        }
        testLong = textField("Test Long:") {
            bind(binder).toLong().bind(Person::testLong)
        }
        testBD = textField("Test BigDecimal:") {
            bind(binder).toBigDecimal().bind(Person::testBD)
        }
        testBI = textField("Test BigInteger:") {
            bind(binder).toBigInteger().bind(Person::testBI)
        }
    }

    fun clear() {
        listOf<HasValue<*, *>>(fullName, comment, testDouble, testInt, testLong, testBD, testBI).forEach { it.value = "" }
        dateOfBirth.value = null
        isAlive.value = false
    }
}

data class Person(@field:NotNull
                  @field:Size(min = 3, max = 200)
                  var fullName: String? = null,

                  @field:NotNull
                  @field:PastOrPresent
                  var dateOfBirth: LocalDate? = null,

                  @field:NotNull
                  var alive: Boolean = false,

                  var testBoolean: Boolean? = null,

                  var comment: String? = null,

                  var testDouble: Double? = null,

                  var testInt: Int? = null,

                  var testLong: Long? = null,

                  var testBD: BigDecimal? = null,

                  var testBI: BigInteger? = null
) : Serializable