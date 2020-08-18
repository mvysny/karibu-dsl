package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.textfield.*

/**
 * Creates a [Text Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).textField(label: String? = null, block: (@VaadinDsl TextField).() -> Unit = {}): TextField
        = init(TextField(label), block)

/**
 * Creates a [Email Field](https://vaadin.com/components/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).emailField(label: String? = null, block: (@VaadinDsl EmailField).() -> Unit = {}): EmailField
        = init(EmailField(label), block)

/**
 * Creates a [Number Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).numberField(label: String? = null, block: (@VaadinDsl NumberField).() -> Unit = {}): NumberField
        = init(NumberField(label), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).textArea(label: String? = null, block: (@VaadinDsl TextArea).() -> Unit = {}): TextArea
        = init(TextArea(label), block)

/**
 * Creates a [Password Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).passwordField(label: String? = null, block: (@VaadinDsl PasswordField).() -> Unit = {}): PasswordField
        = init(PasswordField(label), block)

/**
 * Creates a [Integer Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes.
 * @since Vaadin 14.1.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).integerField(label: String? = null, block: (@VaadinDsl IntegerField).() -> Unit = {}): IntegerField
        = init(IntegerField(label), block)

/**
 * Creates a [BigDecimal Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes.
 * @since Vaadin 14.1.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).bigDecimalField(label: String? = null, block: (@VaadinDsl BigDecimalField).() -> Unit = {}): BigDecimalField
        = init(BigDecimalField(label), block)

/**
 * Selects all text in this text field. The selection is not really visible in
 * the browser unless the field is focused.
 */
public fun GeneratedVaadinTextField<*, String>.selectAll() {
    element.executeJs("this.inputElement.select()");
}

/**
 * Clears the selection in the text field and moves the cursor to the end of
 * the text. There may be no effect if the field is unfocused - the browser
 * generally selects all when the field gains focus.
 */
public fun <R: GeneratedVaadinTextField<R, String>> R.selectNone() {
    setCursorLocation(value?.length ?: 0)
}

/**
 * Moves the cursor within the text field. Has the side-effect of clearing the selection.
 *
 * There may be no effect if the field is unfocused - the browser
 * generally selects all when the field gains focus.
 */
public fun GeneratedVaadinTextField<*, String>.setCursorLocation(cursorLocation: Int) {
    select(cursorLocation until cursorLocation)
}

/**
 * Selects given characters in this text field. The selection is not really visible in
 * the browser unless the field is focused.
 */
public fun GeneratedVaadinTextField<*, String>.select(selection: IntRange) {
    require(selection.first >= 0)
    require(selection.last >= -1)
    element.executeJs("this.inputElement.setSelectionRange(${selection.first}, ${selection.last + 1})")
}

/**
 * Selects all text in this text field. The selection is not really visible in
 * the browser unless the field is focused.
 */
public fun GeneratedVaadinTextArea<*, String>.selectAll() {
    element.executeJs("this.inputElement.select()")
}

/**
 * Clears the selection in the text field and moves the cursor to the end of
 * the text. There may be no effect if the field is unfocused - the browser
 * generally selects all when the field gains focus.
 */
public fun <R: GeneratedVaadinTextArea<R, String>> R.selectNone() {
    setCursorLocation(value?.length ?: 0)
}

/**
 * Moves the cursor within the text field. Has the side-effect of clearing the selection.
 *
 * There may be no effect if the field is unfocused - the browser
 * generally selects all when the field gains focus.
 */
public fun GeneratedVaadinTextArea<*, String>.setCursorLocation(cursorLocation: Int) {
    select(cursorLocation until cursorLocation)
}

/**
 * Selects given characters in this text field. The selection is not really visible in
 * the browser unless the field is focused.
 */
public fun GeneratedVaadinTextArea<*, String>.select(selection: IntRange) {
    require(selection.first >= 0)
    require(selection.last >= -1)
    element.executeJs("this.inputElement.setSelectionRange(${selection.first}, ${selection.last + 1})")
}
