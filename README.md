[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![GitHub tag](https://img.shields.io/github/tag/mvysny/karibu-dsl.svg)](https://github.com/mvysny/karibu-dsl/tags)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibudsl/karibu-dsl-v8/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibudsl/karibu-dsl-v8)
[![CI](https://github.com/mvysny/karibu-dsl/actions/workflows/gradle.yml/badge.svg)](https://github.com/mvysny/karibu-dsl/actions/workflows/gradle.yml)

# Karibu-DSL (Vaadin Kotlin Extensions)

This is a Kotlin extensions and DSL library for the [Vaadin](https://www.vaadin.com) framework.

Need a button? Write `button {}`. Need a text field? Write `textField {}`.
Need a layout? Write
```kotlin
verticalLayout {
    val nameField = textField("Your name") {}
    button("Click me") {
        setPrimary()
        onClick { Notification.show("Hello, ${nameField.value}") }
    }
}
```
Build own components and views by composing existing components with layouts.

This library:

* Allows you to create Vaadin UI designs in the structured code way; the idea behind the library
  is explained in the [DSLs: Explained for Vaadin](https://www.vaadinonkotlin.eu//dsl_explained/) article.
  The general DSL idea is explained in [Kotlin Type-Safe Builders](https://kotlinlang.org/docs/reference/type-safe-builders.html).
* Includes useful components like `PopupButton` and `TabSheet`
* Contains additional useful methods which Vaadin lacks; see [karibu-tools](https://github.com/mvysny/karibu-tools)

Compatibility chart:

| Vaadin Version | Karibu DSL Version | Min. Java Version | Maintained |
|----------------|--------------------|-------------------|------------|
| 24.5+          | 2.2.0+             | 17                | *yes*      |
| 24+            | 2.0.0+             | 17                | no         |
| 23             | 1.x                | 11                | no         |
| 14-22          | 1.x                | 8                 | no         |
| 8              | 1.0.x              | 5                 | no         |

## UI Basic Example Application

A very simple Gradle-based example application is located here: [karibu-helloworld-application](https://github.com/mvysny/karibu-helloworld-application)
The project only shows a very simple Button, which makes it an ideal quick start application for experimenting
and further development.

To check out the example app and run it:

```bash
git clone https://github.com/mvysny/karibu-helloworld-application
cd karibu-helloworld-application
./gradlew run
```

The app will run on [http://localhost:8080](http://localhost:8080).

### The "Beverage Buddy" Example Application

The [Beverage Buddy VoK](https://github.com/mvysny/beverage-buddy-vok)
is a more complex example app. It's the Vaadin Beverage Buddy app
backed by an in-memory H2 database. You can quickly run the bundled example application from the command-line:

```bash
git clone https://github.com/mvysny/beverage-buddy-vok
cd beverage-buddy-vok
./gradlew appRun
```

The example app will be running at [http://localhost:8080](http://localhost:8080).

## Documentation/Tutorial

**>>> Documentation + Tutorial <<<**: Please read the [Karibu-DSL Vaadin 14+ tutorial](karibu-dsl).

The origins of the word *Karibu*: it's a term for North American subspecies of the reindeer; that connects to
*Vaadin* (which is a Finnish word for a female reindeer). A nice connotation comes from Swahili where *Karibu*
means *welcome*.

> Note: For Groovy Vaadin DSL please see the [Vaadin Groovy Builder](https://github.com/mvysny/vaadin-groovy-builder) library.

## Vaadin 8 No longer maintained

Vaadin 8 support is no longer maintained. If you're still
using Vaadin 8, please use Karibu-DSL 1.0.x. Please find the documentation for
Karibu-DSL Vaadin 8 in the [1.0.x README.md](https://github.com/mvysny/karibu-dsl/tree/1.0.x). Also see [Issue #35](https://github.com/mvysny/karibu-dsl/issues/35)
for more details.

## Why DSL?

Just compare the Kotlin-based [CategoriesList](example/src/main/kotlin/com/vaadin/starter/beveragebuddy/ui/categories/CategoriesList.kt)
with the original Java [CategoriesList](https://github.com/vaadin/beverage-starter-flow/blob/master/src/main/java/com/vaadin/starter/beveragebuddy/ui/views/categorieslist/CategoriesList.java).
Both render the [Beverage Buddy: Categories](https://v-herd.eu/beverage-buddy-vok/categories) page, yet with Kotlin DSL:

* The UI structure is immediately visible
* The code is more readable and much shorter and concise
* You can more easily copy parts of the UI and paste it into your project

Supports [Vaadin 14+](https://vaadin.com/).

## Example Projects

Vaadin 24:

* Simple one-page-one-button example app: [karibu-helloworld-application](https://github.com/mvysny/karibu-helloworld-application) (Gradle), [karibu-helloworld-application](https://github.com/mvysny/karibu-helloworld-application-maven) (Maven)
* Two-page app demoing grids and database: [Beverage Buddy VoK](https://github.com/mvysny/beverage-buddy-vok) running on [vok](https://www.vaadinonkotlin.eu); [Beverage Buddy JOOQ](https://github.com/mvysny/beverage-buddy-jooq)
* A simple database-backed one-page task list app: [vaadin-kotlin-pwa](https://github.com/mvysny/vaadin-kotlin-pwa)

## Vaadin 8 UITest / Component Palette

Back in the days there was a Vaadin 8 project called UITest (or Component Palette) which
demoed all Vaadin 8 components along with all of ValoTheme styles. The project
is no longer around officially, but there's a karibu-dsl fork of the project. To
run it quickly, follow these steps:

```bash
git clone https://github.com/mvysny/karibu-dsl
cd karibu-dsl
git checkout 1.0.x
./gradlew clean build example-v8:appRun
```

You may need JDK 11 or JDK 8 to run the demo: JDK 17+ may or may not work.

## Deprecation notes

The `NumberRangePopup`, `DateRangePopup`, `ClosedInterval`, `NumberInterval` and `DateInterval` classes have been removed from this project since they
were too specific. You can find them in the [jdbi-orm-vaadin](https://gitlab.com/mvysny/jdbi-orm-vaadin) project.

## Testing

Please see the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library, to learn more about how to test Karibu-DSL apps.

# License

Licensed under the [MIT License](https://opensource.org/licenses/MIT).

Copyright (c) 2017-2020 Martin Vysny

All rights reserved.

Permission is hereby granted, free  of charge, to any person obtaining
a  copy  of this  software  and  associated  documentation files  (the
"Software"), to  deal in  the Software without  restriction, including
without limitation  the rights to  use, copy, modify,  merge, publish,
distribute,  sublicense, and/or sell  copies of  the Software,  and to
permit persons to whom the Software  is furnished to do so, subject to
the following conditions:

The  above  copyright  notice  and  this permission  notice  shall  be
included in all copies or substantial portions of the Software.
THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

# Contributing / Developing

See [Contributing](CONTRIBUTING.md).
