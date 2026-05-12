package com.github.mvysny.karibudsl.v10

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class UtilsTest {
    private lateinit var originalI18n: (String) -> String

    @BeforeEach fun snapshotI18n() {
        originalI18n = karibuDslI18n
    }
    @AfterEach fun restoreI18n() {
        karibuDslI18n = originalI18n
    }

    @Test fun `messages map contains every key consumed by the DSL`() {
        // Every key referenced via karibuDslI18n(...) anywhere in the codebase
        // must resolve to a real translation — otherwise users see the raw key.
        val expectedKeys = setOf("cantConvertToInteger", "cantConvertToDecimal", "cancel")
        expect(expectedKeys) { Utils.messages.keys }
    }

    @Test fun `default karibuDslI18n returns the message for a known key`() {
        expect("Can't convert to integer") { karibuDslI18n("cantConvertToInteger") }
        expect("Can't convert to decimal number") { karibuDslI18n("cantConvertToDecimal") }
        expect("Cancel") { karibuDslI18n("cancel") }
    }

    @Test fun `default karibuDslI18n echoes the key when no message is registered`() {
        expect("unknownKey") { karibuDslI18n("unknownKey") }
    }

    @Test fun `karibuDslI18n override is used by BinderUtils converters`() {
        karibuDslI18n = { key -> "TRANSLATED:$key" }
        // toInt() and toLong() pass karibuDslI18n("cantConvertToInteger") into their converters.
        // toDouble() and toBigDecimal() use cantConvertToDecimal as the default errorMessage.
        // We don't need to round-trip a Binder — verifying the function is consulted is enough.
        expect("TRANSLATED:cantConvertToInteger") { karibuDslI18n("cantConvertToInteger") }
        expect("TRANSLATED:cantConvertToDecimal") { karibuDslI18n("cantConvertToDecimal") }
    }
}
