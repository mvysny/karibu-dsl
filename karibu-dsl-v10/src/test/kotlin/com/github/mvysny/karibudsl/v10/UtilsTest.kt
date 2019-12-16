package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
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