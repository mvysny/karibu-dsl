package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.shared.Version
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

/**
 * This is the root (or main) view. MyUI initially shows view whose name is "" (an empty string).
 * @author mvy
 */
@AutoView("")
class WelcomeView: VerticalLayout(), View {

    companion object {
        fun navigateTo() = navigateToView<WelcomeView>()
    }

    init {
        isMargin = true
        label("Welcome To The Karibu-DSL Demo") {
            addStyleNames(ValoTheme.LABEL_COLORED, ValoTheme.LABEL_H2)
        }
        label {
            html("""Karibu-DSL offers the following:
            <ul><li>Provides Vaadin DSL builder support, allowing you to build your Vaadin UIs in Kotlin declarative, statically compiler-checked code</li>
            <li>Provides auto-discovery of your Views</li></ul>
            And more.""")
        }
        label("Vaadin version ${Version.getFullVersion()}")
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}
