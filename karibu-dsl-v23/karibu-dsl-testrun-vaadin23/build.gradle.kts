dependencies {
    testImplementation(project(":karibu-dsl:karibu-dsl-testsuite"))
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v24:${properties["kaributesting_version"]}")
    testImplementation(project(":karibu-dsl-v23"))

    // Vaadin
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin_version"]}")
}
