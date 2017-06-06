@file:Suppress("DEPRECATION")

package com.github.vok.karibudsl.v7

import com.github.vok.karibudsl.VaadinDsl
import com.github.vok.karibudsl.init
import com.vaadin.event.ShortcutListener
import com.vaadin.ui.HasComponents
import com.vaadin.v7.data.Container
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup
import com.vaadin.v7.data.util.converter.Converter
import com.vaadin.v7.shared.ui.label.ContentMode
import com.vaadin.v7.ui.*
import com.vaadin.v7.ui.Calendar
import org.intellij.lang.annotations.Language
import java.util.*

/**
 * Creates a [TextField] and attaches it to this component. [TextField.nullRepresentation] is set to an empty string.
 * @param caption optional caption
 * @param value the optional value
 */
@Deprecated("Use TextField from Vaadin 8")
fun (@VaadinDsl HasComponents).textField7(caption: String? = null, value: String? = null, block: TextField.()->Unit = {}): TextField {
    val textField = TextField(caption, value)
    textField.nullRepresentation = ""
    init(textField, block)
    return textField
}

/**
 * Creates a [Label]
 * @param content the label content
 * @param block use to set additional label parameters
 */
@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).label7(content: String? = null, block: (@VaadinDsl Label).()->Unit = {}) = init(Label(content), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).calendar(caption: String? = null, block: (@VaadinDsl Calendar).()->Unit = {}) = init(Calendar(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).checkBox7(caption: String? = null, checked:Boolean? = null, block: (@VaadinDsl CheckBox).()->Unit = {}) =
        init(if (checked == null) CheckBox(caption) else CheckBox(caption, checked), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).colorPicker7(popupCaption: String? = null, block: (@VaadinDsl ColorPicker).()->Unit = {}) = init(ColorPicker(popupCaption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).comboBox7(caption: String? = null, block: (@VaadinDsl ComboBox).()->Unit = {}) = init(ComboBox(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).dateField7(caption: String? = null, block: (@VaadinDsl DateField).()->Unit = {}) = init(DateField(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).inlineDateField7(caption: String? = null, block: (@VaadinDsl InlineDateField).()->Unit = {}) = init(InlineDateField(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).listSelect7(caption: String? = null, block: (@VaadinDsl ListSelect).()->Unit = {}) = init(ListSelect(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).nativeSelect7(caption: String? = null, block: (@VaadinDsl NativeSelect).()->Unit = {}) = init(NativeSelect(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).optionGroup7(caption: String? = null, block: (@VaadinDsl OptionGroup).()->Unit = {}) = init(OptionGroup(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).popupDateField7(caption: String? = null, block: (@VaadinDsl PopupDateField).()->Unit = {}) = init(PopupDateField(caption), block)

/**
 * Creates a [PasswordField]. [TextField.nullRepresentation] is set to an empty string, and the trimming converter is automatically pre-set.
 *
 * Auto-trimming: passwords generally do not have whitespaces. Pasting a password to a field in a mobile phone will also add a trailing whitespace, which
 * will cause the password to not to match, which is a source of great confusion.
 */
@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).passwordField7(caption: String? = null, block: (@VaadinDsl PasswordField).()->Unit = {}): PasswordField {
    val component = PasswordField(caption)
    component.trimmingConverter()
    component.nullRepresentation = ""
    init(component, block)
    return component
}

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).progressBar7(block: (@VaadinDsl ProgressBar).()->Unit = {}) = init(ProgressBar(), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).richTextArea7(caption: String? = null, block: (@VaadinDsl RichTextArea).()->Unit = {}) = init(RichTextArea(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).slider7(caption: String? = null, block: (@VaadinDsl Slider).()->Unit = {}) = init(Slider(caption), block)

@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).table(caption: String? = null, dataSource: Container? = null, block: (@VaadinDsl Table).()->Unit = {}) =
        init(Table(caption, dataSource), block)

/**
 * Creates a [TextArea] and attaches it to this component. [TextField.nullRepresentation] is set to an empty string.
 * @param caption optional caption
 * @param value the optional value
 */
@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl HasComponents).textArea7(caption: String? = null, block: (@VaadinDsl TextArea).()->Unit = {}): TextArea {
    val component = TextArea(caption)
    component.nullRepresentation = ""
    init(component, block)
    return component
}

@Deprecated("Replacement planned in Vaadin 8.1")
fun (@VaadinDsl HasComponents).tree(caption: String? = null, block: (@VaadinDsl Tree).()->Unit = {}) = init(Tree(caption), block)

@Deprecated("Replacement planned in Vaadin 8.1")
fun (@VaadinDsl HasComponents).treeTable(caption: String? = null, dataSource: Container? = null, block: (@VaadinDsl TreeTable).()->Unit = {}) =
        init(TreeTable(caption, dataSource), block)

@Deprecated("Use Upload from Vaadin 8 but beware of it being immediate by default")
fun (@VaadinDsl HasComponents).upload7(caption: String? = null, block: (@VaadinDsl Upload).()->Unit = {}) = init(Upload(caption, null), block)

/**
 * Shows given html in this label.
 * @param html the html code to show.
 */
@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl Label).html(@Language("HTML") html: String?) {
    contentMode = ContentMode.HTML
    value = html
}

/**
 * Triggers given listener when the text field is focused and user presses the Enter key.
 * @param enterListener the listener to invoke when the user presses the Enter key.
 */
@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl AbstractTextField).onEnterPressed(enterListener: (AbstractTextField) -> Unit) {
    val enterShortCut = object : ShortcutListener("EnterOnTextAreaShorcut", null, KeyCode.ENTER) {
        override fun handleAction(sender: Any, target: Any) {
            enterListener(this@onEnterPressed)
        }
    }
    addFocusListener { addShortcutListener(enterShortCut) }
    addBlurListener { removeShortcutListener(enterShortCut) }
}

/**
 * Trims the user input string before storing it into the underlying property data source. Vital for mobile-oriented apps:
 * Android keyboard often adds whitespace to the end of the text when auto-completion occurs. Imagine storing a username ending with a space upon registration:
 * such person can no longer log in from his PC unless he explicitely types in the space.
 */
@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl AbstractField<String>).trimmingConverter() {
    setConverter(object : Converter<String?, String?> {
        override fun convertToModel(value: String?, targetType: Class<out String?>?, locale: Locale?): String? = value?.trim()

        override fun convertToPresentation(value: String?, targetType: Class<out String?>?, locale: Locale?): String? = value

        @Suppress("UNCHECKED_CAST")
        override fun getPresentationType(): Class<String?> = String::class.java as Class<String?>

        @Suppress("UNCHECKED_CAST")
        override fun getModelType(): Class<String?> = String::class.java as Class<String?>
    })
}

/**
 * An utility method which adds an item and sets item's caption.
 * @param the Identification of the item to be created.
 * @param caption the new caption
 * @return the newly created item ID.
 */
@Deprecated("Deprecated in Vaadin 8")
fun (@VaadinDsl AbstractSelect).addItem(itemId: Any?, caption: String) = addItem(itemId).apply { setItemCaption(itemId, caption) }!!

/**
 * Allows you to create [BeanFieldGroup] like this: `BeanFieldGroup<Person>()` instead of `BeanFieldGroup<Person>(Person::class.java)`
 */
@Deprecated("Deprecated in Vaadin 8")
inline fun <reified T : Any> BeanFieldGroup(): BeanFieldGroup<T> = BeanFieldGroup(T::class.java)
