package com.github.mvysny.karibudsl.v10

fun String.containsWhitespace(): Boolean = any { it.isWhitespace() }

private val messages = mapOf("CANT_CONVERT_TO_INTEGER" to "Can't convert to integer",
        "CANT_CONVERT_TO_DECIMAL" to "Can't convert to decimal number",
        "CANT_CONVERT_TO_INTEGER" to "Can't convert to integer",
        "FROM" to "From:",
        "TO" to "To:",
        "SET" to "Set",
        "CLEAR" to "Clear"
)
/**
 * Change this function to provide a proper i18n for your apps. For a list of all keys used by Karibu-DSL see [messages].
 */
var karibuDslI18n: (key: String) -> String = { key -> messages[key] ?: key }
