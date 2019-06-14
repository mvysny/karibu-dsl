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
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.server.PWA
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
@PWA(name = "Beverage Buddy", shortName = "BeverageBuddy")
class MainLayout : AppLayout() {
    init {
        isDrawerOpened = false
        navbar {
            drawerToggle()
            h3("Beverage Buddy")
        }
        drawer {
            div {
                routerLink(VaadinIcon.LIST, "Reviews", ReviewsList::class)
            }
            div {
                routerLink(VaadinIcon.ARCHIVES, "Categories", CategoriesList::class)
            }
        }
    }

    override fun showRouterLayoutContent(content: HasElement) {
        super.showRouterLayoutContent(content)
        content.element.classList.add("main-layout")
        isDrawerOpened = false
    }
}
