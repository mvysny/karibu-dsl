# Vaadin 8 DSL

## Using in your projects

You'll probably want to use [Vaadin On Kotlin](http://www.vaadinonkotlin.eu) which makes use of this library and adds
support for database, REST and other niceties, and has good documentation. However, if you just want to try out the Vaadin
DSL bindings without any database and other stuff, please read on.

You can include Karibu-DSL library into your WAR project very easily,
simply by including appropriate Gradle dependency:

```groovy
repositories {
    maven { url "https://dl.bintray.com/mvysny/github" }
}

dependencies {
    compile("com.github.vok.karibudsl:karibu-dsl-v8:x.y.z")
}
```

> Note: obtain the newest version from the release name, and the tag name as well: [https://github.com/mvysny/karibu-dsl/releases](https://github.com/mvysny/karibu-dsl/releases)

However, to quickly test out Karibu DSL you can simply start with one
of the example applications below.

### UI Base Application

A very simple Gradle-based application archetype is located here: https://github.com/mvysny/karibu-helloworld-application
The project only shows a very simple Button, so it is an ideal quick start application for experimenting
and further development. And you can always consult the [example-v8](example-v8) project for interesting
components or new ways how to use them.
 
To generate the archetype and run the app:
  
```bash
git clone https://github.com/mvysny/karibu-helloworld-application
cd karibu-helloworld-application
./gradlew appRun
```

The app will run on [http://localhost:8080](http://localhost:8080).

Please see the [archetype github page](https://github.com/mvysny/karibu-helloworld-application) for further
details on development.

### "Component Palette" App

The [example-v8](../example-v8) app bundles a "Component Palette" app which demonstrates Vaadin components,
a responsive navigation menu and a navigator.
Quickly run the application from the command-line:

```bash
git clone https://github.com/mvysny/karibu-dsl
cd karibu-dsl
./gradlew example-v8:appRun
```

The example app will be running at [http://localhost:8080](http://localhost:8080).
You can check the [Karibu UITest Online Demo](https://karibu-uitest.herokuapp.com/) running on Heroku.

In case of questions you can always [browse the sample project sources here](../example-v8).

## Tutorials

### Navigator examples

The Vaadin `Navigator` resolves http paths to views, in order to show different parts of the app to the user. For example,
when the user navigates to `http://localhost:8080/invoices` the Navigator makes sure that the `InvoicesView` UI component is shown. 
See [Vaadin Book on Navigator](https://vaadin.com/docs/-/part/framework/advanced/advanced-navigator.html) for more details.

Karibu-DSL adds additional support for View auto-discovery:

1. You need to register the Karibu's `autoViewProvider`, in your `UI.init()` as follows:
```kotlin
    fun init() {
        navigator = Navigator(this, content as ViewDisplay)
        navigator.addProvider(autoViewProvider)
    }
```
See [Example MyUI.kt](../example-v8/src/main/kotlin/com/github/vok/karibudsl/example/MyUI.kt) for a working code example.

2. Then annotate your Views with `@AutoView` annotation and that's it - `autoViewProvider` will
   discover your view and register it to the navigator.

3. To navigate to given view, just call `navigateToView<YourView>()`.

#### Background on this

The mapping is generally done by converting the CamelCaseView Kotlin
class naming convention to hyphen-separated string `camel-case-view` and drops the trailing `-view`.
You can always override the name in the `@AutoView` annotation, e.g. you can make one view
'primary' by mapping it to an empty view `@AutoView("")` - this view will be shown initially when the user
browses your app.

For example, URL [http://localhost:8080/my-form](http://localhost:8080/my-form) 
will navigate towards the class named `MyFormView` (which must be of course annotated with `@AutoView`). The mapping is done by
the `autoViewProvider` and the `@AutoView` annotation, just check out the Kotlin documentation on those two guys.

> Historical reason for the shebang `#!` URL part: When the URL changes, 
the browser reloads the whole page. This we want to avoid with Vaadin, since Vaadin is used in so-called
*single-page web app*: on navigation, the page is not reloaded completely, instead only the page contents are mutated with the new View.
The only way to achieve this
is to change the 'fragment' portion of the URL only - only then the browser won't reload the page. That explain
the hash # part. The bang part is used for indexing: Google indexer usually ignores fragment since it points
into the same document, just to a different part. However, that's not the case with single-page self-modifying
apps, so we tell Google by using `#!` that the fragment differ and Google need to index that as well.

However, Vaadin 8 now supports HTML 5 history API which allows us to use simple plain URLs. Just make sure that your
UI is annotated with `@PushStateNavigation`.

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

* More complex form: see the example [FormView](../example-v8/src/main/kotlin/com/github/vok/karibudsl/example/form/FormView.kt) for details
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

## Writing your own components

Usually one writes custom components by extending the `CustomComponent` class; but you can of course extend
any Vaadin component. Please read the [Composition with CustomComponent](https://vaadin.com/docs/-/part/framework/components/components-customcomponent.html) for more details.

To integrate your component with Karibu-DSL, you will need to introduce your own extension method for your component.
Just implement your component, say, `MyComposite`,
and then you just write the following code below your component:

```kotlin
fun HasComponents.myComposite(block: MyComposite.()->Unit = {}) = init(MyComposite(), block)
```

This will allow you to use your component as follows:

```kotlin
verticalLayout {
  myComposite {
    styleName = "black"; w = 100.perc
    ... // etc
  }
}
```

## Launching the example project in Intellij IDEA:

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

Please see https://github.com/mvysny/karibu-helloworld-application for tutorial on how to launch a WAR project in Tomcat in Intellij.
Then:

* Open this whole project in Intellij IDEA Ultimate, simply by File / Open... and clicking [build.gradle](build.gradle).
* Navigate to the `example-v8` project and launch it as an exploded WAR in Tomcat

## Testing

Please see the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library, to learn more about how to test Karibu-DSL apps.

