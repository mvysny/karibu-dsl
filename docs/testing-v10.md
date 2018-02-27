# Getting started: Vaadin 10

To start, just add the following lines into your Gradle `build.gradle` file:

```groovy
repositories {
    maven { url "https://dl.bintray.com/mvysny/github" }
}
dependencies {
    testCompile "com.github.vok.karibudsl:karibu-testing-v10:0.3.3"
}
```

You will also need to add Kotlin support to your project, even if it will compile the testing classes only: [Using Gradle](https://kotlinlang.org/docs/reference/using-gradle.html).

## Writing your first test

todo

## A more complete example

The [Vaadin 10 Karibu-DSL Helloworld Application](https://github.com/mvysny/karibu10-helloworld-application) is a very simple project consisting of just
one view and a single test for that view.

Please head to the [Beverage Buddy](https://github.com/mvysny/beverage-buddy-vok/) for a more complete example application -
it is a very simple Vaadin-on-Kotlin-based
full-stack Vaadin 10 application which also sports a complete testing suite.
