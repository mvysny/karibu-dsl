package com.github.vok.karibudsl.flow

import com.vaadin.ui.button.Button
import com.vaadin.ui.common.HasClickListeners
import com.vaadin.ui.common.HasComponents

fun (@VaadinDsl HasComponents).button(caption: String? = null, block: (@VaadinDsl Button).() -> Unit = {})
        = init(Button(caption), block)

fun (@VaadinDsl Button).onLeftClick(leftClickListener: ((HasClickListeners.ClickEvent<Button>)->Unit)) {
    addClickListener(leftClickListener)
}
