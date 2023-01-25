// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

plugins {
    id("com.vaadin")
}

dependencies {
    // Vaadin
    implementation(project(":karibu-dsl-v23"))
    implementation("com.vaadin:vaadin-core:${properties["vaadin23_version"]}")
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:10.3")

    implementation("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")

    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v23:${properties["kaributesting_version"]}")
    testImplementation("com.github.mvysny.dynatest:dynatest:${properties["dynatest_version"]}")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}
