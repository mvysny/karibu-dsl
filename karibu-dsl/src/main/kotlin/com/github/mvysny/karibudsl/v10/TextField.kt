package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.textfield.*

/**
 * Creates a [Text Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).textField(label: String? = null, block: (@VaadinDsl TextField).() -> Unit = {}): TextField
        = init(TextField(label), block)

/**
 * Creates a [Email Field](https://vaadin.com/components/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).emailField(label: String? = null, block: (@VaadinDsl EmailField).() -> Unit = {}): EmailField
        = init(EmailField(label), block)

/**
 * Creates a [Number Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).numberField(label: String? = null, block: (@VaadinDsl NumberField).() -> Unit = {}): NumberField
        = init(NumberField(label), block)

@VaadinDsl
fun (@VaadinDsl HasComponents).textArea(label: String? = null, block: (@VaadinDsl TextArea).() -> Unit = {}): TextArea
        = init(TextArea(label), block)

/**
 * Creates a [Password Field](https://vaadin.com/elements/vaadin-text-field). See the HTML Examples link for a list
 * of possible alternative themes.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).passwordField(label: String? = null, block: (@VaadinDsl PasswordField).() -> Unit = {}): PasswordField
        = init(PasswordField(label), block)

/**
 * Creates a [Integer Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes.
 * @since Vaadin 14.1.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).integerField(label: String? = null, block: (@VaadinDsl IntegerField).() -> Unit = {}): IntegerField
        = init(IntegerField(label), block)

/**
 * Creates a [BigDecimal Field](https://vaadin.com/components/vaadin-number-field/java-examples/number-field). See the HTML Examples link for a list
 * of possible alternative themes.
 * @since Vaadin 14.1.
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).bigDecimalField(label: String? = null, block: (@VaadinDsl BigDecimalField).() -> Unit = {}): BigDecimalField
        = init(BigDecimalField(label), block)

fun GeneratedVaadinTextField<*, String>.selectAll() {
    getElement().executeJs("this.inputElement.select()");
}

fun GeneratedVaadinTextField<*, String>.selectNone() {
    setCursorLocation(0)
}

/**
 * Moves the cursor within the text field. Has the side-effect of clearing the selection.
 */
fun GeneratedVaadinTextField<*, String>.setCursorLocation(cursorLocation: Int) {
    select(cursorLocation until cursorLocation)
}

fun GeneratedVaadinTextField<*, String>.select(selection: IntRange) {
    require(selection.first >= 0)
    require(selection.last >= -1)
    getElement().executeJs("this.inputElement.setSelectionRange(${selection.first}, ${selection.last + 1})")
}

fun GeneratedVaadinTextArea<*, String>.selectAll() {
    getElement().executeJs("this.inputElement.select()")
}

fun GeneratedVaadinTextArea<*, String>.selectNone() {
    setCursorLocation(0)
}

/**
 * Moves the cursor within the text field. Has the side-effect of clearing the selection.
 */
fun GeneratedVaadinTextArea<*, String>.setCursorLocation(cursorLocation: Int) {
    select(cursorLocation until cursorLocation)
}

fun GeneratedVaadinTextArea<*, String>.select(selection: IntRange) {
    require(selection.first >= 0)
    require(selection.last >= -1)
    getElement().executeJs("this.inputElement.setSelectionRange(${selection.first}, ${selection.last + 1})")
}
