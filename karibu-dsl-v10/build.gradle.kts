plugins {
    id("io.spring.dependency-management") version "1.0.4.RELEASE"
}

dependencyManagement {
    imports { mavenBom("com.vaadin:vaadin-bom:${ext["vaadin10_version"]}") }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    testCompile("com.github.mvysny.dynatest:dynatest-engine:${ext["dynatest_version"]}")
    testCompile("com.github.mvysny.kaributesting:karibu-testing-v10:${ext["kaributesting_version"]}")
    testCompile("org.slf4j:slf4j-simple:1.7.25")

    // Vaadin
    compile("com.vaadin:vaadin-core:${ext["vaadin10_version"]}")
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
configureBintray("karibu-dsl-v10")
