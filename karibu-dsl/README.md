[![GitHub tag](https://img.shields.io/github/tag/mvysny/karibu-dsl.svg)](https://github.com/mvysny/karibu-dsl/tags)

# Vaadin Kotlin DSL

Karibu-DSL supports Vaadin 14+.

## Using in your projects

You can include Karibu-DSL library into your WAR project very easily,
simply by including appropriate Gradle dependency:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile("com.github.mvysny.karibudsl:karibu-dsl:x.y.z")
}
```

> Note: obtain the newest version from the latest tag name above. See the Compatibility Chart for version compatible with your Vaadin version

Maven: Karibu-DSL is in Maven-Central, just add Karibu-DSL to your project as a dependency:

```xml
<project>
	<dependencies>
		<dependency>
			<groupId>com.github.mvysny.karibudsl</groupId>
			<artifactId>karibu-dsl</artifactId>
			<version>x.y.z</version>
		</dependency>
    </dependencies>
</project>
```

To quickly test out Karibu DSL you can simply start with one
of the example applications below.

### UI Basic Example Application

A very simple Gradle-based example application is located here: [karibu-helloworld-application](https://github.com/mvysny/karibu-helloworld-application)
The project only shows a very simple Button, which makes it an ideal quick start application for experimenting
and further development.
 
To checkout the example app and run it:
  
```bash
git clone https://github.com/mvysny/karibu-helloworld-application
cd karibu-helloworld-application
./gradlew appRun
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

## Tutorials

### How to write DSLs

The following example shows a really simple form with two fields and one "Save" button (todo screenshot).

```kotlin
verticalLayout {
  w = wrapContent; isSpacing = true

  formLayout {
    textField("Name:") {
      focus()
    }
    textField("Age:")
  }
  horizontalLayout {
    content { align(right, middle) }
    button("Save") {
      onLeftClick { okPressed() }
      setPrimary()
    }
  }
}
```

Please see the [Creating UIs](https://www.vaadinonkotlin.eu/creating_ui/) tutorial
for more information, or the [DSLs Explained](https://www.vaadinonkotlin.eu/dsl_explained/) article.

For technical explanation on how this works, please read the
[Using DSL to write structured UI code](https://mvysny.github.io/Using-DSL-to-write-structured-UI-code/)
article.

### VerticalLayout / HorizontalLayout

Layouting in Vaadin 14 employs the so-called *flex layout*. Vaadin 14 still
does provide `VerticalLayout` and `HorizontalLayout` classes which loosely resembles their Vaadin 8 counterparts,
however they use flex layout under the hood and hence there are very important differences.

Generally you use the new `HorizontalLayout` as follows:

```kotlin
horizontalLayout {
  content { align(right, middle) }
}
```

Important notes:

* `HorizontalLayout` only supports one row of components; if you have multiple rows you need to use `FlexLayout`.
* Never use `setSizeFull()` nor set the `setWidth("100%")` as you used to do with Vaadin 8. With Vaadin 10 it will
not work as you expect. With Vaadin 8 the child would fill the slot allocated by HorizontalLayout. However with Vaadin 10 and flex layout
there are no slots; setting the width to `100%` would make the component match the width of parent - it would set it to be as wide as
the HorizontalLayout is.

Instead of setting the width of the child to `100%`, set the width to some ideal width, say, `100px` (or `null` to wrap its contents).
The child will initially be exactly
as wide as you tell it to be; if you use `flexGrow` the component will enlarge itself automatically.

To alter the layout further, call the following properties on children:
* Most important: `flexGrow` (and its brother `isExpand`) expands that particular child to take up all of the remaining space. The child
is automatically enlarged.
* `verticalAlignSelf` to align child vertically; it is not possible to align particular child horizontally
* `flexShrink` - when there is not enough room for all children then they are shrank
* `flexBasis`

Please read the [Vaadin 10 server-side layouting for Vaadin 8 and Android developers](http://mavi.logdown.com/posts/6855605) for a tutorial on how to
use `VerticalLayout`/`HorizontalLayout`.

### Forms

Please see [Creating Forms](https://www.vaadinonkotlin.eu/forms) tutorial
for more information.

Example of a form:
```kotlin
class ReviewEditor(val bean: Review) : VerticalLayout() {
  // to propagate the changes made in the fields by the user, we will use binder to bind the field to the Review property.
  private val binder: Binder<Review> = beanValidationBinder()

  init {
    formLayout {
      responsiveSteps { "0"(1); "50em"(2) }

      textField("Beverage name") {
        // no need to have validators here: they are automatically picked up from the bean field.
        bind(binder).trimmingConverter().bind(Review::name)
      }
      integerField("Times tasted") {
        bind(binder).bind(Review::count)
      }
      comboBox<Category>("Choose a category") {
        setItemLabelGenerator { it.name }

        // can't create new Categories here
        isAllowCustomValue = false

        // provide the list of options as a DataProvider, providing instances of Category
        setItems({ item: Category, filterText: String ->
            CategoryService.categoryMatchesFilter(
                item,
                filterText
            )
        }, CategoryService.findAll())

        // bind the combo box to the Review::category field so that changes done by the user are stored.
        bind(binder).bind(Review::category)
      }
      datePicker("Choose the date") {
        max = LocalDate.now()
        min = LocalDate.of(1, 1, 1)
        value = LocalDate.now()
        bind(binder).bind(Review::date)
      }
      comboBox<String>("Mark a score") {
        isAllowCustomValue = false
        setItems("1", "2", "3", "4", "5")
        bind(binder).toInt().bind(Review::score)
      }
    }
    button("Save") {
      onLeftClick { save() }
    }
    
    binder.readBean(bean)
  }
  
  private fun save() {
    if (form.binder.writeBeanIfValid(bean)) {
      onSaveItem(bean)
    } else {
      val status = form.binder.validate()
      Notification.show(status.validationErrors.joinToString("; ") { it.errorMessage }, 3000, Notification.Position.BOTTOM_START)
    }
  }
}
```

### FormLayout

Thanks to Kotlin DSL extensions, you can write the responsive-steps configuration in a
much more condensed form as
```kotlin
responsiveSteps { "0px"(1, top); "30em"(2, aside) }
```

You can also use the `colspan` extension property to set colspan for child fields:

```kotlin
formLayout {
  textArea {
    colspan = 2
  }
}
```

### TabSheet

Vaadin provides the [Tabs](https://vaadin.com/components/vaadin-tabs) component,
but that's just the tab bar without any contents. That's exactly what the TabSheet
Karibu-DSL component solves.

You can add and populate tabs in two ways:
* eagerly, by calling either `tab()` or `addTab()` function.
* lazily, by calling `addLazyTab()`.

Example code:
```
tabSheet {
  tab("DSL-style tab") {
    span("Hello")
  }
  addTab("Old-school style", Span("Hi"))
  addLazyTab("Populated when first selected") { Span("Lazy") }
}
```

### Writing your own components

Usually one writes custom components by extending the `KComposite` class. Please read the [Creating a Component](https://vaadin.com/docs/v10/flow/creating-components/tutorial-component-composite.html) for more details.

> We promote composition over inheritance, similar to [React's Composition vs Inheritance](https://reactjs.org/docs/composition-vs-inheritance.html).
You should always extend `KComposite` instead of extending e.g. `HorizontalLayout` - extending from `HorizontalLayout` makes
it part of your class and you can't replace it with e.g. `FlowLayout` without breaking backward compatibility.

An example of such a component:

```kotlin
class ButtonBar : KComposite() {
    init {
        ui {
            // create the component UI here; maybe even attach very simple listeners here
            horizontalLayout {
                button("ok") {
                    onLeftClick { okClicked() }
                }
                button("cancel") {
                    onLeftClick { cancelClicked() }
                }
            }
        }

        // perform any further initialization here
    }

    // add listener methods here
    private fun okClicked() {}
    private fun cancelClicked() {}
}
```

To allow for your component to be inserted into other layouts, you will need to introduce your own extension method for your component.
Just write the following code below your component:

```kotlin
@VaadinDsl
fun (@VaadinDsl HasComponents).buttonBar(block: (@VaadinDsl ButtonBar).()->Unit = {}) = init(ButtonBar(), block)
```

This will allow you to use your component as follows:

```kotlin
verticalLayout {
  buttonBar {
    styleName = "black"
    ... // etc
  }
}
```

#### The `KComposite` Pattern

The advantage of extending from `KComposite`, instead of extending the layout (e.g. `VerticalLayout`) directly, is as follows:

* The component public API is not polluted by methods coming from the `VerticalLayout`,
  resulting in a more compact and to-the-point API. The API coming from `KComposite` is
  tiny in comparison.
* Since the `VerticalLayout` API doesn't leak into our component, we are free to
  replace the `VerticalLayout` with any other layout in the future, without breaking the API.
* The UI structure is more clearly visible. Take the `ButtonBar` class below as
  an example: it can clearly be seen that the buttons are nested in the `HorizontalLayout`:

Example 1.: ButtonBar extending KComposite with a clear UI hierarchy
```kotlin
class ButtonBar : KComposite() {
    val root = ui {
        horizontalLayout {
            button("ok")
        }
    }
}
```

Example 2.: ButtonBar extending HorizontalLayout without a clear indication that
the button is nested in a horizontal layout:
```kotlin
class ButtonBar : HorizontalLayout() {
    init {
        button("ok")
    }
}
```

The `root` variable will be marked by the IDE as unused. This is okay: the
side-effect of the `ui {}` is that it runs the `horizontalLayout()` function
which then attaches the `HorizontalLayout` to the `KComposite` itself.
However, you may prefer to get rid of this unused `root` variable and call the
`ui {}` from the `init {}` Kotlin initializer. The downside is that the
UI-creating code will be indented by two tabs instead of one.

### Retrieving TimeZone from the browser

It's important to retrieve browser's TimeZone, in order to be able to convert
`Instant`, `Date` and `Calendar`-typed values (retrieved from the database)
into `LocalDate` and `LocalDateTime` values edited by Vaadin's `DatePicker`
and `DateTimePicker`.

In order to do that, it's recommended to call `fetchTimeZoneFromBrowser()` when
[Vaadin Session is being initialized](https://vaadin.com/docs/v14/flow/advanced/tutorial-application-lifecycle.html).

### CustomField

Use this example to populate the contents of the CustomField in a DSL manner:

```kotlin
class DateTimeField: CustomField<DateInterval>() {
  private val content = content {
    horizontalLayout {
      dateField()
      timeField()
    }
  }
}
```

### Grid

Any of the following DSLs work and will pass the class of the item to Grid's constructor.
In order for you to be in control and create the columns
in a controlled DSL manner, no columns are auto-created.

```kotlin
grid<Person> {}
grid(Person::class) {}
grid(Person::class.java) {}
```

Alternatively, you can decide not to pass the item class into Grid's constructor:

```kotlin
grid<Person>(klass = null) {}
grid<Person>(clazz = null) {}
```

A more of a real-world example:

```kotlin
grid<Category> {
  isExpand = true; addThemeName("row-dividers")

  columnFor(Category::name) {
      setHeader("Category")
  }
  column({ it.getReviewCount() }) {
    setHeader("Beverages")
  }
  componentColumn({ cat -> createEditButton(cat) }) {
    flexGrow = 0; key = "edit"
  }

  gridContextMenu = gridContextMenu {
      item("New", { editorDialog.createNew() })
      item("Edit (Alt+E)", { cat -> if (cat != null) edit(cat) })
      item("Delete", { cat -> if (cat != null) deleteCategory(cat) })
  }
}
```

### VirtualList

Only available since Vaadin 23; you need to depend on the `karibu-dsl-v23` module. (Since Karibu-DSL 1.1.3)

```kotlin
virtualList<Person> {}
```

### Dialogs

Since Vaadin 23.1 the Dialog has a header and a footer. You need to depend on the `karibu-dsl-v23` module. (Since Karibu-DSL 1.1.3)

```kotlin
Dialog().apply {
  header { h3("Header") } // or better: this.setHeaderTitle("Header")
  verticalLayout(isPadding = false) {
    // contents
  }
  footer {
    button("Save") {
      setPrimary()
    }
    button("Cancel")
  }
}.open()
```

## Launching the example project in Intellij IDEA

Please see the [Vaadin Boot](https://github.com/mvysny/vaadin-boot#preparing-environment) documentation
on how you run, develop and package this Vaadin-Boot-based app.

Simply run `./gradlew example:run` from the command-line, or run the `main()` method in the `Main.kt` file.

## Testing

Please see the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library, to learn more about how to test Karibu-DSL apps.
