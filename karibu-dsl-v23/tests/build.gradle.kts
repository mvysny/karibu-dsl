dependencies {
    api(project(":karibu-dsl:karibu-dsl-testsuite"))
    api("com.github.mvysny.kaributesting:karibu-testing-v24:${properties["kaributesting_version"]}")
    api(project(":karibu-dsl-v23"))

    // Vaadin
    compileOnly("com.vaadin:vaadin-core:${properties["vaadin_version"]}")
}
