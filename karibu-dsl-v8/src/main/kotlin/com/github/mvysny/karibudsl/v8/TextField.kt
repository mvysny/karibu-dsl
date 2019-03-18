package com.github.mvysny.karibudsl.v8

import com.vaadin.annotations.JavaScript
import com.vaadin.server.AbstractJavaScriptExtension
import com.vaadin.shared.JavaScriptExtensionState
import com.vaadin.ui.AbstractTextField
import com.vaadin.ui.HasComponents
import com.vaadin.ui.TextArea
import com.vaadin.ui.TextField

/**
 * Creates a [TextField] and attaches it to this component.
 * @param caption optional caption
 * @param value the optional value
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).textField(caption: String? = null, value: String = "", block: (@VaadinDsl TextField).() -> Unit = {}): TextField = init(TextField(caption, value), block)

/**
 * Creates a [TextArea] and attaches it to this component.
 * @param caption optional caption
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).textArea(caption: String? = null, block: (@VaadinDsl TextArea).() -> Unit = {}): TextArea = init(TextArea(caption), block)

/**
 * Simply sets the `type` HTML attribute to the `<input>` element represented by the [TextField] Vaadin component. Instead of
 * using this class directly, you can use [AbstractTextField.type] to change the type easily.
 */
@JavaScript("type-extension.js")
class TypeExtension(textField: AbstractTextField) : AbstractJavaScriptExtension(textField) {

    var type: String
        get() = getState(false).type
        set(value) {
            state.type = value
        }

    override fun getState(): TypeExtensionState = super.getState() as TypeExtensionState
    override fun getState(markAsDirty: Boolean): TypeExtensionState = super.getState(markAsDirty) as TypeExtensionState

    class TypeExtensionState : JavaScriptExtensionState() {
        @JvmField
        var type: String = ""
    }
}

/**
 * Controls the type of the text field. You can use any of the [HTML types allowed](https://www.w3schools.com/tags/att_input_type.asp), however
 * only a couple may actually be usable:
 * * email
 * * number
 * * search
 * * text (default)
 * * url
 * * range
 * You should use other Vaadin fields as necessary, for example use [com.vaadin.ui.DateField] to input date etc.
 */
var AbstractTextField.type: String? by ExtensionPropertyDelegate(TypeExtension::class.java, TypeExtension::type, { TypeExtension(it) }, "text")

/**
 * Creates a [TextField] with the [type] of `"email"` and attaches it to this component.
 * @param caption optional caption
 * @param value the optional value
 */
@VaadinDsl
fun (@VaadinDsl HasComponents).emailField(caption: String? = null, value: String = "", block: (@VaadinDsl TextField).() -> Unit = {}) = textField(caption, value) {
    type = "email"
    block()
}
