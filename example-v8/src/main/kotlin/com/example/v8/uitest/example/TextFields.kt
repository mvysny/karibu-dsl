/*
 * Copyright 2000-2013 Vaadin Ltd.
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
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.ClassResource
import com.vaadin.server.UserError
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

@AutoView
class TextFields : Composite(), View {

    private val root = verticalLayout {
        isSpacing = false
        title("Text Fields")

        val testIcon = VaadinIcons.values().iterator()
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            textField("Normal") {
                placeholder = "First name"
                icon = testIcon.next()
            }
            textField("Custom color") {
                placeholder = "Email"
                addStyleName("color1")
            }
            textField("User Color") {
                placeholder = "Gender"
                addStyleName("color2")
            }
            textField("Themed") {
                placeholder = "Age"
                addStyleName("color3")
            }
            textField("Error", "Something’s wrong") {
                componentError = UserError("Fix it, now!")
            }
            textField("Error, borderless", "Something’s wrong") {
                componentError = UserError("Fix it, now!")
                addStyleName(ValoTheme.TEXTFIELD_BORDERLESS)
            }
            textField("Read-only", "Finnish") {
                placeholder = "Nationality"
                isReadOnly = true
            }
            textField("Small", "Field value") {
                addStyleName(ValoTheme.TEXTFIELD_SMALL)
            }
            textField("Large", "Field value") {
                addStyleName(ValoTheme.TEXTFIELD_LARGE)
                icon = ClassResource("document32.png")
            }
            textField("Icon inside") {
                placeholder = "Ooh, an icon"
                addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                icon = testIcon.next()
            }
            textField("Large, Icon inside") {
                placeholder = "Ooh, an icon"
                addStyleNames(ValoTheme.TEXTFIELD_LARGE, ValoTheme.TEXTFIELD_INLINE_ICON)
                icon = testIcon.next()
            }
            textField("Small, Icon inside") {
                placeholder = "Ooh, an icon"
                addStyleNames(ValoTheme.TEXTFIELD_SMALL, ValoTheme.TEXTFIELD_INLINE_ICON)
                icon = testIcon.next()
            }
            textField("16px supported by default") {
                placeholder = "Image icon"
                addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                icon = ClassResource("document16.png")
            }
            textField(value = "Font, no caption") {
                addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                icon = testIcon.next()
            }
            textField(value = "Image, no caption") {
                addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                icon = ClassResource("document16.png")
            }
            cssLayout {
                styleName = ValoTheme.LAYOUT_COMPONENT_GROUP
                val text = textField {
                    value = "Grouped with a button"
                    placeholder = "Grouped with a button"
                    addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                    icon = testIcon.next()
                    w = 260.px
                }
                button("Speak") {
                    onLeftClick {
                        if (text.value.isNotBlank()) tts(text.value.trim(), "UK English Female")
                    }
                }
            }
            textField("Borderless") {
                placeholder = "Write here…"
                addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                addStyleName(ValoTheme.TEXTFIELD_BORDERLESS)
                icon = testIcon.next()
            }

            textField("Right-aligned") {
                value = "1,234"
                addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT)
            }
            textField("Centered") {
                placeholder = "Guess what?"
                addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER)
            }

            passwordField("Password") {
                placeholder = "Secret words"
                addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
                icon = VaadinIcons.LOCK
            }
            passwordField("Password, right-aligned") {
                placeholder = "Secret words"
                addStyleNames(ValoTheme.TEXTFIELD_INLINE_ICON, ValoTheme.TEXTFIELD_ALIGN_RIGHT)
                icon = VaadinIcons.LOCK
            }
            passwordField("Password, centered") {
                placeholder = "Secret words"
                addStyleNames(ValoTheme.TEXTFIELD_INLINE_ICON, ValoTheme.TEXTFIELD_ALIGN_CENTER)
                icon = VaadinIcons.LOCK
            }
            textField("Tiny") {
                value = "Field value"
                addStyleName(ValoTheme.TEXTFIELD_TINY)
            }
            textField("Huge") {
                value = "Field value"
                addStyleName(ValoTheme.TEXTFIELD_HUGE)
            }
        }
        label("Text Areas") { styleName = ValoTheme.LABEL_H3 }
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            textArea("Normal") { placeholder = "Write your comment…" }
            textArea("Inline icon") {
                placeholder = "Inline icon not really working"
                addStyleName("inline-icon")
                icon = testIcon.next()
            }
            textArea("Custom color") {
                addStyleName("color1")
                placeholder = "Write your comment…"
            }
            textArea("Custom color, read-only") {
                addStyleName("color2")
                value = "Field value, spanning multiple lines of text"
                isReadOnly = true
            }
            textArea("Custom color") {
                addStyleName("color3")
                value = "Field value, spanning multiple lines of text"
            }
            textArea("Small") {
                addStyleName(ValoTheme.TEXTAREA_SMALL)
                placeholder = "Write your comment…"
            }
            textArea("Large") {
                addStyleName(ValoTheme.TEXTAREA_LARGE)
                placeholder = "Write your comment…"
            }
            textArea("Borderless") {
                addStyleName(ValoTheme.TEXTAREA_BORDERLESS)
                placeholder = "Write your comment…"
            }
            textArea("Right-aligned") {
                addStyleName(ValoTheme.TEXTAREA_ALIGN_RIGHT)
                value = "Field value, spanning multiple lines of text"
            }
            textArea("Centered") {
                addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER)
                value = "Field value, spanning multiple lines of text"
            }
            textArea("Tiny") {
                addStyleName(ValoTheme.TEXTAREA_TINY)
                placeholder = "Write your comment…"
            }
            textArea("Huge") {
                addStyleName(ValoTheme.TEXTAREA_HUGE)
                placeholder = "Write your comment…"
            }
            richTextArea { value = "<b>Some</b> <i>rich</i> content" }
            richTextArea("Read-only") {
                value = "<b>Some</b> <i>rich</i> content"
                isReadOnly = true
            }
        }
        label {
            html("Text-to-Speech powered by <a href='https://responsivevoice.org/'>ResponsiveVoice</a>")
        }
    }
}
