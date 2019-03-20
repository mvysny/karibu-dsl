package com.github.mvysny.karibudsl.v10

fun String.containsWhitespace(): Boolean = any { it.isWhitespace() }
