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
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class NativeSelects : VerticalLayout(), View {
    init {
        isSpacing = false
        title("Selects")

        horizontalLayout {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            nativeSelect<String>("Drop Down Select") {
                setItems((1..10).map { "Option $it" })
            }
            listSelect<String>("List Select") {
                setItems((1..10).map { "Option $it" })
            }
            twinColSelect<String>("TwinCol Select") {
                leftColumnCaption = "Left Column"
                rightColumnCaption = "Right Column"
                setItems((1..10).map { "Option $it" })
            }
            twinColSelect<String>("Sized TwinCol Select") {
                leftColumnCaption = "Left Column"
                rightColumnCaption = "Right Column"
                w = 280.px; h = 200.px
                setItems((1..10).map { "Option $it" })
            }
        }
    }

    override fun enter(event: ViewChangeEvent) {
    }
}
