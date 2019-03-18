package com.github.mvysny.karibudsl.v8

import com.vaadin.server.ExternalResource
import com.vaadin.server.Resource
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

// annotating DSL functions with @VaadinDsl will make Intellij mark the DSL functions in a special way
// which makes them stand out apart from the common functions, which is very nice.
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
annotation class VaadinDsl

@VaadinDsl
fun (@VaadinDsl HasComponents).button(caption: String? = null, block: (@VaadinDsl Button).() -> Unit = {})
        = init(Button(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).image(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Image).()->Unit = {}) = init(Image(caption, resource), block)

/**
 * Creates a [Label]
 * @param content the label content
 * @param block use to set additional label parameters
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).label(content: String? = null, block: (@VaadinDsl Label).()->Unit = {}) = init(Label(content), block)
/**
 * A small rectangular spinner (indeterminate progress bar).
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).spinner(block: (@VaadinDsl Label).()->Unit = {}) = init(Label()) {
    styleName = ValoTheme.LABEL_SPINNER
    block()
}

@VaadinDsl
fun (@VaadinDsl HasComponents).audio(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Audio).()->Unit = {}) = init(Audio(caption, resource), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).browserFrame(url: String? = null, block: (@VaadinDsl BrowserFrame).()->Unit = {}) =
        init(BrowserFrame(null, (if (url == null) null else ExternalResource(url))), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).checkBox(caption: String? = null, checked:Boolean? = null, block: (@VaadinDsl CheckBox).()->Unit = {}) =
        init(if (checked == null) CheckBox(caption) else CheckBox(caption, checked), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).colorPicker(popupCaption: String? = null, block: (@VaadinDsl ColorPicker).()->Unit = {}) = init(ColorPicker(popupCaption), block)

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).comboBox(caption: String? = null, block: (@VaadinDsl ComboBox<T>).()->Unit = {}) = init(ComboBox<T>(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).dateField(caption: String? = null, block: (@VaadinDsl DateField).()->Unit = {}) = init(DateField(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).dateTimeField(caption: String? = null, block: (@VaadinDsl DateTimeField).()->Unit = {}) = init(DateTimeField(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).embedded(caption: String? = null, block: (@VaadinDsl Embedded).()->Unit = {}) = init(Embedded(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).inlineDateField(caption: String? = null, block: (@VaadinDsl InlineDateField).()->Unit = {}) = init(InlineDateField(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).inlineDateTimeField(caption: String? = null, block: (@VaadinDsl InlineDateTimeField).()->Unit = {}) = init(InlineDateTimeField(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).link(caption: String? = null, url: String? = null, block: (@VaadinDsl Link).()->Unit = {}) =
        init(Link(caption, if (url == null) null else ExternalResource(url)), block)

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).listSelect(caption: String? = null, block: (@VaadinDsl ListSelect<T>).()->Unit = {}) = init(ListSelect<T>(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).nativeButton(caption: String? = null, leftClickListener: ((Button.ClickEvent)->Unit)? = null, block: (@VaadinDsl NativeButton).() -> Unit = {})
        = init(NativeButton(caption), block).apply {
    if (leftClickListener != null) onLeftClick(leftClickListener)
}

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).nativeSelect(caption: String? = null, block: (@VaadinDsl NativeSelect<T>).()->Unit = {}) = init(NativeSelect<T>(caption), block)

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).twinColSelect(caption: String? = null, block: (@VaadinDsl TwinColSelect<T>).()->Unit = {}) = init(TwinColSelect<T>(caption), block)

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).radioButtonGroup(caption: String? = null, block: (@VaadinDsl RadioButtonGroup<T>).()->Unit = {}) = init(RadioButtonGroup<T>(caption), block)

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).checkBoxGroup(caption: String? = null, block: (@VaadinDsl CheckBoxGroup<T>).()->Unit = {}) = init(CheckBoxGroup<T>(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).panel(caption: String? = null, block: (@VaadinDsl Panel).()->Unit = {}) = init(Panel(caption), block)

/**
 * Creates a [PasswordField].
 *
 * *WARNING:* When Binding to a field, do not forget to call [Binder.BindingBuilder.trimmingConverter] to perform auto-trimming:
 * passwords generally do not have whitespaces. Pasting a password to a field in a mobile phone will also add a trailing whitespace, which
 * will cause the password to not to match, which is a source of great confusion.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).passwordField(caption: String? = null, block: (@VaadinDsl PasswordField).()->Unit = {}): PasswordField {
    val component = PasswordField(caption)
    init(component, block)
    return component
}

@VaadinDsl
fun (@VaadinDsl HasComponents).progressBar(caption: String? = null, block: (@VaadinDsl ProgressBar).()->Unit = {}) = init(ProgressBar()) {
    this.caption = caption
    block()
}

@VaadinDsl
fun (@VaadinDsl HasComponents).richTextArea(caption: String? = null, block: (@VaadinDsl RichTextArea).()->Unit = {}) = init(RichTextArea(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).slider(caption: String? = null, block: (@VaadinDsl Slider).()->Unit = {}) = init(Slider(caption), block)

@VaadinDsl
fun <T: Any> (@VaadinDsl HasComponents).tree(caption: String? = null, block: (@VaadinDsl Tree<T>).()->Unit = {}) = init(Tree<T>(caption), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).upload(caption: String? = null, block: (@VaadinDsl Upload).()->Unit = {}) = init(Upload(caption, null), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).verticalSplitPanel(block: (@VaadinDsl VerticalSplitPanel).()->Unit = {}) = init(VerticalSplitPanel(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).horizontalSplitPanel(block: (@VaadinDsl HorizontalSplitPanel).()->Unit = {}) = init(HorizontalSplitPanel(), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).video(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Video).()->Unit = {}) = init(Video(caption, resource), block)
