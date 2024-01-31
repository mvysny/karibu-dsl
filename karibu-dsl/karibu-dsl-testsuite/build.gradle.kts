dependencies {
    api(project(":karibu-dsl"))

    api(libs.dynatest)
    api(libs.kaributesting)
    api(libs.slf4j.simple)

    // Vaadin
    // don't compile-depend on vaadin-core anymore: the app itself should manage Vaadin dependencies, for example
    // using the gradle-flow-plugin or direct dependency on vaadin-core.
    compileOnly(libs.vaadin.core)
    testImplementation(libs.vaadin.core)
}
