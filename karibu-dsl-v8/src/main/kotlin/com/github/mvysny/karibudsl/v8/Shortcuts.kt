package com.github.mvysny.karibudsl.v8

import com.vaadin.event.ShortcutAction
import com.vaadin.event.ShortcutListener
import com.vaadin.shared.Registration
import com.vaadin.ui.AbstractComponent
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import java.io.Serializable

public enum class ModifierKey(public val value: Int) {
    Shift(ShortcutAction.ModifierKey.SHIFT),
    Ctrl(ShortcutAction.ModifierKey.CTRL),
    Alt(ShortcutAction.ModifierKey.ALT),
    Meta(ShortcutAction.ModifierKey.META);

    public infix operator fun plus(other: ModifierKey): Set<ModifierKey> = setOf(this, other)
    public infix operator fun plus(key: Int): KeyShortcut = KeyShortcut(key, setOf(this))
}

public infix operator fun Set<ModifierKey>.plus(key: Int): KeyShortcut = KeyShortcut(key, this)

/**
 * Denotes a keyboard shortcut, such as [ModifierKey.Ctrl]+[ModifierKey.Alt]+[ShortcutAction.KeyCode.C]`. When properly imported, this
 * becomes `Ctrl+Alt+C` ;)
 * @property keyCode one of the ShortcutAction.KeyCode.* constants.
 */
public data class KeyShortcut(val keyCode: Int, val modifierKeys: Set<ModifierKey> = setOf()) : Serializable {
    val vaadinModifiers: IntArray = modifierKeys.map { it.value }.toIntArray()
}

private fun shortcutListener(shortcut: KeyShortcut, block: () -> Unit): ShortcutListener =
        object : ShortcutListener(null, null, shortcut.keyCode, shortcut.vaadinModifiers) {
            override fun handleAction(sender: Any?, target: Any?) {
                block()
            }
        }

/**
 * Adds global shortcut listener. The listener is not added directly for this component - instead it is global, up to the nearest parent
 * Panel, UI or Window.
 * @param shortcut the shortcut, e.g. `ModifierKey.Ctrl + ModifierKey.Alt + ShortcutAction.KeyCode.C`.
 * When you properly import `ModifierKey.*` and `ShortcutAction.KeyCode.*`, this expression can be written as `Ctrl + Alt + C`
 */
public fun (@VaadinDsl Component).addGlobalShortcutListener(shortcut: KeyShortcut, action: () -> Unit): Registration {
    val listener = shortcutListener(shortcut, action)
    return (this as AbstractComponent).addShortcutListener(listener)
}

/**
 * Adds global shortcut listener. The listener is not added directly for this component - instead it is global, up to the nearest parent
 * Panel, UI or Window.
 * @param keyCode the key code, e.g. [ShortcutAction.KeyCode.C]
 */
public fun (@VaadinDsl Component).addGlobalShortcutListener(keyCode: Int, action: () -> Unit): Registration =
        addGlobalShortcutListener(KeyShortcut(keyCode), action)

/**
 * Makes it possible to invoke a click on this button by pressing the given
 * [ShortcutAction.KeyCode] and (optional) [ModifierKey]s.
 * The shortcut is global (bound to the containing Window).
 *
 * Example of shortcut expression: `ModifierKey.Ctrl + ModifierKey.Alt + ShortcutAction.KeyCode.C`.
 * When you properly import `ModifierKey.*` and `ShortcutAction.KeyCode.*`, this expression can be written as `Ctrl + Alt + C`.
 */
public var (@VaadinDsl Button).clickShortcut: KeyShortcut
    get() = throw RuntimeException("Property is write-only")
    set(shortcut) = setClickShortcut(shortcut.keyCode, *shortcut.vaadinModifiers)
