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

import com.github.mvysny.karibudsl.v10.div
import com.github.mvysny.karibudsl.v10.h2
import com.github.mvysny.karibudsl.v10.routerLink
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.server.InitialPageSettings
import com.vaadin.flow.server.PageConfigurator
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Theme(Lumo::class)
class MainLayout : VerticalLayout(), RouterLayout, AfterNavigationObserver, PageConfigurator {
    private lateinit var categories: RouterLink
    private lateinit var reviews: RouterLink

    init {
        addClassName("main-layout")
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.STRETCH
        div { // header
            addClassName("main-layout__header")
            h2("Beverage Buddy") {
                addClassName("main-layout__title")
            }
            div { // navigation
                addClassName("main-layout__nav")
                reviews = routerLink(VaadinIcon.LIST, "Reviews", ReviewsList::class) {
                    addClassName("main-layout__nav-item")
                }
                categories = routerLink(VaadinIcon.ARCHIVES, "Categories", CategoriesList::class) {
                    addClassName("main-layout__nav-item")
                }
            }
        }
    }

    override fun afterNavigation(event: AfterNavigationEvent) {
        // updating the active menu item based on if either of views is active
        // (note that this is triggered even for the error view)
        val segment = event.location.firstSegment
        val reviewsActive = segment == reviews.href
        val categoriesActive = segment == categories.href

        reviews.setClassName(ACTIVE_ITEM_STYLE, reviewsActive)
        categories.setClassName(ACTIVE_ITEM_STYLE, categoriesActive)
    }

    override fun configurePage(settings: InitialPageSettings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes")
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black")
    }

    companion object {
        private val ACTIVE_ITEM_STYLE = "main-layout__nav-item--selected"
    }
}
