package com.github.karibu.testing

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasClickListeners
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

fun <T : Component> HasComponents.init(component: T, block: T.()->Unit = {}): T {
    add(component)
    component.block()
    return component
}
fun HasComponents.verticalLayout(block: VerticalLayout.() -> Unit = {})
        = init(VerticalLayout(), block)
fun <T: Component> HasClickListeners<T>.onLeftClick(leftClickListener: ((HasClickListeners.ClickEvent<T>)->Unit)) {
    addClickListener(leftClickListener)
}
fun HasComponents.button(text: String? = null, icon: Component? = null, block: Button.() -> Unit = {})
        = init(Button(text, icon), block)
fun HasComponents.textField(label: String? = null, block: TextField.() -> Unit = {})
        = init(TextField(label), block)
fun HasComponents.text(text: String, block: Text.() -> Unit = {}) = init(Text(text), block)
