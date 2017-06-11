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
        title("Welcome To The Karibu-DSL Demo")

        label {
            w = fillParent
            html("""Karibu-DSL offers the following:
            <ul><li>Provides Vaadin DSL builder support, allowing you to build your Vaadin UIs in Kotlin declarative, statically compiler-checked code</li>
            <li>Provides auto-discovery of your Views</li></ul>
            And more.""")
        }
        label {
            w = fillParent
            html("""<strong>Vaadin version:</strong> ${Version.getFullVersion()}<br/><strong>Kotlin version:</strong> ${KotlinVersion.CURRENT}""")
        }
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}
