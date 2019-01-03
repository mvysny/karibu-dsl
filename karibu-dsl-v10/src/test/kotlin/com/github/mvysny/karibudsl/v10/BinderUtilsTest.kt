package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size
import kotlin.test.expect

class BinderUtilsTest : DynaTest({

    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("ReadBeanWithNullFields") {
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

    test("WriteBeanWithNullFields") {
        // https://github.com/vaadin/framework/issues/8664
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        form.clear()
        val person = Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, null, "some comment",
                25.5, 5, 555L, BigDecimal("77.11"), BigInteger("123"))
        binder.writeBean(person)
        expect(Person(testBoolean = false, comment = "")) { person }
    }

    test("SimpleBindings") {
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        form.testDouble.value = "25.5"
        form.testInt.value = "5"
        form.testLong.value = "555"
        form.testBI.value = "123"
        form.testBD.value = "77.11"
        form.testBoolean.value = true
        form.testInstant.value = LocalDate.of(2015, 1, 25)
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        val instant = LocalDate.of(2015, 1, 25).atStartOfDay(ZoneId.of("UTC")).toInstant()
        expect(Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, true, "some comment",
                25.5, 5, 555L, BigDecimal("77.11"), BigInteger("123"), instant)) { person }
    }

    test("ValidatingBindings") {
        val binder = beanValidationBinder<Person>()
        Form(binder)
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        expect(Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, false, "some comment")) { person }
    }

    test("test validation errors") {
        val binder = beanValidationBinder<Person>()
        val form = Form(binder)
        form.fullName.value = ""
        val person = Person()
        expect(false) { binder.writeBeanIfValid(person) }
        expect(Person()) { person }  // make sure that no value has been written
    }

    test("test that bind() supports both non-nullable and nullable properties") {
        data class TestingPerson(var foo: String?, var baz: String)
        val binder = beanValidationBinder<TestingPerson>()
        TextField().bind(binder).bind(TestingPerson::foo)
        TextField().bind(binder).bind(TestingPerson::baz)
    }
})

private class Form(binder: Binder<Person>): VerticalLayout() {
    val fullName: TextField
    val dateOfBirth: DatePicker
    val isAlive: Checkbox
    val testBoolean: Checkbox
    val comment: TextArea
    val testDouble: TextField
    val testInt: TextField
    val testLong: TextField
    val testBD: TextField
    val testBI: TextField
    val testInstant: DatePicker
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
            bind(binder).bind(Person::alive)
        }
        testBoolean = checkBox("Test Boolean:") {
            bind(binder).bind(Person::testBoolean)
        }
        comment = textArea("Comment:") {
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
        testInstant = datePicker("Created:") {
            bind(binder).toInstant().bind(Person::created)
        }
    }

    fun clear() {
        listOf<HasValue<AbstractField.ComponentValueChangeEvent<TextField, String>, String>>(fullName, testDouble, testInt, testLong, testBD, testBI).forEach { it.value = "" }
        comment.value = ""
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

                  var testBI: BigInteger? = null,

                  var created: Instant? = null
) : Serializable
