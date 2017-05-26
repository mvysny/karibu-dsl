package com.github.vok.karibudsl

import org.junit.Test
import kotlin.test.expect

/**
 * @author mavi
 */
class AutoViewProviderTest {
    @Test
    fun testParseViewName() {
        expect("foo") { AutoViewProvider.Companion.parseViewName("!foo/25") }
        expect("foo") { AutoViewProvider.Companion.parseViewName("foo") }
        expect("foo") { AutoViewProvider.Companion.parseViewName("foo/") }
    }
}