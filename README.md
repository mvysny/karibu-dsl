# Karibu-DSL (Vaadin Kotlin Extensions)

[![Build Status](https://travis-ci.org/mvysny/karibu-dsl.svg?branch=master)](https://travis-ci.org/mvysny/karibu-dsl)

# WORK IN PROGRESS, NOT YET FULLY AVAILABLE

Kotlin extensions and DSL for the [Vaadin](https://www.vaadin.com) framework:

* Allows you to create Vaadin UI designs/component graphs in the DSL fashion
* Auto-discovery and auto-mapping of Views via a custom Navigator
* Additional useful methods which Vaadin lacks

Supports:

* Vaadin 8 (including support for v7 compatibility package)

## QuickStart

In case of questions you can always browse the sample projects here: todo mavi links to test projects
To start the sample projects see below.

### Maven quickstart

1. Generate a Vaadin app: https://vaadin.com/maven
2. Add the JCenter repository to your `pom.xml`:
```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```
3. Add Karibu-DSL dependency:
```xml
<dependency>
    <groupId>com.github.vok.karibudsl</groupId>
    <artifact>karibu-dsl-v8</artifact>
    <version>0.1.0</version>
</dependency>
```
(If you need support for v7-compat libraries, use the `karibu-dsl-v8compat7` artifact - this also depends on `karibu-dsl-v8`)

4. Add Kotlin support to your `pom.xml`: in Intellij just create a Kotlin class, Intellij will offer to auto-add kotlin
   to your `pom.xml`. Otherwise: https://kotlinlang.org/docs/reference/using-maven.html


### Gradle quickstart
Currently there is no template project. Yet Karibu-DSL is in JCenter:
 
 1. create a Gradle project: https://github.com/johndevs/gradle-vaadin-plugin/wiki/Getting-Started
 2. add dependency on VaadinOnKotlin: `dependency 'com.github.vok.karibudsl:karibu-dsl-v8:0.1.0`
 3. Add Kotlin to your `build.gradle`: https://kotlinlang.org/docs/reference/using-gradle.html

## Tutorials

### Navigator examples

todo

### How to write DSLs for Vaadin 8 and Vaadin8-v7-compat

The following example shows a really simple form with two fields and one "Save" button (todo screenshot).

```kotlin
verticalLayout {
  w = wrapContent
  formLayout {
    isSpacing = true
    textField("Name:") {
      focus()
    }
    textField("Age:")
  }
  horizontalLayout {
    alignment = Alignment.MIDDLE_RIGHT
    button("Save") {
      onLeftClick { okPressed() }
      setPrimary()
    }
  }
}
```

If you need to use the old v7 components:
```kotlin
verticalLayout7 {
  w = wrapContent
  formLayout {
    isSpacing = true
    textField7("Name:") {
      focus()
    }
    textField7("Age:")
  }
  horizontalLayout7 {
    isSpacing = true
    alignment = Alignment.MIDDLE_RIGHT
    button("Save") {
      onLeftClick { okPressed() }
      setPrimary()
    }
  }
}
```

#### More complex examples:

* More complex form: see the example project for details @todo link for v8 and v7-compat forms
* A Grid example: see the example project for details @todo link for v8 and v7-compat forms

#### Simple popups

```kotlin
popupView("Details") {
  verticalLayout {
    w = 300.px
    formLayout { 
      w = 100.perc
      ... 
    }
    button("Close", { isPopupVisible = false }) {
      alignment = Alignment.MIDDLE_RIGHT
    }
  }
}
```

todo screenshot

#### Keyboard shortcuts via operator overloading

```kotlin
import com.github.vok.framework.vaadin.ModifierKey.Alt
import com.github.vok.framework.vaadin.ModifierKey.Ctrl
import com.vaadin.event.ShortcutAction.KeyCode.C

button("Create New Person (Ctrl+Alt+C)") {
  onLeftClick { ... }
  clickShortcut = Ctrl + Alt + C
}
```

#### Width/height

```kotlin
button {
  icon = ...
  w = 48.px
  h = 50.perc
}
if (button.w.isFillParent) { ... }
```

## Run the sample projects outside of any IDE:

Run it with Jetty Runner:

* Download Jetty Runner here: http://www.eclipse.org/jetty/documentation/current/runner.html
* Run `./gradlew`
* Locate the WAR in `vok-example-crud/build/libs/`
* Run the WAR via the Runner: `java -jar jetty-runner*.jar *.war`
* Open [http://localhost:8080](http://localhost:8080)

## To develop in IDEA:

### Embedded Jetty

* The easiest option: just open the [Server.kt](vok-example-crud/src/test/java/com/github/vok/example/crud/Server.kt) and launch it.

### Jetty

* Open the project in IDEA
* Download the Jetty Distribution zip file from here: http://download.eclipse.org/jetty/stable-9/dist/
* Unpack the Jetty Distribution
* In IDEA, add Jetty Server Local launcher, specify the path to the Jetty Distribution directory and attach the `vok-example-crud` WAR-exploded artifact to the runner
* Run or Debug the launcher

### Tomcat

* Open the project in IDEA
* Launch the `vok-example-crud` WAR in Tomcat as described here: https://kotlinlang.org/docs/tutorials/httpservlets.html
