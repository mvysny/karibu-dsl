package com.github.mvysny.karibudsl.v8

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.server.ErrorMessage
import com.vaadin.server.UserError
import java.net.URI
import kotlin.test.expect

class UtilsTest : DynaTest({
    group("queryMap") {
        test("null query") {
            val uri = URI("http://localhost")
            expect(null) { uri.query }
            expect(mapOf()) { uri.queryMap }
        }
        test("malformed query") {
            val uri = URI("http://localhost?param1")
            expect(mapOf()) { uri.queryMap }
        }
        test("query with one parameter") {
            val uri = URI("http://localhost?param1=value1")
            expect(mapOf("param1" to listOf("value1"))) { uri.queryMap }
        }
        test("query with three distinct parameters") {
            val uri = URI("http://localhost?param1=value1&lang=en&offset=25")
            expect(mapOf("param1" to listOf("value1"), "lang" to listOf("en"), "offset" to listOf("25"))) { uri.queryMap }
        }
        test("query with one repeated parameter") {
            val uri = URI("http://localhost?q=value1&q=en&q=25")
            expect(mapOf("q" to listOf("value1", "en", "25"))) { uri.queryMap }
        }
    }

    test("error message") {
        expect("foo bar baz") { (UserError("foo bar baz") as ErrorMessage).message }
    }
})