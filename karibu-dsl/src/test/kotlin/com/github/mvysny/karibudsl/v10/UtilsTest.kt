package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size
import kotlin.test.expect

class UtilsTest : DynaTest({
    test("getter") {
        expect("getFullName") { Person::class.java.getGetter("fullName").name }
        expect("getAlive") { Person::class.java.getGetter("alive").name }
        expectThrows(IllegalArgumentException::class, "No such field 'foo' in class com.github.mvysny.karibudsl.v10.Person; available properties: alive, class, comment, created, dateOfBirth, fullName, testBD, testBI, testBoolean, testDouble, testInt, testLong") {
            Person::class.java.getGetter("foo")
        }
    }
})

private data class Person(@field:NotNull
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
