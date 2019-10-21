// The Vaadin Framework uitest module, Valo theme test part. Ported to Kotlin.
// https://github.com/vaadin/framework/tree/master/uitest/src/main/java/com/vaadin/tests/themes/valo

plugins {
    war
    id("org.gretty")
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

val staging by configurations.creating

dependencies {
    compile(project(":karibu-dsl-v8compat7"))

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    compile("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")
    compile("org.slf4j:slf4j-api:${properties["slf4j_version"]}")
    // this will allow us to configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:${properties["slf4j_version"]}")

    compile("com.vaadin:vaadin-compatibility-client-compiled:${properties["vaadin8_version"]}")
    compile("com.vaadin:vaadin-themes:${properties["vaadin8_version"]}")

    testCompile("com.github.mvysny.kaributesting:karibu-testing-v8:${properties["kaributesting_version"]}")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")
}
