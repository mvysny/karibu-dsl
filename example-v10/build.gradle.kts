// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

plugins {
    id("org.gretty")
    war
    id("com.vaadin")
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

dependencies {
    compile(project(":karibu-dsl-v10"))
    // Vaadin 14
    compile("com.vaadin:vaadin-core:${properties["vaadin10_version"]}") {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
                "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
                "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
                .forEach { exclude(group = it) }
    }
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")
    compile("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")

    testCompile("com.github.mvysny.kaributesting:karibu-testing-v10:${properties["kaributesting_version"]}")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")
    testCompile("org.jetbrains.kotlin:kotlin-test")
}
