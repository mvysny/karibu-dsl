import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.61"
    id("org.gretty") version "2.2.0"
    id("com.jfrog.bintray") version "1.8.1"
    `maven-publish`
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
        plugin("maven-publish")
        plugin("kotlin")
        plugin("com.jfrog.bintray")
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

    // creates a reusable function which configures proper deployment to Bintray
    ext["configureBintray"] = { artifactId: String ->

        val local = Properties()
        val localProperties: File = rootProject.file("local.properties")
        if (localProperties.exists()) {
            localProperties.inputStream().use { local.load(it) }
        }

        val java: JavaPluginConvention = convention.getPluginByName("java")

        val sourceJar = task("sourceJar", Jar::class) {
            dependsOn(tasks.findByName("classes"))
            classifier = "sources"
            from(java.sourceSets["main"].allSource)
        }

        publishing {
            publications {
                create("mavenJava", MavenPublication::class.java).apply {
                    groupId = project.group.toString()
                    this.artifactId = artifactId
                    version = project.version.toString()
                    pom.withXml {
                        val root = asNode()
                        root.appendNode("description", "Karibu-DSL, Kotlin extensions/DSL for Vaadin")
                        root.appendNode("name", artifactId)
                        root.appendNode("url", "https://github.com/mvysny/karibu-dsl")
                    }
                    from(components.findByName("java")!!)
                    artifact(sourceJar) {
                        classifier = "sources"
                    }
                }
            }
        }

        bintray {
            user = local.getProperty("bintray.user")
            key = local.getProperty("bintray.key")
            pkg(closureOf<BintrayExtension.PackageConfig> {
                repo = "github"
                name = "com.github.vok.karibudsl"
                setLicenses("MIT")
                vcsUrl = "https://github.com/mvysny/karibu-dsl"
                publish = true
                setPublications("mavenJava")
                version(closureOf<BintrayExtension.VersionConfig> {
                    this.name = project.version.toString()
                    released = Date().toString()
                })
            })
        }
    }
}
