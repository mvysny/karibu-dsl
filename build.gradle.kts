import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.vaadin") version "23.3.6" apply(false)
    `maven-publish`
    signing
}

defaultTasks("clean", "build")

allprojects {
    group = "com.github.mvysny.karibudsl"
    version = "1.2.0"

    repositories {
        mavenCentral()
    }
}

subprojects {

    apply {
        plugin("maven-publish")
        plugin("kotlin")
        plugin("org.gradle.signing")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            // to see the exceptions of failed tests in CI console.
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // creates a reusable function which configures proper deployment to Maven Central
    ext["configureBintray"] = { artifactId: String ->

        java {
            withJavadocJar()
            withSourcesJar()
        }

        tasks.withType<Javadoc> {
            isFailOnError = false
        }

        publishing {
            repositories {
                maven {
                    setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = project.properties["ossrhUsername"] as String? ?: "Unknown user"
                        password = project.properties["ossrhPassword"] as String? ?: "Unknown user"
                    }
                }
            }
            publications {
                create("mavenJava", MavenPublication::class.java).apply {
                    groupId = project.group.toString()
                    this.artifactId = artifactId
                    version = project.version.toString()
                    pom {
                        description.set("Karibu-DSL, Kotlin extensions/DSL for Vaadin")
                        name.set(artifactId)
                        url.set("https://github.com/mvysny/karibu-dsl")
                        licenses {
                            license {
                                name.set("The MIT License (MIT)")
                                url.set("https://opensource.org/licenses/MIT")
                                distribution.set("repo")
                            }
                        }
                        developers {
                            developer {
                                id.set("mavi")
                                name.set("Martin Vysny")
                                email.set("martin@vysny.me")
                            }
                        }
                        scm {
                            url.set("https://github.com/mvysny/karibu-dsl")
                        }
                    }

                    from(components["java"])
                }
            }
        }

        signing {
            sign(publishing.publications["mavenJava"])
        }
    }
}
