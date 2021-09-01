package com.github.mvysny.karibudsl.v10

public object Utils {
    public val messages: Map<String, String> = mapOf("cantConvertToInteger" to "Can't convert to integer",
            "cantConvertToDecimal" to "Can't convert to decimal number",
            "from" to "From (inclusive):",
            "to" to "To (inclusive):",
            "set" to "Set",
            "clear" to "Clear",
            "all" to "All"
    )
}

/**
 * Change this function to provide a proper i18n for your apps. For a list of all keys used by Karibu-DSL see [messages].
 */
public var karibuDslI18n: (key: String) -> String = { key: String ->
    Utils.messages[key] ?: key
}
