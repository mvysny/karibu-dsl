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
package com.github.vok.karibudsl.example

import com.github.vok.karibudsl.*
import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.ClassResource
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class CheckBoxes : VerticalLayout(), View {
    init {
        isSpacing = false
        title("Check Boxes")

        val testIcon = VaadinIcons.values().iterator()
        horizontalLayout {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            checkBox("Checked", true)
            checkBox("Checked, explicit width, so that the caption should wrap", true) {
                w = 150.px
            }
            checkBox("Not checked")
            checkBox(null, true) {
                description = "No caption"
            }
            checkBox("Custom color", true) {
                addStyleName("color1")
            }
            checkBox("Custom color", true) {
                addStyleName("color2")
                icon = testIcon.next()
            }
            checkBox("With Icon", true) {
                icon = testIcon.next()
            }
            checkBox {
                icon = ClassResource("document32.png")
            }
            checkBox("Small", true) {
                addStyleName(ValoTheme.CHECKBOX_SMALL)
            }
            checkBox("Large", true) {
                addStyleName(ValoTheme.CHECKBOX_LARGE)
            }
            checkBox("Disabled", true) {
                isEnabled = false
                icon = testIcon.next()
            }
            checkBox("Readonly", true) {
                isReadOnly = true
                icon = testIcon.next()
            }
        }

        label("Option Groups") {
            styleName = ValoTheme.LABEL_H2
        }
        horizontalLayout {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            radioButtonGroup<String>("Choose one, explicit width") {
                w = 200.px
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two, with a longer caption that should wrap when the components width is explicitly set." to testIcon.next(),
                        "Option Three" to ClassResource("document32.png"))
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            checkBoxGroup<String>("Choose many, explicit width") {
                w = 200.px
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two, with a longer caption that should wrap when the components width is explicitly set." to testIcon.next(),
                        "Option Three" to ClassResource("document32.png"))
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            radioButtonGroup<String>("Choose one, small") {
                addStyleName(ValoTheme.OPTIONGROUP_SMALL)
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two" to testIcon.next(),
                        "Option Three" to ClassResource("document32.png"))
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            checkBoxGroup<String>("Choose many, small") {
                addStyleName(ValoTheme.OPTIONGROUP_SMALL)
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two" to testIcon.next(),
                        "Option Three" to ClassResource("document32.png"))
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            radioButtonGroup<String>("Choose one, large") {
                addStyleName(ValoTheme.OPTIONGROUP_LARGE)
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two" to testIcon.next(),
                        "Option Three" to ClassResource("document32.png"))
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            checkBoxGroup<String>("Choose many, large") {
                addStyleName(ValoTheme.OPTIONGROUP_LARGE)
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two" to testIcon.next(),
                        "Option Three" to ClassResource("document32.png"))
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            radioButtonGroup<String>("Horizontal items") {
                addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL)
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two, with a longer caption" to testIcon.next(),
                        "Option Three" to testIcon.next())
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            radioButtonGroup<String>("Horizontal items, explicit width") {
                addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL); w = 500.px
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two, with a longer caption" to testIcon.next(),
                        "Option Three" to testIcon.next())
                setItems(options.keys)
                setItemIconGenerator { options[it] }
            }
            radioButtonGroup<String>("Disabled items") {
                val options = mapOf("Option One" to testIcon.next(),
                        "Option Two" to testIcon.next(),
                        "Option Three" to ClassResource("document32.png"))
                setItems(options.keys)
                setItemIconGenerator { options[it] }
                isEnabled = false
            }
        }
    }

    override fun enter(event: ViewChangeEvent) {
    }
}
