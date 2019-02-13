package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.*
import com.vaadin.flow.server.Command
import java.io.Serializable

enum class ModifierKey(private val hack: Int) {
    Shift(KeyModifier.SHIFT.ordinal),
    Ctrl(KeyModifier.CONTROL.ordinal),
    Alt(KeyModifier.ALT.ordinal),
    AltGr(KeyModifier.ALT_GRAPH.ordinal),
    Meta(KeyModifier.META.ordinal);

    infix operator fun plus(other: ModifierKey) = setOf(this, other)
    infix operator fun plus(key: Key) = KeyShortcut(key, setOf(this))

    val vaadin: KeyModifier get() = KeyModifier.values()[hack]  // workaround for https://github.com/vaadin/flow/issues/5051
}

infix operator fun Set<ModifierKey>.plus(key: Key) = KeyShortcut(key, this)

/**
 * Denotes a keyboard shortcut, such as [ModifierKey.Ctrl]+[ModifierKey.Alt]+[Key.KEY_C]`. When properly imported, this
 * becomes `Ctrl+Alt+KEY_C` ;)
 */
data class KeyShortcut(val key: Key, val modifierKeys: Set<ModifierKey> = setOf()) : Serializable {
    val vaadinModifiers: Array<KeyModifier> = modifierKeys.map { it.vaadin }.toTypedArray()
}

/**
 * Allows you to [add click shortcuts][ClickNotifier.addClickShortcut] such as `Ctrl+Alt+KEY_C`. See [KeyShortcut] for more details.
 */
fun ClickNotifier<*>.addClickShortcut(shortcut: KeyShortcut): ShortcutRegistration = addClickShortcut(shortcut.key, *shortcut.vaadinModifiers)

/**
 * Allows you to [add focus shortcuts][Focusable.addFocusShortcut] such as `Ctrl+Alt+KEY_C`. See [KeyShortcut] for more details.
 */
fun Focusable<*>.addFocusShortcut(shortcut: KeyShortcut): ShortcutRegistration = addFocusShortcut(shortcut.key, *shortcut.vaadinModifiers)

/**
 * Attaches a keyboard shortcut to given component receiver. The keyboard shortcut is only active when the component is visible
 * and attached to the UI. Ideal targets are therefore: views (for creating a view-wide shortcut), modal dialogs.
 */
fun Component.addShortcut(shortcut: KeyShortcut, block: ()->Unit): ShortcutRegistration =
        Shortcuts.addShortcutListener(this, Command { block() }, shortcut.key, *shortcut.vaadinModifiers)
