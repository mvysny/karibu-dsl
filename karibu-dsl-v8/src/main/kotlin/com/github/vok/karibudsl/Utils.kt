package com.github.vok.karibudsl

fun Iterable<String?>.filterNotBlank(): List<String> = filterNotNull().filter { it.isNotBlank() }
