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

# How to write DSLs

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

# Documentation

Note that Karibu-DSL depends on [Karibu-Tools](https://github.com/mvysny/karibu-tools)
which brings in a bunch of useful extension functions to Vaadin components: make sure to consult Karibu-Tools documentation as well.

## Button

The [Vaadin Button](https://vaadin.com/docs/latest/components/button) component allows users to perform actions:
```kotlin
button(text: String? = null, icon: Component? = null, id: String? = null) {}
```
Example of use:
```kotlin
button("Create (Alt+N)", VaadinIcon.PLUS.create()) {
  setPrimary()
  onClick { save() }
  addClickShortcut(Alt + KEY_N)
}
```

* `setPrimary()` is a shortcut for adding `ButtonVariant.LUMO_PRIMARY` button variant.
* `onClick()` is a shorthand for `addClickListener()`.
* `iconButton(icon) {}` is a shorthand for creating a button with just an icon (no text) and
  adding the `ButtonVariant.LUMO_ICON` button variant.

## VerticalLayout / HorizontalLayout

[Vaadin Vertical Layout](https://vaadin.com/docs/latest/components/vertical-layout) places components top-to-bottom in a column.
[Vaadin Horizontal Layout](https://vaadin.com/docs/latest/components/horizontal-layout) places components side-by-side in a row.
```kotlin
horizontalLayout(padding: Boolean = false, spacing: Boolean = true, classNames: String = "") {}
verticalLayout(padding: Boolean = false, spacing: Boolean = true, classNames: String = "") {}
```

Example code:

```kotlin
horizontalLayout {
  content { align(right, middle) }
  button("Hello")
}
```

The `content{}` block configures the layout how it should align its children.
The syntax for both Vertical and Horizontal layout is:
```kotlin
content { align(horizontalAlignment, verticalAlignment )}
```
Additionally there's `content { center() }` which centers the content perfectly
inside of the vertical / horizontal layout.

Important notes:

* Layouting in Vaadin 14 employs the so-called *CSS FlexBox*; if the layout doesn't work according to your
  expectations, the [CSS Flexbox Layout Guide](https://css-tricks.com/snippets/css/a-guide-to-flexbox/) might help.
* `HorizontalLayout` only supports one row of components; if you have multiple rows you need to use `FlexLayout`.
* The `setSizeFull()` nor set the `setWidth("100%")` sets it to be as wide as the HorizontalLayout is, which is not what you want.

Instead of setting the width of the child to `100%`, set the width to some ideal width, say, `100px` (or `null` to wrap its contents).
The child will initially be exactly
as wide as you tell it to be; if you use `flexGrow` the component will enlarge itself automatically.

To alter the layout further, call the following properties on children:
* Most important: `flexGrow` (and its brother `isExpand`) expands that particular child to take up all of the remaining space. The child
is automatically enlarged.
* `verticalAlignSelf` to align child vertically; it is not possible to align particular child horizontally
* `flexShrink` - when there is not enough room for all children then they are shrank
* `flexBasis`

## Accordion

[Vaadin Accordion](https://vaadin.com/docs/latest/components/accordion)
is a vertically stacked set of expandable panels. It reduces clutter and helps maintain the user’s focus by showing only the relevant content at any time.
```kotlin
accordion {}
```
Examples:
```kotlin
accordion {
  panel("lorem ipsum") {
    content { span("dolor sit amet") }
  }
}
```
or
```kotlin
accordion {
  panel {
    summary { checkBox("More Lorem Ipsum?") }
    content { span("dolor sit amet") }
  }
}
```

## App Layout

[App Layout](https://vaadin.com/docs/latest/components/app-layout) is a component for building common application layouts.

```kotlin
appLayout {}
```
Typically, you don't use the `appLayout{}` DSL call when building views; it's usually easier to extend
from the `AppLayout` class:
```kotlin
class MainLayout : AppLayout() {
  init {
    isDrawerOpened = false
    navbar {
      drawerToggle()
      h3("Beverage Buddy")
    }
    drawer {
      sideNav("Tasks") {
        route(TaskListView::class, VaadinIcon.CHECK)
        route(AboutView::class, VaadinIcon.QUESTION)
      }
    }
  }
}
```

However, a cleaner (but a bit more complex) way is to use `KComposite`:
```kotlin
class MainLayout : KComposite() {
  private val root = ui {
    appLayout {
      isDrawerOpened = false
      navBar {}
      // ...
    }
  }
}
```

## Avatar

[Avatar](https://vaadin.com/docs/latest/components/avatar) is a graphical representation of an object or entity, for example, a person or an organization.

```kotlin
avatar(name: String? = null, imageUrl: String? = null) {}
```

### Avatar Group
[Avatar Group](https://vaadin.com/docs/latest/components/avatar#avatar-group) is used to group multiple Avatars together. It can be used, for example,
to show that there are multiple users viewing the same page or for listing members of a project.
```kotlin
avatarGroup {
  item("John Foo")
  item("Random Guy")
}
```

## Badge

[Badges](https://vaadin.com/docs/latest/components/badge) are colored text elements containing small bits of information. They’re used for labeling content, displaying metadata and/or highlighting information.
```kotlin
badge(text: String? = null) {}
```
Example:
```kotlin
badge("Hello") {
  addThemeVariants(BadgeVariant.PILL, BadgeVariant.SMALL)
}
```

## Checkbox

[Checkbox](https://vaadin.com/docs/latest/components/checkbox) is an input field representing a binary choice. Checkbox Group is a group of related binary choices.
```kotlin
checkBox(label: String? = null) {}
checkBoxGroup<T>(label: String? = null) {}
```
Example:
```kotlin
checkBox("I accept the conditions")
```

## ComboBox

[Combo Box](https://vaadin.com/docs/latest/components/combo-box) allows the user to choose a value from
a filterable list of options presented in an overlay. It supports lazy loading and can be configured to accept custom typed values.
```kotlin
comboBox<T>(label: String? = null) {}
```
Example:
```kotlin
comboBox<Department>("Department") {
  setItems(DepartmentDataProvider())
  setItemLabelGenerator { it.name }
}
```

## ConfirmDialog

[Confirm Dialog](https://vaadin.com/docs/latest/components/confirm-dialog) is a modal Dialog used to confirm user actions.

Since `ConfirmDialog` isn't expected to be inserted into components, there's no special DSL function for the dialog, but there
is a utility builder function which creates the dialog and opens it automatically (since Karibu-DSL 2.1.5). Example of use:
```kotlin
openConfirmDialog(
  "Delete beverage",
  "Are you sure you want to delete stuff?"
) {
  setConfirmButton("Delete") {
    deleteItem()
  }
  setConfirmIsDanger()
  setCloseOnCancel("Cancel")
}
```

## ContextMenu

[Context Menu](https://vaadin.com/docs/latest/components/context-menu) is a component that you can attach to any component to display a context menu.
The menu appears on right (default) or left click. On a touch device, a long press opens the context menu.
```kotlin
contextMenu {}
gridContextMenu {}
```
Example:
```kotlin
button("foo") {
  contextMenu {
    item("save", { e -> println("saved") })
    item("style") {
      item("bold", { e -> println("bold") })
      item("italic", { e -> println("italic") })
    }
    separator()
    item("clear", { e -> println("clear") })
  }
}
```

## CustomField

[Custom Field](https://vaadin.com/docs/latest/components/custom-field) is a component for wrapping multiple components as a single field. It provides standard input field features like label, helper, validation, and data binding. Use it to create custom input components.

`CustomField` is intended to be extended, not inserted into the component tree as-is, therefore there's no DSL.
However, there is a `content{}` DSL which populates the contents of the field:
```kotlin
class DateRangePopup: CustomField<DateInterval>() {
  private val content: Button = content {
    button {
      onClick {
        isDialogVisible = !isDialogVisible
      }
    }
  }
}
```

## DatePicker

[Date Picker](https://vaadin.com/docs/latest/components/date-picker) is an input field that allows the user to enter a date by typing or by selecting from a calendar overlay.

```kotlin
datePicker(label: String? = null) {}
```

## DateTimePicker

[Date Time Picker](https://vaadin.com/docs/latest/components/date-time-picker) is an input field for selecting both a date and a time.
```kotlin
dateTimePicker(label: String? = null) {}
```

## Details

[Details](https://vaadin.com/docs/latest/components/details) is an expandable panel for showing and hiding content from the user, to make the UI less cluttered.
```kotlin
details(summaryText: String? = null) {}
```
To populate the `Details` component, use the `content{}` DSL:
```kotlin
details("Hello") {
  content { button("hi!") }
}
```
The summary part can also be populated with components:
```kotlin
details {
  summary { button("learn more") }
  content { button("hi!") }
}
```

## Dialogs

[Dialog](https://vaadin.com/docs/latest/components/dialog) is a small window that can be used to present information and user interface elements in an overlay.

Since Dialog is not meant to be inserted into a component tree, there's no DSL for the dialog itself. There's
`openDialog{}` function which creates and opens the dialog and allows you to build the contents, but it's limited
to one-off dialogs; usually you'll have a bigger dialogs which are typically implemented as a class extending the `Dialog` class.

There are utility DSL functions to build dialog's header and footer. Example:

```kotlin
openDialog {
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
}
```

[Karibu-Tools utility functions for Dialogs](https://github.com/mvysny/karibu-tools?tab=readme-ov-file#dialogs).

## EmailField

[Email Field](https://vaadin.com/docs/latest/components/email-field) is an extension of Text Field that accepts only email addresses as input. If the given address is invalid, the field is highlighted in red and an error message appears underneath the input.
```kotlin
emailField(label: String? = null) {}
```

## FormLayout

[Form Layout](https://vaadin.com/docs/latest/components/form-layout) allows you to build responsive forms with multiple columns, and to position input labels above or to the side of the input.
```kotlin
formLayout(classNames: String = "") {}
```

Thanks to Kotlin DSL extensions, you can write the responsive-steps configuration in a
much more condensed form as
```kotlin
formLayout {
  responsiveSteps { "0px"(1, top); "30em"(2, aside) }
  textField("Name") //...
}
```

You can also use the `colspan` extension property to set colspan for child fields:

```kotlin
formLayout {
  textArea {
    colspan = 2
  }
}
```

## Grid

[Vaadin Grid](https://vaadin.com/docs/latest/components/grid)
is a component for displaying tabular data, including various enhancements to grid renderings.

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

## Icons

[The icon component](https://vaadin.com/docs/latest/components/icons) can render SVG and font icons. Two icon collections are available out-of-the-box.
```kotlin
icon(icon: VaadinIcon) {}
icon(collection: String, icon: String) {}
```

## ListBox

[List Box](https://vaadin.com/docs/latest/components/icons) allows the user to select one or more values from a scrollable list of items.
```kotlin
listBox {}
```

## Login

[Login is a component](https://vaadin.com/docs/latest/components/icons) that contains a log-in form.
You can use it to authenticate the user with a username and password. It’s compatible with password managers, supports internationalization, and works on all device sizes.

Login comes in two variants: `LoginForm` (a component designed to be inserted into the component tree) and a
`LoginOverlay` (a modal dialog, not meant to be inserted into the component tree). Therefore, the DSL function is available only for the `LoginForm`,
and there is a builder function for `LoginOverlay`:
```kotlin
loginForm(loginI18n: LoginI18n = LoginI18n.createDefault()) {}
openLoginOverlay(loginI18n: LoginI18n = LoginI18n.createDefault()) {}
```

There are additional utility functions for both LoginForm and LoginOverlay:
* `loginI18n{}` builds the `LoginI18n` object which contain the login messages;
* `setErrorMessage()` allows you to easily set the error message.

Example of a `LoginView` (see e.g. [vok-security-demo](https://github.com/mvysny/vok-security-demo) example app):
```kotlin
class LoginView : KComposite(), BeforeEnterObserver {
  private val root = ui {
    verticalLayout {
      setSizeFull(); isPadding = false; content { center() }
      val loginI18n: LoginI18n = loginI18n {
        header.title = "VoK Security Demo"
        additionalInformation = "Log in as user/user or admin/admin"
        setErrorMessage("Couldn't log in", "Incorrect username or password")
      }
      loginForm(loginI18n) {
        addLoginListener { e ->
          if (!login(e.username, e.password)) {
            isError = true
          } else {
            UI.getCurrent().navigateTo("")
          }
        }
      }
    }
  }
}
```
Example of the `LoginOverlay`:
```kotlin
loginButton.onClick {
  val loginI18n: LoginI18n = loginI18n {
    header.title = "VoK Security Demo"
    additionalInformation = "Log in as user/user or admin/admin"
  }
  openLoginOverlay(loginI18n) {
    addLoginListener { e ->
      if (!login(e.username, e.password)) {
        isError = true
      }
    }
  }
}
```

## MenuBar

[Menu Bar](https://vaadin.com/docs/latest/components/menu-bar) is a horizontal button bar with hierarchical drop-down menus. Menu items can trigger an action, open a menu, or work as a toggle.
```kotlin
menuBar {}
```
Example:
```kotlin
menuBar {
  item("save", { e -> println("saved") })
  item("style") {
    item("bold", { e -> println("bold") })
    separator()
    item("italic", { e -> println("italic") })
  }
  item("clear", { e -> println("clear") })
}
```

## MessageInput

[Message Input](https://vaadin.com/docs/latest/components/message-input) allows users to author and send messages.
```kotlin
messageInput {}
```

## MessageList

[Message List](https://vaadin.com/docs/latest/components/message-list) allows you to show a list of messages, for example, a chat log. You can configure the text content, information about the sender, and the time of sending for each message.
```kotlin
messageList(messages: List<MessageListItem> = listOf()) {}
```

## MultiSelectComboBox

[Multi-Select Combo Box](https://vaadin.com/docs/latest/components/multi-select-combo-box) allows the user to choose one or more values from a filterable list of options presented in an overlay.
The component supports the same features as the regular Combo Box, such as lazy loading or allowing custom typed values. This page explains how to add this component to your project and how to configure it.
```kotlin
multiSelectComboBox(label: String? = null) {}
```
Example:
```kotlin
multiSelectComboBox<String> {
  setItems("a", "b", "c")
}
```

## Notification

[Notification](https://vaadin.com/docs/latest/components/notification) is used to provide feedback to the user about activities, processes, and events in the application.

No DSL - Notification is not meant to be inserted into the component tree but rather opened via
`Notification.open()`.

There are bunch of [notification utility functions coming from Karibu-Tools](https://github.com/mvysny/karibu-tools?tab=readme-ov-file#notification).

## NumberField

[Number Field](https://vaadin.com/docs/latest/components/number-field) is an input field that accepts only numeric input. The input can be a decimal or an integer. There is also a big decimal for Flow.

```kotlin
numberField(label: String? = null) {} // for Double values
integerField(label: String? = null) {} // For Integer values
bigDecimalField(label: String? = null) {} // For BigDecimal values
```

## PasswordField

[Password Field](https://vaadin.com/docs/latest/components/password-field) is an input field for entering passwords.
The input is masked by default. On mobile devices, though, the last typed letter is shown for a brief moment. The masking can be toggled using an optional reveal button.
```kotlin
passwordField(label: String? = null) {}
```

## Popover

[Popover](https://vaadin.com/docs/latest/components/popover) is a generic overlay whose position is anchored to an element in the UI.
```kotlin
popover(position: PopoverPosition? = null) {}
```
Example:
```kotlin
button("hello") {
  popover {
    isOpenOnFocus = true
    verticalLayout {
      span("Hi!")
    }
  }
}
```

## ProgressBar

[Progress Bar](https://vaadin.com/docs/latest/components/progress-bar) shows the amount of completion of a task or process. The progress can be determinate or indeterminate. Use Progress Bar to show an ongoing process that takes a noticeable time to finish.
```kotlin
progressBar(min: Double = 0.0, max: Double = 1.0, value: Double = min, indeterminate: Boolean = false) {}
```

## RadioButton

[Radio Button Group](https://vaadin.com/docs/latest/components/radio-button) allows users to select one value among multiple choices.
```kotlin
radioButtonGroup(label: String? = null) {}
```

Example:

```kotlin
radioButtonGroup<String>("Nationality") {
  setItems("Finnish", "Swedish")
}
```

> Note: Vaadin doesn't offer a single radio button component out-of-the-box.

## Scroller

[Scroller](https://vaadin.com/docs/latest/components/scroller) is a component container for creating scrollable areas in the UI.
```kotlin
scroller(scrollDirection: Scroller.ScrollDirection = Scroller.ScrollDirection.BOTH) {}
```
Example:
```kotlin
scroller {
  height = "100px"
  content {
    div {
      width = "200px"; height = "200px"; element.styles.add("background-color", "red")
    }
  }
}
```

## Select

[Select](https://vaadin.com/docs/latest/components/select) allows users to choose a single value from a list of options presented in an overlay.
```kotlin
select<T>(label: String? = null) {}
```
Example:
```kotlin
select<String>("Foo") {
  setItems("a", "b", "c")
}
```

## Side Navigation

[Side Navigation](https://vaadin.com/docs/latest/components/side-nav) provides a vertical list of navigation links with support for collapsible, nested sections.
```kotlin
sideNav(label: String? = null) {}
route(routeClass: KClass<out Component>, icon: VaadinIcon? = null, title: String = getRouteTitle(routeClass)) {}
item(title: String, path: String? = null) {}
```
Example:
```kotlin
sideNav("Messages") {
  route(MainRoute::class, VaadinIcon.INBOX, "Inbox")
  route(MainRoute::class, VaadinIcon.PAPERPLANE, "Sent")
  route(MainRoute::class, VaadinIcon.TRASH, "Trash")
}
sideNav("Admin") {
  isCollapsible = true
  route(MainRoute::class, VaadinIcon.GROUP, "Users")
  route(MainRoute::class, VaadinIcon.KEY, "Permissions")
}
```

## TODO more components

## VirtualList

Only available since Vaadin 23; you need to depend on the `karibu-dsl-v23` module. (Since Karibu-DSL 1.1.3)

```kotlin
virtualList<Person> {}
```

## TabSheet

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

# Forms

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

# Writing your own components

Usually one writes custom components by extending the `KComposite` class. Please read the [Creating a Component](https://vaadin.com/docs/v10/flow/creating-components/tutorial-component-composite.html) for more details.

> We promote composition over inheritance, similar to [React's Composition vs Inheritance](https://reactjs.org/docs/composition-vs-inheritance.html).
> You should always extend `KComposite` instead of extending e.g. `HorizontalLayout` - extending from `HorizontalLayout` makes
> it part of your class and you can't replace it with e.g. `FlowLayout` without breaking backward compatibility.

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

## The `KComposite` Pattern

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

## Retrieving TimeZone from the browser

It's important to retrieve browser's TimeZone, in order to be able to convert
`Instant`, `Date` and `Calendar`-typed values (retrieved from the database)
into `LocalDate` and `LocalDateTime` values edited by Vaadin's `DatePicker`
and `DateTimePicker`.

In order to do that, it's recommended to call `fetchTimeZoneFromBrowser()` when
[Vaadin Session is being initialized](https://vaadin.com/docs/v14/flow/advanced/tutorial-application-lifecycle.html).

## CustomField

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
