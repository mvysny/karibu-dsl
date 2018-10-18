import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.61"
    id("org.gretty") version "2.2.0"
    id("com.jfrog.bintray") version "1.8.1"
}

defaultTasks("clean", "build")

allprojects {
    ext["vaadin8_version"] = "8.5.2"
    ext["vaadin10_version"] = "11.0.1"
    ext["dynatest_version"] = "0.11"
    ext["kaributesting_version"] = "0.5.0"
    ext["hibernate_validator_version"] = "6.0.13.Final"
    val local = Properties()
    val localProperties: File = rootProject.file("local.properties")
    if (localProperties.exists()) {
        localProperties.inputStream().use { local.load(it) }
    }
    ext["local"] = local

    group = "com.github.vok.karibudsl"
    version = "0.4.12-SNAPSHOT"

    repositories {
        jcenter()
        maven { setUrl("https://dl.bintray.com/mvysny/github") }
    }

    tasks {
        // Heroku
        val stage by registering {
            dependsOn("build")
        }
    }
}

subprojects {

    apply {
        plugin("kotlin")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            // to see the exceptions of failed tests in Travis-CI console.
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
