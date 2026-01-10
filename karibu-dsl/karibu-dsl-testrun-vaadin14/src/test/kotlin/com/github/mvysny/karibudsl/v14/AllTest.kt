package com.github.mvysny.karibudsl.v14

import com.github.mvysny.karibudsl.v10.AllTests
import com.github.mvysny.kaributools.VaadinVersion
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

class AllTest {
    @Test fun vaadinVersion() {
        expect(25) { VaadinVersion.get.major }
    }

    @Nested inner class All : AllTests()
}