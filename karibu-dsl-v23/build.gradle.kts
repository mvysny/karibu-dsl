dependencies {
    api(project(":karibu-dsl"))
    api(libs.kaributools)

    testImplementation(libs.dynatest)
    testImplementation(libs.slf4j.simple)

    // Vaadin
    // don't compile-depend on vaadin-core anymore: the app itself should manage Vaadin dependencies, for example
    // using the gradle-flow-plugin or direct dependency on vaadin-core. The reason is that the app may wish to use the
    // npm mode and exclude all webjars.
    compileOnly(libs.vaadinnext.core)
}

kotlin {
    explicitApi()
}

val configureMavenCentral = ext["configureMavenCentral"] as (artifactId: String) -> Unit
configureMavenCentral("karibu-dsl-v23")
