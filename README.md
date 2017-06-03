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

Quickly run the bundled example application from the command-line:

```bash
git clone https://github.com/mvysny/karibu-dsl
cd karibu-dsl
./gradlew example-v8:appRun
```

Online demo of the example application: [https://demo.vaadin.com/valo-theme/#!common](https://demo.vaadin.com/valo-theme/#!common)

In case of questions you can always browse the sample projects here: todo mavi links to test projects
To start the sample projects see below.

## Using in your projects

You'll probably want to use [Vaadin On Kotlin](http://www.vaadinonkotlin.eu) which makes use of this library and adds
support for database, REST and other niceties, and has good documentation. Anyway, if you wish to use this library anyway,
please read on.

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

The navigator resolves http paths to views, so that different view shows for different app urls.
See [Vaadin Book on Navigator](https://vaadin.com/docs/-/part/framework/advanced/advanced-navigator.html) for more details.

Karibu-DSL adds additional support for View auto-discovery:

1. You need to register the Karibu's `autoViewProvider`, in your `UI.init()` as follows:
```kotlin
    fun init() {
        navigator = Navigator(this, content as ViewDisplay)
        navigator.addProvider(autoViewProvider)
    }
```

2. Then annotate your Views with `@AutoView` annotation and that's it - `autoViewProvider` will
   discover your view and register it to the navigator.

3. To navigate to given view, just call `navigateToView<YourView>()`.

#### Background on this

The mapping is generally done by converting the CamelCaseView Kotlin
class naming convention to hyphen-separated string `camel-case-view` and drops the trailing `-view`.
You can always override the name in the `@AutoView` annotation, e.g. you can make one view
'primary' by mapping it to an empty view `@AutoView("")` - this view will be shown initially when the user
browses your app.

For example, URL [http://localhost:8080/#!my-form](http://localhost:8080/#!form) 
will navigate towards the class named `MyFormView` (which must be of course annotated with `@AutoView`). The mapping is done by
the autoViewProvider and the @AutoView annotation, just check out the Kotlin documentation on those two guys.

Note: Why there is the shebang `#!` thingy? When the URL changes, 
the browser reloads the whole page. This we want to avoid with Vaadin, since Vaadin is used in so-called
*single-page web app*: on navigation, the page is not reloaded completely, instead only the page contents are mutated with the new View.
The only way to achieve this
is to change the 'fragment' portion of the URL only - only then the browser won't reload the page. That explain
the hash # part. The bang part is used for indexing: Google indexer usually ignores fragment since it points
into the same document, just to a different part. However, that's not the case with single-page self-modifying
apps, so we tell Google by using #! that the fragment differ and Google need to index that as well.

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

## Launch the example project in Intellij IDEA:

### Embedded Jetty

* The easiest option: just open the [Server.kt](example-v8/src/test/java/com/github/vok/example/crud/Server.kt) and launch it.
  Just make sure that the launcher's current working directory is set to `$MODULE_DIR$` otherwise you'll get

```
18:08:20.176 [main] WARN  o.eclipse.jetty.webapp.WebAppContext - Failed startup of context o.e.j.w.WebAppContext@49e5f737{/,null,null}{src/main/webapp}
java.io.FileNotFoundException: src/main/webapp
	at org.eclipse.jetty.webapp.WebInfConfiguration.unpack(WebInfConfiguration.java:497)
```

Disadvantages:

* For proper code hot-redeployment you'll need JRebel since Java's default debugger hot-redeployment is pretty weak.
  You can work around this by using Tomcat as said below, however that requires the Intellij Ultimate Edition. Luckily,
  it's cheaper than JRebel :-D

### Tomcat

* Open the project in Intellij IDEA Ultimate
* Launch the `example-v8` WAR in Tomcat as described here: https://kotlinlang.org/docs/tutorials/httpservlets.html
