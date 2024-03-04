import com.vaadin.gradle.getBooleanProperty

// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

plugins {
    alias(libs.plugins.vaadin)
}

dependencies {
    // Vaadin
    implementation(project(":karibu-dsl-v23"))
    implementation(libs.vaadin.core) {
        if (vaadin.effective.productionMode.get()) {
            exclude(module = "vaadin-dev")
        }
    }
    implementation(libs.vaadinboot)

    implementation(libs.slf4j.simple)

    testImplementation(libs.kaributesting)
    testImplementation(libs.dynatest)
}
