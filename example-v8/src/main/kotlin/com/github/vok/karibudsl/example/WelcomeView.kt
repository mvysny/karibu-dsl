package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.shared.Version
import com.vaadin.ui.VerticalLayout

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
        setSizeFull(); isMargin = true
        label {
            html("""<h3>Karibu-DSL</h3>Welcome to the Karibu-DSL demo. Karibu-DSL offers the following:
            <ul><li>Provides Vaadin DSL builder support, allowing you to build your Vaadin UIs in Kotlin declarative, statically compiler-checked code</li>
            <li>Provides auto-discovery of your Views</li></ul>
            And more.""")
        }
        label("Vaadin version ${Version.getFullVersion()}")
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}
