package com.github.vok.karibudsl

import com.vaadin.server.ExternalResource
import com.vaadin.server.Resource
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@DslMarker
annotation class VaadinDsl

fun HasComponents.button(caption: String? = null, leftClickListener: ((Button.ClickEvent)->Unit)? = null, block: (@VaadinDsl Button).() -> Unit = {})
        = init(Button(caption), block).apply {
    if (leftClickListener != null) onLeftClick(leftClickListener)
}

fun HasComponents.image(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Image).()->Unit = {}) = init(Image(caption, resource), block)

/**
 * Creates a [TextField] and attaches it to this component.
 * @param caption optional caption
 * @param value the optional value
 */
fun HasComponents.textField(caption: String? = null, value: String? = null, block: (@VaadinDsl TextField).()->Unit = {}): TextField {
    val textField = TextField(caption, value ?: "")  // TextField no longer accepts null as a value.
    init(textField, block)
    return textField
}

/**
 * Creates a [Label]
 * @param content the label content
 * @param block use to set additional label parameters
 */
fun HasComponents.label(content: String? = null, block: (@VaadinDsl Label).()->Unit = {}) = init(Label(content), block)
/**
 * A small rectangular spinner (indeterminate progress bar).
 */
fun HasComponents.spinner(block: (@VaadinDsl Label).()->Unit = {}) = init(Label()) {
    styleName = ValoTheme.LABEL_SPINNER
}

fun HasComponents.accordion(block: (@VaadinDsl Accordion).()->Unit = {}) = init(Accordion(), block)

fun HasComponents.audio(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Audio).()->Unit = {}) = init(Audio(caption, resource), block)

fun HasComponents.browserFrame(url: String? = null, block: (@VaadinDsl BrowserFrame).()->Unit = {}) =
        init(BrowserFrame(null, (if (url == null) null else ExternalResource(url))), block)

fun HasComponents.checkBox(caption: String? = null, checked:Boolean? = null, block: (@VaadinDsl CheckBox).()->Unit = {}) =
        init(if (checked == null) CheckBox(caption) else CheckBox(caption, checked), block)

fun HasComponents.colorPicker(popupCaption: String? = null, block: (@VaadinDsl ColorPicker).()->Unit = {}) = init(ColorPicker(popupCaption), block)

fun <T> HasComponents.comboBox(caption: String? = null, block: (@VaadinDsl ComboBox<T>).()->Unit = {}) = init(ComboBox<T>(caption), block)

fun HasComponents.dateField(caption: String? = null, block: (@VaadinDsl DateField).()->Unit = {}) = init(DateField(caption), block)

fun HasComponents.dateTimeField(caption: String? = null, block: (@VaadinDsl DateTimeField).()->Unit = {}) = init(DateTimeField(caption), block)

fun HasComponents.embedded(caption: String? = null, block: (@VaadinDsl Embedded).()->Unit = {}) = init(Embedded(caption), block)

fun HasComponents.inlineDateField(caption: String? = null, block: (@VaadinDsl InlineDateField).()->Unit = {}) = init(InlineDateField(caption), block)

fun HasComponents.inlineDateTimeField(caption: String? = null, block: (@VaadinDsl InlineDateTimeField).()->Unit = {}) = init(InlineDateTimeField(caption), block)

fun HasComponents.link(caption: String? = null, url: String? = null, block: (@VaadinDsl Link).()->Unit = {}) =
        init(Link(caption, if (url == null) null else ExternalResource(url)), block)

fun <T> HasComponents.listSelect(caption: String? = null, block: (@VaadinDsl ListSelect<T>).()->Unit = {}) = init(ListSelect<T>(caption), block)

fun HasComponents.menuBar(block: (@VaadinDsl MenuBar).()->Unit = {}) = init(MenuBar(), block)

fun HasComponents.nativeButton(caption: String? = null, leftClickListener: ((Button.ClickEvent)->Unit)? = null, block: (@VaadinDsl NativeButton).() -> Unit = {})
        = init(NativeButton(caption), block).apply {
    if (leftClickListener != null) onLeftClick(leftClickListener)
}

fun <T> HasComponents.nativeSelect(caption: String? = null, block: (@VaadinDsl NativeSelect<T>).()->Unit = {}) = init(NativeSelect<T>(caption), block)

fun <T> HasComponents.radioButtonGroup(caption: String? = null, block: (@VaadinDsl RadioButtonGroup<T>).()->Unit = {}) = init(RadioButtonGroup<T>(caption), block)

fun <T> HasComponents.checkBoxGroup(caption: String? = null, block: (@VaadinDsl CheckBoxGroup<T>).()->Unit = {}) = init(CheckBoxGroup<T>(caption), block)

fun HasComponents.panel(caption: String? = null, block: (@VaadinDsl Panel).()->Unit = {}) = init(Panel(caption), block)

/**
 * Creates a [PasswordField].
 *
 * *WARNING:* When Binding to a field, do not forget to call [Binder.BindingBuilder.trimmingConverter] to perform auto-trimming:
 * passwords generally do not have whitespaces. Pasting a password to a field in a mobile phone will also add a trailing whitespace, which
 * will cause the password to not to match, which is a source of great confusion.
 */
fun HasComponents.passwordField(caption: String? = null, block: (@VaadinDsl PasswordField).()->Unit = {}): PasswordField {
    val component = PasswordField(caption)
    init(component, block)
    return component
}

fun HasComponents.progressBar(block: (@VaadinDsl ProgressBar).()->Unit = {}) = init(ProgressBar(), block)

fun HasComponents.richTextArea(caption: String? = null, block: (@VaadinDsl RichTextArea).()->Unit = {}) = init(RichTextArea(caption), block)

fun HasComponents.slider(caption: String? = null, block: (@VaadinDsl Slider).()->Unit = {}) = init(Slider(caption), block)

fun HasComponents.tabSheet(block: (@VaadinDsl TabSheet).()->Unit = {}) = init(TabSheet(), block)

/**
 * Creates a [TextArea] and attaches it to this component.
 * @param caption optional caption
 * @param value the optional value
 */
fun HasComponents.textArea(caption: String? = null, block: (@VaadinDsl TextArea).()->Unit = {}): TextArea {
    val component = TextArea(caption)
    init(component, block)
    return component
}

fun <T: Any> HasComponents.tree(caption: String? = null, block: (@VaadinDsl Tree<T>).()->Unit = {}) = init(Tree<T>(caption), block)

fun HasComponents.upload(caption: String? = null, block: (@VaadinDsl Upload).()->Unit = {}) = init(Upload(caption, null), block)

fun HasComponents.verticalSplitPanel(block: (@VaadinDsl VerticalSplitPanel).()->Unit = {}) = init(VerticalSplitPanel(), block)

fun HasComponents.horizontalSplitPanel(block: (@VaadinDsl HorizontalSplitPanel).()->Unit = {}) = init(HorizontalSplitPanel(), block)

fun HasComponents.video(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Video).()->Unit = {}) = init(Video(caption, resource), block)
