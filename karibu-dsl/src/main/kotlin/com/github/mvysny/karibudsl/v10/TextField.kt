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
