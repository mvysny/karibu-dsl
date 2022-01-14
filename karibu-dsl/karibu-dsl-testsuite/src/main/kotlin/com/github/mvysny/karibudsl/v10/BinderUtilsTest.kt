package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._value
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.ChronoZonedDateTime
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.binderUtilsTest() {

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
        expect(null) { form.testInstant.value }
        expect(null) { form.testCalendar.value }
    }

    test("WriteBeanWithNullFields") {
        // https://github.com/vaadin/framework/issues/8664
        val binder = Binder<Person>(Person::class.java)
        val form = Form(binder)
        form.clear()
        val person = Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, null, "some comment",
                25.5, 5, 555L, BigDecimal("77.11"), BigInteger("123"))
        binder.writeBean(person)
        expect(Person(testBoolean = false)) { person }
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
        form.testCalendar.value = LocalDateTime.of(2015, 1, 25, 6, 10, 20)
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        val instant = LocalDate.of(2015, 1, 25).atStartOfDay(ZoneId.of("UTC")).toInstant()
        val cal = LocalDateTime.of(2015, 1, 25, 6, 10, 20).atZone(ZoneId.of("UTC")).toCalendar()
        expect(Person("Zaphod Beeblebrox", LocalDate.of(2010, 1, 25), false, true, "some comment",
                25.5, 5, 555L, BigDecimal("77.11"), BigInteger("123"), instant, cal)) { person }
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

    test("SimpleBindings with NumberField: write") {
        val binder = Binder<Person>(Person::class.java)
        val form = Form2(binder)
        form.testInt.value = 5.0
        form.testLong.value = 555.0
        form.testBI.value = 123.0
        form.testBD.value = 77.11
        val person = Person()
        expect(true) { binder.writeBeanIfValid(person) }
        expect(Person(testInt = 5, testLong = 555L, testBD = BigDecimal("77.11"), testBI = BigInteger("123"))) { person }
    }

    test("SimpleBindings with NumberField: load") {
        val binder = Binder<Person>(Person::class.java)
        val form = Form2(binder)
        binder.readBean(Person(testInt = 5, testLong = 555L, testBD = BigDecimal("77.11"), testBI = BigInteger("123")))
        expect(5.0) { form.testInt.value }
        expect(555.0) { form.testLong.value }
        expect(123.0) { form.testBI.value }
        expect(77.11) { form.testBD.value }
    }

    test("intToLong") {
        val binder = Binder<Person>(Person::class.java)
        val field = UI.getCurrent().integerField("Test Long") {
            bind(binder).toLong().bind("testLong")
        }
        binder.bean = Person()
        expect(null) { field._value }
        field._value = 25
        expect(25L) { binder.bean.testLong }
        binder.readBean(Person(testLong = 555L))
        expect(555) { field._value }
    }

    test("guessIsReadOnly") {
        val binder = Binder<Person>(Person::class.java)
        expect(false) { binder.guessIsReadOnly }
        Form2(binder)
        expect(false) { binder.guessIsReadOnly }
        binder.setReadOnly(true)
        expect(true) { binder.guessIsReadOnly }
        binder.setReadOnly(false)
        expect(false) { binder.guessIsReadOnly }
    }
}

private class Form2(binder: Binder<Person>): VerticalLayout() {
    val testInt: NumberField
    val testLong: NumberField
    val testBD: NumberField
    val testBI: NumberField
    init {
        testInt = numberField("Test Int:") {
            bind(binder).toInt().bind(Person::testInt)
        }
        testLong = numberField("Test Long:") {
            bind(binder).toLong().bind(Person::testLong)
        }
        testBD = numberField("Test BigDecimal:") {
            bind(binder).toBigDecimal().bind(Person::testBD)
        }
        testBI = numberField("Test BigInteger:") {
            bind(binder).toBigInteger().bind(Person::testBI)
        }
    }

    fun clear() {
        listOf(testInt, testLong, testBD, testBI).forEach { it.value = null }
    }
}

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
    val testCalendar: DateTimePicker
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
        testCalendar = dateTimePicker("Test Calendar:") {
            bind(binder).toCalendar().bind(Person::testCalendar)
        }
    }

    fun clear() {
        listOf<HasValue<AbstractField.ComponentValueChangeEvent<TextField, String>, String>>(fullName, testDouble, testInt, testLong, testBD, testBI).forEach { it.value = "" }
        comment.value = ""
        dateOfBirth.value = null
        isAlive.value = false
        testInstant.value = null
        testCalendar.value = null
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

                  var created: Instant? = null,

                  var testCalendar: Calendar? = null
) : Serializable

fun Instant.toDate(): Date = Date.from(this)

fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun Instant.toCalendar() = toDate().toCalendar()

fun ChronoZonedDateTime<*>.toCalendar() = toInstant().toCalendar()
