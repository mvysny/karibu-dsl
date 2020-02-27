/*
 * Copyright 2000-2017 Vaadin Ltd.
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
package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.server.InitialPageSettings
import com.vaadin.flow.server.PageConfigurator
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import com.vaadin.starter.beveragebuddy.ui.categories.CategoriesList
import com.vaadin.starter.beveragebuddy.ui.reviews.ReviewsList

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@BodySize(width = "100vw", height = "100vh")
@CssImport("frontend://styles/shared-styles.css")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Theme(Lumo::class)
class MainLayout : KComposite(), RouterLayout, PageConfigurator {

    private val root = ui {
        verticalLayout {
            addClassName("main-layout"); setSizeFull(); isPadding = false
            content { align(stretch, top) }
            div {
                // header
                addClassName("main-layout__header")
                h2("Beverage Buddy") {
                    addClassName("main-layout__title")
                }
                div {
                    // navigation
                    addClassName("main-layout__nav")
                    routerLink(VaadinIcon.LIST, "Reviews", ReviewsList::class) {
                        addClassName("main-layout__nav-item")
                        highlightCondition = HighlightConditions.sameLocation()
                    }
                    routerLink(VaadinIcon.ARCHIVES, "Categories", CategoriesList::class) {
                        addClassName("main-layout__nav-item")
                        highlightCondition = HighlightConditions.sameLocation()
                    }
                }
            }
        }
    }

    override fun showRouterLayoutContent(content: HasElement) {
        root.add(content as Component)
        content.isExpand = true
    }

    override fun configurePage(settings: InitialPageSettings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes")
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black")
    }
}
