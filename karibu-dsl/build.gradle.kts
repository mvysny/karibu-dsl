dependencies {
    api(kotlin("stdlib-jdk8"))
    api(libs.kaributools)

    testImplementation(libs.junit)
    testImplementation(libs.slf4j.simple)

    // Vaadin
    // don't compile-depend on vaadin-core anymore: the app itself should manage Vaadin dependencies, for example
    // using the gradle-flow-plugin or direct dependency on vaadin-core. The reason is that the app may wish to use the
    // npm mode and exclude all webjars.
    compileOnly(libs.vaadin.core)
    testImplementation(libs.vaadin.core)

    // IDEA language injections
    api(libs.jetbrains.annotations)

    // always include support for bean validation
    api(libs.hibernate.validator)
    // EL is required: http://hibernate.org/validator/documentation/getting-started/
    implementation(libs.jakarta.el)

    // always include support for css
    api(libs.kotlin.css.jvm)

}

kotlin {
    explicitApi()
}

val configureMavenCentral = ext["configureMavenCentral"] as (artifactId: String) -> Unit
configureMavenCentral("karibu-dsl")
