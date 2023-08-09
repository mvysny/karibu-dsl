// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

plugins {
    id("com.vaadin")
}

dependencies {
    // Vaadin
    implementation(project(":karibu-dsl-v23"))
    implementation("com.vaadin:vaadin-core:${properties["vaadin_version"]}") {
        afterEvaluate {
            if (vaadin.productionMode) {
                exclude(module = "vaadin-dev")
            }
        }
    }
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:12.0")

    implementation("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")

    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v23:${properties["kaributesting_version"]}")
    testImplementation("com.github.mvysny.dynatest:dynatest:${properties["dynatest_version"]}")
}
