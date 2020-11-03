package com.github.mvysny.karibudsl.v8

import com.vaadin.server.ExternalResource
import com.vaadin.server.Resource
import com.vaadin.shared.ui.ContentMode
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

// annotating DSL functions with @VaadinDsl will make Intellij mark the DSL functions in a special way
// which makes them stand out apart from the common functions, which is very nice.
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
public annotation class VaadinDsl

@VaadinDsl
public fun (@VaadinDsl HasComponents).button(caption: String? = null, block: (@VaadinDsl Button).() -> Unit = {}): Button =
        init(Button(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).image(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Image).() -> Unit = {}): Image =
        init(Image(caption, resource), block)

/**
 * Creates a [Label]
 * @param content the label content
 * @param block use to set additional label parameters
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).label(
        content: String? = null,
        contentMode: ContentMode = ContentMode.TEXT,
        block: (@VaadinDsl Label).() -> Unit = {}
): Label {
    val label = Label(content)
    label.contentMode = contentMode
    return init(label, block)
}
/**
 * A small rectangular spinner (indeterminate progress bar).
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).spinner(block: (@VaadinDsl Label).() -> Unit = {}): Label = init(Label()) {
    styleName = ValoTheme.LABEL_SPINNER
    block()
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).audio(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Audio).() -> Unit = {}): Audio =
        init(Audio(caption, resource), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).browserFrame(url: String? = null, block: (@VaadinDsl BrowserFrame).() -> Unit = {}): BrowserFrame =
        init(BrowserFrame(null, (if (url == null) null else ExternalResource(url))), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).checkBox(caption: String? = null, checked: Boolean? = null, block: (@VaadinDsl CheckBox).() -> Unit = {}): CheckBox =
        init(if (checked == null) CheckBox(caption) else CheckBox(caption, checked), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).colorPicker(popupCaption: String? = null, block: (@VaadinDsl ColorPicker).() -> Unit = {}): ColorPicker =
        init(ColorPicker(popupCaption), block)

@VaadinDsl
public fun <T> (@VaadinDsl HasComponents).comboBox(caption: String? = null, block: (@VaadinDsl ComboBox<T>).() -> Unit = {}): ComboBox<T> =
        init(ComboBox<T>(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).dateField(caption: String? = null, block: (@VaadinDsl DateField).() -> Unit = {}): DateField =
        init(DateField(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).dateTimeField(caption: String? = null, block: (@VaadinDsl DateTimeField).() -> Unit = {}): DateTimeField =
        init(DateTimeField(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).embedded(caption: String? = null, block: (@VaadinDsl Embedded).() -> Unit = {}): Embedded =
        init(Embedded(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).inlineDateField(caption: String? = null, block: (@VaadinDsl InlineDateField).() -> Unit = {}): InlineDateField =
        init(InlineDateField(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).inlineDateTimeField(caption: String? = null, block: (@VaadinDsl InlineDateTimeField).() -> Unit = {}): InlineDateTimeField =
        init(InlineDateTimeField(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).link(caption: String? = null, url: String? = null, block: (@VaadinDsl Link).() -> Unit = {}): Link =
        init(Link(caption, if (url == null) null else ExternalResource(url)), block)

@VaadinDsl
public fun <T> (@VaadinDsl HasComponents).listSelect(caption: String? = null, block: (@VaadinDsl ListSelect<T>).() -> Unit = {}): ListSelect<T> =
        init(ListSelect<T>(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).nativeButton(caption: String? = null, leftClickListener: ((Button.ClickEvent) -> Unit)? = null, block: (@VaadinDsl NativeButton).() -> Unit = {}): NativeButton {
    val button = NativeButton(caption)
    if (leftClickListener != null) {
        button.onLeftClick(leftClickListener)
    }
    return init(button, block)
}

@VaadinDsl
public fun <T> (@VaadinDsl HasComponents).nativeSelect(caption: String? = null, block: (@VaadinDsl NativeSelect<T>).() -> Unit = {}): NativeSelect<T> =
        init(NativeSelect<T>(caption), block)

@VaadinDsl
public fun <T> (@VaadinDsl HasComponents).twinColSelect(caption: String? = null, block: (@VaadinDsl TwinColSelect<T>).() -> Unit = {}): TwinColSelect<T> =
        init(TwinColSelect<T>(caption), block)

@VaadinDsl
public fun <T> (@VaadinDsl HasComponents).radioButtonGroup(caption: String? = null, block: (@VaadinDsl RadioButtonGroup<T>).() -> Unit = {}): RadioButtonGroup<T> =
        init(RadioButtonGroup<T>(caption), block)

@VaadinDsl
public fun <T> (@VaadinDsl HasComponents).checkBoxGroup(caption: String? = null, block: (@VaadinDsl CheckBoxGroup<T>).() -> Unit = {}): CheckBoxGroup<T> =
        init(CheckBoxGroup<T>(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).panel(caption: String? = null, block: (@VaadinDsl Panel).() -> Unit = {}): Panel =
        init(Panel(caption), block)

/**
 * Creates a [PasswordField].
 *
 * *WARNING:* When Binding to a field, do not forget to call [Binder.BindingBuilder.trimmingConverter] to perform auto-trimming:
 * passwords generally do not have whitespaces. Pasting a password to a field in a mobile phone will also add a trailing whitespace, which
 * will cause the password to not to match, which is a source of great confusion.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).passwordField(caption: String? = null, block: (@VaadinDsl PasswordField).() -> Unit = {}): PasswordField {
    val component = PasswordField(caption)
    init(component, block)
    return component
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).progressBar(caption: String? = null, block: (@VaadinDsl ProgressBar).() -> Unit = {}): ProgressBar {
    val progressBar = ProgressBar()
    progressBar.caption = caption
    return init(progressBar, block)
}

@VaadinDsl
public fun (@VaadinDsl HasComponents).richTextArea(caption: String? = null, block: (@VaadinDsl RichTextArea).() -> Unit = {}): RichTextArea =
        init(RichTextArea(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).slider(caption: String? = null, block: (@VaadinDsl Slider).() -> Unit = {}): Slider =
        init(Slider(caption), block)

@VaadinDsl
public fun <T : Any> (@VaadinDsl HasComponents).tree(caption: String? = null, block: (@VaadinDsl Tree<T>).() -> Unit = {}): Tree<T> =
        init(Tree<T>(caption), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).upload(caption: String? = null, block: (@VaadinDsl Upload).() -> Unit = {}): Upload =
        init(Upload(caption, null), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).verticalSplitPanel(block: (@VaadinDsl VerticalSplitPanel).() -> Unit = {}): VerticalSplitPanel =
        init(VerticalSplitPanel(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).horizontalSplitPanel(block: (@VaadinDsl HorizontalSplitPanel).() -> Unit = {}): HorizontalSplitPanel =
        init(HorizontalSplitPanel(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).video(caption: String? = null, resource: Resource? = null, block: (@VaadinDsl Video).() -> Unit = {}): Video =
        init(Video(caption, resource), block)
