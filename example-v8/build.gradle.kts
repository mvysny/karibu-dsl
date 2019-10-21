// The Vaadin Framework uitest module, Valo theme test part. Ported to Kotlin.
// https://github.com/vaadin/framework/tree/master/uitest/src/main/java/com/vaadin/tests/themes/valo

plugins {
    id("com.devsoap.plugin.vaadin") version "1.4.1"
    war
    id("org.gretty")
}

// don't update Jetty carelessly, it tends to break Atmosphere and Push support!
// test before commit :-)
// see https://github.com/vaadin/framework/issues/8134 for details
val jettyVer = "9.4.2.v20170220"

vaadin {
    version = properties["vaadin8_version"] as String
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

val staging by configurations.creating

dependencies {
    compile(project(":karibu-dsl-v8"))

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    compile("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")
    compile("org.slf4j:slf4j-api:${properties["slf4j_version"]}")
    // this will allow us to configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:${properties["slf4j_version"]}")

    // Vaadin: workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-client-compiled:${properties["vaadin8_version"]}")
    compile("com.vaadin:vaadin-themes:${properties["vaadin8_version"]}")

    // easy development with Jetty
    testCompile("org.eclipse.jetty:jetty-webapp:$jettyVer")
    testCompile("org.eclipse.jetty:jetty-annotations:$jettyVer")
    // workaround for https://github.com/Atmosphere/atmosphere/issues/978
    testCompile("org.eclipse.jetty:jetty-continuation:$jettyVer")
    // make sure that JSR356 is on classpath, otherwise Atmosphere will use native Jetty Websockets which will result
    // in ClassNotFoundException: org.eclipse.jetty.websocket.WebSocketFactory$Acceptor
    // since the class is no longer there in Jetty 9.4
    testCompile("org.eclipse.jetty.websocket:javax-websocket-server-impl:$jettyVer")

    // Embedded Undertow is currently unsupported since it has no servlet/listener/... autodiscovery capabilities:
    // http://stackoverflow.com/questions/22307748/deploying-servlets-webapp-in-embedded-undertow

    // Embedded Tomcat is currently unsupported since it always starts its own class loader which is only known on Tomcat start time
    // and we can't thus discover and preload JPA entities.

    testCompile("com.github.mvysny.kaributesting:karibu-testing-v8:${properties["kaributesting_version"]}")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")

    // heroku app runner
    staging("com.github.jsimone:webapp-runner-main:9.0.24.0")
}

// Heroku
tasks {
    val copyToLib by registering(Copy::class) {
        into("$buildDir/server")
        from(staging) {
            include("webapp-runner*")
        }
    }
    "stage" {
        dependsOn("build", copyToLib)
    }
}
