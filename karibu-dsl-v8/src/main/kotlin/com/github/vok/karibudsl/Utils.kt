package com.github.vok.karibudsl

import com.vaadin.server.ErrorMessage
import org.jsoup.Jsoup
import java.net.URI
import java.util.*

fun Iterable<String?>.filterNotBlank(): List<String> = filterNotNull().filter { it.isNotBlank() }

/**
 * Parses the [URI.query] string and returns a map of all query parameters. Since a key may be present multiple times in the query,
 * the map maps key to a list of values. The order of the values follows the order in which the values were present in the query.
 *
 * To retrieve the query map from Vaadin, just use `Page.getCurrent().location.queryMap`.
 */
val URI.queryMap: Map<String, List<String>> get() {
    val keyValueList: List<Pair<String, String>> = (query ?: "")
        .split('&')
        .filterNotBlank()
        .map { it.trim().split('=', limit = 2) }
        .filter { it.size == 2 }
        .map { it[0] to it[1] }

    val result = mutableMapOf<String, MutableList<String>>()
    for (pair in keyValueList) {
        result.computeIfAbsent(pair.first, { LinkedList() }) .add(pair.second)
    }
    return result
}

/**
 * Unescapes [ErrorMessage.getFormattedHtmlMessage] and converts it to sane string. E.g.
 * `The&#32;user&#32;does&#32;not&#32;exist` is converted to `The user does not exist`.
 */
val ErrorMessage.message: String get() = Jsoup.parse(formattedHtmlMessage).text()
