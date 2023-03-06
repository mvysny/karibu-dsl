dependencies {
    api(kotlin("stdlib-jdk8"))
    api("com.github.mvysny.karibu-tools:karibu-tools:${properties["karibu_tools_version"]}")

    testImplementation("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")
    testImplementation("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")

    // Vaadin
    // don't compile-depend on vaadin-core anymore: the app itself should manage Vaadin dependencies, for example
    // using the gradle-flow-plugin or direct dependency on vaadin-core. The reason is that the app may wish to use the
    // npm mode and exclude all webjars.
    compileOnly("com.vaadin:vaadin-core:${properties["vaadin14_version"]}")
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin14_version"]}")

    // IDEA language injections
    api("org.jetbrains:annotations:23.1.0")

    // always include support for bean validation
    implementation("org.hibernate.validator:hibernate-validator:${properties["hibernate_validator_version"]}")
    // EL is required: http://hibernate.org/validator/documentation/getting-started/
    implementation("org.glassfish:jakarta.el:4.0.2")
}

kotlin {
    explicitApi()
}

val configureBintray = ext["configureBintray"] as (artifactId: String) -> Unit
configureBintray("karibu-dsl")
