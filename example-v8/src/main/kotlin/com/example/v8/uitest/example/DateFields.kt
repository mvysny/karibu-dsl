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
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.UserError
import com.vaadin.shared.ui.datefield.DateResolution
import com.vaadin.ui.Composite
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import java.time.LocalDate
import java.util.*

@AutoView
class DateFields : Composite(), View {
    private val root = verticalLayout {
        isSpacing = false
        title("Date Fields")

        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            dateField("Default resolution") {
                value = LocalDate.now()
            }
            dateField("Error") {
                value = LocalDate.now()
                componentError = UserError("Fix it, now!")
            }
            dateField("Error, borderless") {
                value = LocalDate.now()
                componentError = UserError("Fix it, now!")
                addStyleName(ValoTheme.DATEFIELD_BORDERLESS)
            }
            cssLayout {
                caption = "Grouped with a Button"; addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP)
                val date2 = dateField()
                button("Today", { date2.value = LocalDate.now() })
            }
            dateField("Default resolution, explicit size") {
                value = LocalDate.now()
                w = 260.px; h = 60.px
            }
            dateField("Day resolution") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
            }
            dateField("Month resolution") {
                value = LocalDate.now()
                resolution = DateResolution.MONTH
            }
            dateField("Year resolution") {
                value = LocalDate.now()
                resolution = DateResolution.YEAR
            }
            dateField("Custom color") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName("color1")
            }
            dateField("Custom color") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName("color2")
            }
            dateField("Custom color") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName("color3")
            }
            dateField("Small") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName(ValoTheme.DATEFIELD_SMALL)
            }
            dateField("Large") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName(ValoTheme.DATEFIELD_LARGE)
            }
            dateField("Borderless") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName(ValoTheme.DATEFIELD_BORDERLESS)
            }
            dateField("Week numbers") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                locale = Locale("fi", "fi")
                isShowISOWeekNumbers = true
            }
            dateField("Custom format") {
                value = LocalDate.now()
                dateFormat = "E dd/MM/yyyy"
            }
            dateField("Tiny") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName(ValoTheme.DATEFIELD_TINY)
            }
            dateField("Huge") {
                value = LocalDate.now()
                resolution = DateResolution.DAY
                addStyleName(ValoTheme.DATEFIELD_HUGE)
            }
            inlineDateField("Date picker") {
                value = LocalDate.now()
                rangeStart = LocalDate.now()
                rangeEnd = LocalDate.now().plusMonths(1)
            }
            inlineDateField("Date picker with week numbers") {
                value = LocalDate.now()
                locale = Locale("fi", "fi")
                isShowISOWeekNumbers = true
            }
        }
    }
}
