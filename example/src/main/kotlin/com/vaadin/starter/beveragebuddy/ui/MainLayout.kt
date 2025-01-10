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
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLayout
import com.vaadin.starter.beveragebuddy.ui.categories.CategoriesList
import com.vaadin.starter.beveragebuddy.ui.categories.CategoriesListLit
import com.vaadin.starter.beveragebuddy.ui.reviews.ReviewsList

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
class MainLayout : KComposite(), RouterLayout {

    private val root: VerticalLayout = ui {
        verticalLayout(false, false, classNames = "main-layout") {
            setSizeFull(); content { align(stretch, top) }
            div("main-layout__header") { // header
                h2("Beverage Buddy") {
                    addClassName("main-layout__title")
                }
                div("main-layout__nav") { // navigation
                    routerLink(VaadinIcon.LIST, "Reviews", ReviewsList::class) {
                        addClassName("main-layout__nav-item")
                        highlightCondition = HighlightConditions.sameLocation()
                    }
                    routerLink(VaadinIcon.ARCHIVES, "Categories", CategoriesList::class) {
                        addClassName("main-layout__nav-item")
                        highlightCondition = HighlightConditions.sameLocation()
                    }
                    routerLink(VaadinIcon.ARCHIVES, "Categories-lit", CategoriesListLit::class) {
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
}
