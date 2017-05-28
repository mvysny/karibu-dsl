@file:Suppress("DEPRECATION")

package com.github.vok.karibudsl.v7

import com.github.vok.karibudsl.init
import com.vaadin.ui.HasComponents
import com.vaadin.v7.ui.HorizontalLayout
import com.vaadin.v7.ui.VerticalLayout

@Deprecated("Use VerticalLayout from Vaadin 8")
fun HasComponents.verticalLayout7(block: VerticalLayout.()->Unit = {}) = init(VerticalLayout(), block)

@Deprecated("Use HorizontalLayout from Vaadin 8")
fun HasComponents.horizontalLayout7(block: HorizontalLayout.()->Unit = {}) = init(HorizontalLayout(), block)
