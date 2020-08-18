package com.github.mvysny.karibudsl.v8

import com.vaadin.event.ShortcutAction
import com.vaadin.event.ShortcutListener
import com.vaadin.server.Page
import com.vaadin.shared.Registration
import com.vaadin.shared.ui.ContentMode
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import org.intellij.lang.annotations.Language

/**
 * When introducing extensions for your custom components, just call this in your method. For example:
 *
 * `fun HasComponents.shinyComponent(caption: String? = null, block: ShinyComponent.()->Unit = {}) = init(ShinyComponent(caption), block)`
 *
 * Adds [component] to receiver, see [addChild] for details.
 *
 * @param component the component to attach
 * @param block optional block to run over the component, allowing you to add children to the [component]
 */
@VaadinDsl
public fun <T : Component> (@VaadinDsl HasComponents).init(component: T, block: T.()->Unit = {}): T {
    addChild(component)
    component.block()
    return component
}

/**
 * Shows given html in this label.
 * @param html the html code to show.
 */
public fun (@VaadinDsl Label).html(@Language("HTML") html: String?) {
    contentMode = ContentMode.HTML
    value = html
}

/**
 * Triggers given listener when the text field is focused and user presses the Enter key.
 * @param enterListener the listener to invoke when the user presses the Enter key.
 */
public fun (@VaadinDsl AbstractTextField).onEnterPressed(enterListener: (AbstractTextField) -> Unit) {
    val enterShortCut = object : ShortcutListener("EnterOnTextAreaShorcut", null, KeyCode.ENTER) {
        override fun handleAction(sender: Any, target: Any) {
            enterListener(this@onEnterPressed)
        }
    }
    var r: Registration? = null
    addFocusListener { r?.remove(); r = addShortcutListener(enterShortCut) }
    addBlurListener { r?.remove() }
}

/**
 * Configures this button as primary. Beware - all buttons marked primary using this function, attached to the current UI
 * or Window will be pressed on Enter key press.
 */
public fun (@VaadinDsl Button).setPrimary() {
    addStyleName(ValoTheme.BUTTON_PRIMARY)
    setClickShortcut(ShortcutAction.KeyCode.ENTER)
}

/**
 * Directs the browser to go back.
 */
public fun goBack() {
    Page.getCurrent().javaScript.execute("window.history.back();")
}

/**
 * Alias for [goBack].
 */
public fun navigateBack() {
    goBack()
}
