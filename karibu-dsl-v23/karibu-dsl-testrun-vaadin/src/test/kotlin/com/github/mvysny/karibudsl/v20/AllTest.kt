package com.github.mvysny.karibudsl.v20

import com.github.mvysny.karibudsl.v10.AllTests
import com.github.mvysny.kaributools.VaadinVersion
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

class AllTest {
    @Test fun vaadinVersion() {
        expect(24) { VaadinVersion.get.major }
        expect(5) { VaadinVersion.get.minor }
    }

    @Nested inner class AllTest : AllTests()
    @Nested inner class AllTest24 : AllTests24()
}
