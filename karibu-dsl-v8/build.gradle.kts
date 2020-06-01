dependencies {
    api(kotlin("stdlib-jdk8"))
    implementation("org.slf4j:slf4j-api:${properties["slf4j_version"]}")

    testImplementation("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v8:${properties["kaributesting_version"]}")
    testImplementation("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")

    // Vaadin
    api("com.vaadin:vaadin-server:${properties["vaadin8_version"]}")
    implementation("com.vaadin:vaadin-push:${properties["vaadin8_version"]}") // to include atmosphere and have support for class auto-discovery
    compileOnly("javax.servlet:javax.servlet-api:3.1.0")

    // IDEA language injections
    api("com.intellij:annotations:12.0")

    // always include support for bean validation
    api("javax.validation:validation-api:2.0.1.Final")  // so that the BeanFieldGroup will perform JSR303 validations
    implementation("org.hibernate.validator:hibernate-validator:${properties["hibernate_validator_version"]}") {
        exclude(module = "jakarta.validation-api")
    }
    // EL is required: http://hibernate.org/validator/documentation/getting-started/
    implementation("org.glassfish:javax.el:3.0.1-b11")
}

val configureBintray = ext["configureBintray"] as (artifactId: String) -> Unit
configureBintray("karibu-dsl-v8")
