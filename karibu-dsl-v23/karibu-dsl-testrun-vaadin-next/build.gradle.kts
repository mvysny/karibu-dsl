dependencies {
    testImplementation(project(":karibu-dsl-v23:tests"))

    // Vaadin
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin_version_next"]}")
}
