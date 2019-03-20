package com.github.mvysny.karibudsl.v10

fun String.containsWhitespace(): Boolean = any { it.isWhitespace() }

private val messages = mapOf("cantConvertToInteger" to "Can't convert to integer",
        "cantConvertToDecimal" to "Can't convert to decimal number",
        "from" to "From:",
        "to" to "To:",
        "set" to "Set",
        "clear" to "Clear"
)
/**
 * Change this function to provide a proper i18n for your apps. For a list of all keys used by Karibu-DSL see [messages].
 */
var karibuDslI18n: (key: String) -> String = { key -> messages[key] ?: key }
