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
import com.vaadin.server.UserError
import com.vaadin.ui.Composite
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class ComboBoxes : Composite(), View {
    private val root = verticalLayout {
        isSpacing = false
        title("Combo Boxes")

        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            comboBox<Int>("Normal") {
                placeholder = "You can type here"
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                isEmptySelectionAllowed = false
                value = 0
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
            cssLayout {
                caption = "Grouped with a Button"; styleName = ValoTheme.LAYOUT_COMPONENT_GROUP
                comboBox<Int> {
                    w = 240.px
                    placeholder = "You can type here"
                    setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                    itemCaptionGenerator = StringGenerator
                    isEmptySelectionAllowed = false
                    value = 0
                    setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
                }
                button("Do It")
            }
            comboBox<String>("Explicit size") {
                placeholder = "You can type here"; w = 260.px; h = 60.px
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, listOf("Option One", "Option Two", "Option Three"))
            }
            comboBox<String>("No text input allowed") {
                placeholder = "You can click here"
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, listOf("Option One", "Option Two", "Option Three"))
                isTextInputAllowed = false
                isEmptySelectionAllowed = false
                value = "Option One"
            }
            comboBox<String>("Error") {
                placeholder = "You can type here"
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, listOf("Option One", "Option Two", "Option Three"))
                isEmptySelectionAllowed = false
                value = "Option One"
                setComponentError(UserError("Fix it, now!"))
            }
            comboBox<String>("Error, borderless") {
                placeholder = "You can type here"; styleName = ValoTheme.COMBOBOX_BORDERLESS
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, listOf("Option One", "Option Two", "Option Three"))
                isEmptySelectionAllowed = false
                value = "Option One"
                setComponentError(UserError("Fix it, now!"))
            }
            comboBox<String>("Disabled") {
                placeholder = "You can't type here"
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, listOf("Option One", "Option Two", "Option Three"))
                isEnabled = false
            }
            comboBox<Int>("Custom color") {
                placeholder = "You can type here"; styleName = "color1"
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                isEmptySelectionAllowed = false
                value = 0
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
            comboBox<Int>("Custom color") {
                placeholder = "You can type here"; styleName = "color2"
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                isEmptySelectionAllowed = false
                value = 0
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
            comboBox<Int>("Custom color") {
                placeholder = "You can type here"; styleName = "color3"
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                isEmptySelectionAllowed = false
                value = 0
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
            comboBox<Int>("Small") {
                placeholder = "You can type here"; styleName = ValoTheme.COMBOBOX_SMALL
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                isEmptySelectionAllowed = false
                value = 0
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
            comboBox<Int>("Large") {
                placeholder = "You can type here"; styleName = ValoTheme.COMBOBOX_LARGE
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                isEmptySelectionAllowed = false
                value = 0
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
            comboBox<String>("Borderless") {
                placeholder = "You can type here"; styleName = ValoTheme.COMBOBOX_BORDERLESS
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, listOf("Option One", "Option Two", "Option Three"))
            }
            comboBox<Int>("Tiny") {
                placeholder = "You can type here"; styleName = ValoTheme.COMBOBOX_TINY
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
            comboBox<Int>("Huge") {
                placeholder = "You can type here"; styleName = ValoTheme.COMBOBOX_HUGE
                setItems({ caption: String, text: String -> caption.contains(text, ignoreCase = true)}, (0..199).toList())
                itemCaptionGenerator = StringGenerator
                setItemIconGenerator { VaadinIcons.values()[it % VaadinIcons.values().size] }
            }
        }
    }
}
