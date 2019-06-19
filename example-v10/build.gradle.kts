// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

plugins {
    id("org.gretty")
    war
    id("com.devsoap.vaadin-flow") version "1.2"
}

vaadin {
    version = properties["vaadin10_version"] as String
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

dependencies {
    compile(project(":karibu-dsl-v10"))
    compile(vaadin.bom())
    compile(vaadin.core())
    compile(vaadin.lumoTheme())
    compileOnly(vaadin.servletApi())
    runtime(vaadin.slf4j())

    testCompile("com.github.mvysny.kaributesting:karibu-testing-v10:${properties["kaributesting_version"]}")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")
    testCompile("org.jetbrains.kotlin:kotlin-test")
}
