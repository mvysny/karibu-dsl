dependencies {
    api(project(":karibu-dsl:karibu-dsl-testsuite"))
    api(libs.kaributesting)
    api(project(":karibu-dsl-v23"))

    // Vaadin
    compileOnly(libs.vaadinnext.core)
}
