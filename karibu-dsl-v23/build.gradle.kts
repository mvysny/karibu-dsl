dependencies {
    api(project(":karibu-dsl"))
    api("com.github.mvysny.karibu-tools:karibu-tools-23:${properties["karibu_tools_version"]}")

    testImplementation("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")
    testImplementation("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")

    // Vaadin
    // don't compile-depend on vaadin-core anymore: the app itself should manage Vaadin dependencies, for example
    // using the gradle-flow-plugin or direct dependency on vaadin-core. The reason is that the app may wish to use the
    // npm mode and exclude all webjars.
    compileOnly("com.vaadin:vaadin-core:${properties["vaadin_version_next"]}")
}

kotlin {
    explicitApi()
}

val configureMavenCentral = ext["configureMavenCentral"] as (artifactId: String) -> Unit
configureMavenCentral("karibu-dsl-v23")
