/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example.v8.uitest.example

import com.github.mvysny.karibudsl.v8.*
import com.github.mvysny.karibudsl.v8.ModifierKey.Alt
import com.github.mvysny.karibudsl.v8.ModifierKey.Ctrl
import com.vaadin.event.ShortcutAction.KeyCode.E
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.ClassResource
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

/**
 * @author Vaadin Ltd
 */
@AutoView
class ButtonsAndLinks : Composite(), View {
    private val root = verticalLayout {
        isSpacing = false
        title("Buttons")

        val testIcon = VaadinIcons.values().iterator()
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            button("Normal")
            button("Disabled") { isEnabled = false }
            button("Primary") { setPrimary() }
            button("Friendly") { styleName = ValoTheme.BUTTON_FRIENDLY }
            button("Danger") { styleName = ValoTheme.BUTTON_DANGER }
            button("Small") {
                styleName = ValoTheme.BUTTON_SMALL
                icon = testIcon.next()
            }
            button("Large") {
                styleName = ValoTheme.BUTTON_LARGE
                icon = testIcon.next()
            }
            button("Top") {
                styleName = ValoTheme.BUTTON_ICON_ALIGN_TOP
                icon = testIcon.next()
            }
            button("Image icon") { icon = ClassResource("document16.png") }
            button("Image icon") {
                styleName = ValoTheme.BUTTON_ICON_ALIGN_RIGHT
                icon = ClassResource("document32.png")
            }
            button("Photos") { icon = testIcon.next() }
            button {
                icon = testIcon.next()
                styleName = ValoTheme.BUTTON_ICON_ONLY
            }
            button("Borderless") {
                icon = testIcon.next()
                addStyleName(ValoTheme.BUTTON_BORDERLESS)
            }
            button("Borderless, colored") {
                icon = testIcon.next()
                addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED)
            }
            button("Quiet") {
                icon = testIcon.next()
                addStyleName(ValoTheme.BUTTON_QUIET)
            }
            button("Link style") {
                icon = testIcon.next()
                addStyleName(ValoTheme.BUTTON_LINK)
            }
            button("Icon on right") {
                icon = testIcon.next()
                addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT)
            }
            cssLayout {
                styleName = ValoTheme.LAYOUT_COMPONENT_GROUP
                button("One")
                button("Two")
                button("Three")
            }
            button("Tiny") { styleName = ValoTheme.BUTTON_TINY }
            button("Huge") { styleName = ValoTheme.BUTTON_HUGE }
            nativeButton("Native")
            button("Ctrl+Alt+E") {
                addGlobalShortcutListener(Ctrl + Alt + E) {
                    Notification.show("Ctrl+Alt+E pressed!")
                }
            }
        }
        label("Links") {
            styleName = ValoTheme.LABEL_H3
        }

        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            link("vaadin.com", "https://vaadin.com")
            link("Link with icon", "https://github.com/mvysny/karibu-dsl") {
                styleName = "color3"
                icon = testIcon.next()
            }
            link("Small", "https://vaadin.com") { styleName = ValoTheme.LINK_SMALL }
            link("Large", "https://vaadin.com") { styleName = ValoTheme.LINK_LARGE }
            link(null, "https://vaadin.com") {
                icon = testIcon.next()
                addStyleName(ValoTheme.LINK_LARGE)
            }
            link("Disabled", "https://vaadin.com") {
                icon = testIcon.next()
                isEnabled = false
            }
        }
    }
}
