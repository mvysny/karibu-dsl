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
import com.vaadin.ui.HasComponents
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class Panels : VerticalLayout(), View {
    init {
        isSpacing = false
        title("Panels & Layout panels")

        val testIcon = VaadinIcons.values().iterator()
        horizontalLayout {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            panel("Normal") {
                icon = testIcon.next()
                demoContent()
            }
            panel("Sized") {
                icon = testIcon.next()
                w = 10.em; h = 250.px
                demoContent()
            }
            panel("Custom Caption") {
                icon = testIcon.next()
                addStyleName("color1")
                demoContent()
            }
            panel("Custom Caption") {
                icon = testIcon.next()
                addStyleName("color2")
                demoContent()
            }
            panel("Custom Caption") {
                icon = testIcon.next()
                addStyleName("color3")
                demoContent()
            }
            panel("Borderless style") {
                icon = testIcon.next()
                addStyleName(ValoTheme.PANEL_BORDERLESS)
                demoContent()
            }
            panel("Borderless + scroll divider") {
                icon = testIcon.next()
                addStyleNames(ValoTheme.PANEL_BORDERLESS, ValoTheme.PANEL_SCROLL_INDICATOR)
                demoContentScroll()
                h = 17.em
            }
            panel("Well style") {
                icon = testIcon.next()
                addStyleName(ValoTheme.PANEL_WELL)
                demoContent()
            }
            cssLayout("Panel style layout") {
                icon = testIcon.next()
                addStyleName(ValoTheme.LAYOUT_CARD)
                demoContent()
            }
            cssLayout {
                addStyleName(ValoTheme.LAYOUT_CARD); w = 14.em
                horizontalLayout {
                    isSpacing = false
                    addStyleName("v-panel-caption")
                    w = fillParent
                    label("Panel style layout") {
                        expandRatio = 1f
                    }
                    button {
                        icon = VaadinIcons.PENCIL
                        addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED, ValoTheme.BUTTON_SMALL, ValoTheme.BUTTON_ICON_ONLY)
                    }
                    menuBar {
                        addStyleNames(ValoTheme.MENUBAR_BORDERLESS, ValoTheme.MENUBAR_SMALL)
                        item("", VaadinIcons.CHEVRON_DOWN, null) {
                            styleName = "icon-only"
                            item("Settings")
                            item("Preferences")
                            addSeparator()
                            item("Sign Out")
                        }
                    }
                }
                demoContent()
            }
            cssLayout("Well style layout") {
                icon = testIcon.next()
                addStyleName(ValoTheme.LAYOUT_WELL)
                demoContent()
            }
        }
    }

    private fun HasComponents.demoContent() = verticalLayout {
        setSizeFull()
        label("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.") {
            w = 10.em
        }
        button("Button") {
            setSizeFull()
        }
    }

    private fun HasComponents.demoContentScroll() = verticalLayout {
        label("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio. Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.") {
            w = 10.em
        }
        button("Button")
    }

    override fun enter(event: ViewChangeEvent) {
    }
}
