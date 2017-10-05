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

import com.vaadin.router.RouterLayout
import com.vaadin.router.RouterLink
import com.vaadin.router.event.AfterNavigationEvent
import com.vaadin.router.event.AfterNavigationListener
import com.vaadin.ui.Text
import com.vaadin.ui.common.HtmlImport
import com.vaadin.ui.html.Div
import com.vaadin.ui.html.H2
import com.vaadin.ui.icon.Icon
import com.vaadin.ui.icon.VaadinIcons

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@HtmlImport("frontend://styles.html")
class MainLayout : Div(), RouterLayout, AfterNavigationListener {
    private val categories: RouterLink
    private val reviews: RouterLink

    init {
        val title = H2("Beverage Buddy")
        title.addClassName("main-layout__title")

        reviews = RouterLink(null, ReviewsList::class.java)
        reviews.add(Icon(VaadinIcons.LIST), Text("Reviews"))
        reviews.addClassName("main-layout__nav-item")

        categories = RouterLink(null, CategoriesList::class.java)
        categories.add(Icon(VaadinIcons.ARCHIVES), Text("Categories"))
        categories.addClassName("main-layout__nav-item")

        val navigation = Div(reviews, categories)
        navigation.addClassName("main-layout__nav")

        val header = Div(title, navigation)
        header.addClassName("main-layout__header")
        add(header)

        addClassName("main-layout")
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

    companion object {

        private val ACTIVE_ITEM_STYLE = "main-layout__nav-item--selected"
    }
}
