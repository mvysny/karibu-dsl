/*
 * Copyright 2000-2016 Vaadin Ltd.
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
import com.vaadin.ui.*
import com.vaadin.ui.MenuBar.MenuItem
import com.vaadin.ui.themes.ValoTheme

@AutoView
class MenuBars : VerticalLayout(), View {
    init {
        setMargin(true)
        isSpacing = true

        title("Menu Bars")
        sampleMenuBar {
            caption = "Normal style"
        }
        sampleMenuBar {
            caption = "Small style"
            addStyleName(ValoTheme.MENUBAR_SMALL)
        }
        sampleMenuBar {
            caption = "Borderless style"
            addStyleName(ValoTheme.MENUBAR_BORDERLESS)
        }
        sampleMenuBar {
            caption = "Small borderless style"
            addStyleNames(ValoTheme.MENUBAR_BORDERLESS, ValoTheme.MENUBAR_SMALL)
        }

        label("Drop Down Button") { styleName = ValoTheme.LABEL_H3 }
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING; isSpacing = true
            sampleMenuButton("Normal", false)
            sampleMenuButton("Small", false) {
                styleName = ValoTheme.MENUBAR_SMALL
            }
            sampleMenuButton("Borderless", false) {
                styleName = ValoTheme.MENUBAR_BORDERLESS
            }
            // @todo mavi add themed support
            sampleMenuButton("Themed", false) {
                styleName = "color1"
            }
            sampleMenuButton("Small", false) {
                addStyleNames("color1", ValoTheme.MENUBAR_SMALL)
            }
        }

        label("Split Button") { styleName = ValoTheme.LABEL_H3 }
        horizontalLayout {
            styleName = ValoTheme.LAYOUT_HORIZONTAL_WRAPPING
            isSpacing = true
            sampleMenuButton("Normal", true)
            sampleMenuButton("Small", true) {
                styleName = ValoTheme.MENUBAR_SMALL
            }
            sampleMenuButton("Borderless", true) {
                styleName = ValoTheme.MENUBAR_BORDERLESS
            }
            // @todo mavi add themed support
            sampleMenuButton("Themed", true) {
                styleName = "color1"
            }
            sampleMenuButton("Small", true) {
                addStyleNames("color1", ValoTheme.MENUBAR_SMALL)
            }
        }
    }

    override fun enter(event: ViewChangeEvent) {
    }

    companion object {

        private fun HasComponents.sampleMenuButton(caption: String, splitButton: Boolean, block: MenuBar.()->Unit = {}) =
                dropDownButton(caption, splitButton) {
            item("Another Action")
            item("Secondary Action")
            addSeparator()
            item("Last Action")
        }.apply(block)
    }

}

fun HasComponents.sampleMenuBar(block: MenuBar.()->Unit = {}) = menuBar {
    val click: (MenuItem) -> Unit = { selectedItem: MenuItem -> Notification.show("Clicked " + selectedItem.text) }
    w = fillParent
    item("File") {
        item("New") {
            item("File", click)
            item("Folder", click)
            item("Project...", click)
        }
        item("Open file...", click)
        addSeparator()
        item("Close", click)
        item("Close All", click)
        addSeparator()
        item("Save", click)
        item("Save As...", click)
        item("Save All", click)
    }
    item("Edit") {
        item("Undo", click)
        item("Redo", click).isEnabled = false
        addSeparator()
        item("Cut", click)
        item("Copy", click)
        item("Paste", click)
        addSeparator()
    }
    item("Find/Replace") {
        item("Google Search", click)
        addSeparator()
        item("Find/Replace...", click)
        item("Find Next", click)
        item("Find Previous", click)
    }
    val check: (MenuItem) -> Unit = { selectedItem: MenuItem -> Notification.show(if (selectedItem.isChecked) "Checked" else "Unchecked") }
    item("View") {
        item("Show Status Bar", check).isCheckable = true
        item("Show Title Bar", check) {
            isCheckable = true
            isChecked = true
        }
        item("Customize Toolbar...", click)
        addSeparator()
        item("Actual Size", click)
        item("Zoom In", click)
        item("Zoom Out", click)
    }
    item("", check) {
        icon = VaadinIcons.BOLD
        styleName = "icon-only"
        isCheckable = true
        isChecked = true
    }
    item("", check) {
        icon = VaadinIcons.BOLT
        styleName = "icon-only"
        isCheckable = true
        isChecked = true
    }
    item("Attach", VaadinIcons.PAPERCLIP, click)
    item("Undo", VaadinIcons.ROTATE_LEFT, click)
    item("Redo", VaadinIcons.ROTATE_RIGHT, click) {
        isEnabled = false
    }
    item("Upload", VaadinIcons.UPLOAD, click)
    block()
}

fun HasComponents.sampleToolBar(block: MenuBar.()->Unit = {}) = menuBar {
    w = fillParent
    item("", VaadinIcons.ALIGN_LEFT) {
        styleName = "icon-only"
        isCheckable = true
    }
    item("", VaadinIcons.ALIGN_CENTER) {
        styleName = "icon-only"
        isCheckable = true
    }
    item("", VaadinIcons.ALIGN_RIGHT) {
        styleName = "icon-only"
        isCheckable = true
    }
    item("", VaadinIcons.PAPERCLIP) {
        styleName = "icon-only"
    }
    item("", VaadinIcons.ROTATE_LEFT) {
        styleName = "icon-only"
    }
    item("", VaadinIcons.ROTATE_RIGHT) {
        styleName = "icon-only"
        isEnabled = false
    }
    block()
}
