dependencies {
    testImplementation(project(":karibu-dsl:karibu-dsl-testsuite"))

    // Vaadin
    testImplementation(libs.vaadin.core)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
