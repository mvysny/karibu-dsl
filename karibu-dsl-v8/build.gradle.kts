dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.slf4j:slf4j-api:1.7.25")

    testCompile("com.github.mvysny.dynatest:dynatest-engine:${ext["dynatest_version"]}")
    testCompile("com.github.mvysny.kaributesting:karibu-testing-v8:${ext["kaributesting_version"]}")
    testCompile("ch.qos.logback:logback-classic:1.2.3")

    compile("io.michaelrocks:bimap:1.0.2")

    // Vaadin
    compile("com.vaadin:vaadin-server:${ext["vaadin8_version"]}")
    compile("com.vaadin:vaadin-push:${ext["vaadin8_version"]}") // to include atmosphere and have support for class auto-discovery
    compile("javax.servlet:javax.servlet-api:3.1.0")

    // IDEA language injections
    compile("com.intellij:annotations:12.0")

    // always include support for bean validation
    compile("javax.validation:validation-api:2.0.1.Final")  // so that the BeanFieldGroup will perform JSR303 validations
    compile("org.hibernate.validator:hibernate-validator:${ext["hibernate_validator_version"]}")
    // EL is required: http://hibernate.org/validator/documentation/getting-started/
    compile("org.glassfish:javax.el:3.0.1-b08")
}

val configureBintray = ext["configureBintray"] as (artifactId: String) -> Unit
configureBintray("karibu-dsl-v8")
