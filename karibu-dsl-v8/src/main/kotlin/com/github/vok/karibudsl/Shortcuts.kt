package com.github.vok.karibudsl

import com.vaadin.event.ShortcutAction
import com.vaadin.event.ShortcutListener
import com.vaadin.ui.AbstractComponent
import com.vaadin.ui.Button
import com.vaadin.ui.Component

enum class ModifierKey(val value: Int) {
    Shift(ShortcutAction.ModifierKey.SHIFT),
    Ctrl(ShortcutAction.ModifierKey.CTRL),
    Alt(ShortcutAction.ModifierKey.ALT),
    Meta(ShortcutAction.ModifierKey.META);

    infix operator fun plus(other: ModifierKey) = setOf(this, other)
    infix operator fun plus(key: Int) = KeyShortcut(key, setOf(this))
}

infix operator fun Set<ModifierKey>.plus(key: Int) = KeyShortcut(key, this)

/**
 * Denotes a keyboard shortcut, such as [ModifierKey.Ctrl]+[ModifierKey.Alt]+[ShortcutAction.KeyCode.C]`. When properly imported, this
 * becomes `Ctrl+Alt+C` ;)
 * @property keyCode one of the ShortcutAction.KeyCode.* constants.
 */
data class KeyShortcut(val keyCode: Int, val modifierKeys: Set<ModifierKey> = setOf()) {
    val vaadinModifiers: IntArray = modifierKeys.map { it.value }.toIntArray()
}

private fun shortcutListener(shortcut: KeyShortcut, block: () -> Unit): ShortcutListener =
        object : ShortcutListener(null, null, shortcut.keyCode, shortcut.vaadinModifiers) {
            override fun handleAction(sender: Any?, target: Any?) {
                block()
            }
        }

private fun shortcutListener(shortcut: Int, block: () -> Unit) = shortcutListener(KeyShortcut(shortcut), block)

/**
 * Adds global shortcut listener. The listener is not added directly for this component - instead it is global, up to the nearest parent
 * Panel, UI or Window.
 * @param shortcut the shortcut, e.g. `Ctrl + Alt + C`
 */
fun (@VaadinDsl Component).addGlobalShortcutListener(shortcut: KeyShortcut, action: () -> Unit): ShortcutListener {
    val listener = shortcutListener(shortcut, action)
    (this as AbstractComponent).addShortcutListener(listener)
    return listener
}

/**
 * Adds global shortcut listener. The listener is not added directly for this component - instead it is global, up to the nearest parent
 * Panel, UI or Window.
 * @param keyCode the key code, e.g. [ShortcutAction.KeyCode.C]
 */
fun (@VaadinDsl Component).addGlobalShortcutListener(keyCode: Int, action: () -> Unit) = addGlobalShortcutListener(KeyShortcut(keyCode), action)

/**
 * Makes it possible to invoke a click on this button by pressing the given
 * [ShortcutAction.KeyCode] and (optional) [ModifierKey]s.
 * The shortcut is global (bound to the containing Window).
 *
 * Example of shortcut expression: `Ctrl + Alt + C`
 */
var (@VaadinDsl Button).clickShortcut: KeyShortcut
    get() = throw RuntimeException("Property is write-only")
    set(shortcut) = setClickShortcut(shortcut.keyCode, *shortcut.vaadinModifiers)
