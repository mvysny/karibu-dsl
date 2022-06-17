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
    implementation(project(":karibu-dsl"))
    // Vaadin
    implementation("com.vaadin:vaadin-core:${properties["vaadin20_version"]}")
    providedCompile("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")

    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v10:${properties["kaributesting_version"]}")
    testImplementation("com.github.mvysny.dynatest:dynatest:${properties["dynatest_version"]}")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}
