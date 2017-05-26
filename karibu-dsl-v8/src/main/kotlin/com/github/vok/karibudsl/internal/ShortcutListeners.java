package com.github.vok.karibudsl.internal;

import com.vaadin.event.ShortcutListener;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

/**
 * @author mvy
 */
public class ShortcutListeners {
	public static ShortcutListener listener(int keyCode, @NotNull int[] modifierKeys, final @NotNull Function0<?> action) {
		return new ShortcutListener(null, keyCode, modifierKeys) {
			@Override
			public void handleAction(Object sender, Object target) {
				action.invoke();
			}
		};
	}
}
