dependencies {
    testImplementation(project(":karibu-dsl-v23:tests"))

    // Vaadin
    testImplementation(libs.vaadin.core)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
