package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentUtil

public object Utils {
    public val messages: Map<String, String> = mapOf(
        "cantConvertToInteger" to "Can't convert to integer",
        "cantConvertToDecimal" to "Can't convert to decimal number",
        "cancel" to "Cancel",
    )
}

/**
 * Change this function to provide a proper i18n for your apps. For a list of all keys used by Karibu-DSL see [messages].
 */
public var karibuDslI18n: (key: String) -> String = { key: String ->
    Utils.messages[key] ?: key
}

/**
 * Retrieves data stored via [ComponentUtil.setData]/[ComponentUtil.getData], creating it if it doesn't exist.
 */
internal fun <T> Component.data(key: String, whenMissing: () -> T): T {
    @Suppress("UNCHECKED_CAST") var value: T? = ComponentUtil.getData(this, key) as T?
    if (value == null) {
        value = whenMissing()
        ComponentUtil.setData(this, key, value)
    }
    return value!!
}
