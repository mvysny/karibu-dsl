dependencies {
    testImplementation(project(":karibu-dsl:karibu-dsl-testsuite"))
    testImplementation(project(":karibu-dsl-v23"))

    // Vaadin
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin23_version"]}")
}
