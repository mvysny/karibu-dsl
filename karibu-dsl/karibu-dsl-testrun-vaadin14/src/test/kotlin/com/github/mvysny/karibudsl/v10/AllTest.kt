package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.VaadinMeta
import java.io.File
import java.util.*
import kotlin.test.expect

class AllTest : DynaTest({
    test("vaadin version") {
        expect(14) { VaadinMeta.version }
    }

    test("vaadin version 2") {
        val gradleProps: Properties = File("../../gradle.properties").loadAsProperties()
        val expectedVaadinVersion: String = gradleProps["vaadin14_version"] as String
        expect(expectedVaadinVersion) { vaadinVersion }
    }

    allTests()
})
