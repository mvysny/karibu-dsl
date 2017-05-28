package com.github.vok.karibudsl

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
fun <T : Component> (@VaadinDsl HasComponents).init(component: T, block: T.()->Unit = {}): T {
    addChild(component)
    component.block()
    return component
}

/**
 * Shows given html in this label.
 * @param html the html code to show.
 */
fun (@VaadinDsl Label).html(@Language("HTML") html: String?) {
    contentMode = ContentMode.HTML
    value = html
}

/**
 * Triggers given listener when the text field is focused and user presses the Enter key.
 * @param enterListener the listener to invoke when the user presses the Enter key.
 */
fun (@VaadinDsl AbstractTextField).onEnterPressed(enterListener: (AbstractTextField) -> Unit) {
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
 * Adds or removes given [style] from the component, depending on the value of the [isPresent] parameter.
 */
fun (@VaadinDsl Component).toggleStyleName(style: String, isPresent: Boolean) {
    if (isPresent) addStyleName(style) else removeStyleName(style)
}

/**
 * Returns a set of styles currently present on the component.
 */
val (@VaadinDsl Component).styleNames: Set<String> get() = styleName.split(' ').filterNotBlank().toSet()

/**
 * Checks whether the component has given [style].
 * @param style if contains a space, this is considered to be a list of styles. In such case, all styles must be present on the component.
 */
fun (@VaadinDsl Component).hasStyleName(style: String): Boolean {
    if (style.contains(' ')) return style.split(' ').filterNotBlank().all { hasStyleName(style) }
    return styleNames.contains(style)
}

/**
 * Adds multiple [styles]. Individual items in the styles array may contain spaces.
 */
fun (@VaadinDsl Component).addStyleNames(vararg styles: String) = styles.forEach { addStyleName(it) }

/**
 * Configures this button as primary. Beware - all buttons marked primary using this function, attached to the current UI
 * or Window will be pressed on Enter key press.
 */
fun (@VaadinDsl Button).setPrimary() {
    addStyleName(ValoTheme.BUTTON_PRIMARY)
    setClickShortcut(ShortcutAction.KeyCode.ENTER)
}

/**
 * Directs the browser to go back.
 */
fun goBack() = Page.getCurrent().javaScript.execute("window.history.back();")

fun navigateBack() = goBack()
