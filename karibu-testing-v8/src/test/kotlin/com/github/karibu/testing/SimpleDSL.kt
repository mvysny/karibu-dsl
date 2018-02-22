package com.github.karibu.testing

import com.vaadin.ui.*

private fun HasComponents.addChild(child: Component) {
    when (this) {
        is ComponentContainer -> addComponent(child)
        is SingleComponentContainer -> {
            if (componentCount >= 1) throw IllegalArgumentException("$this can only have one child")
            content = child
        }
        else -> throw IllegalArgumentException("Unsupported component container $this")
    }
}
private fun <T : Component> HasComponents.init(component: T, block: T.()->Unit = {}): T {
    addChild(component)
    component.block()
    return component
}
fun HasComponents.verticalLayout(block: VerticalLayout.()->Unit = {}) = init(VerticalLayout(), block)
fun HasComponents.textField(caption: String? = null, value: String? = null, block: TextField.()->Unit = {}): TextField {
    val textField = TextField(caption, value ?: "")  // TextField no longer accepts null as a value.
    init(textField, block)
    return textField
}
fun HasComponents.label(content: String? = null, block: Label.()->Unit = {}) = init(Label(content), block)
fun HasComponents.button(caption: String? = null, block: Button.() -> Unit = {})
        = init(Button(caption), block)
