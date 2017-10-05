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

import com.vaadin.shared.Registration
import com.vaadin.ui.Composite
import com.vaadin.ui.button.Button
import com.vaadin.ui.common.HtmlImport
import com.vaadin.ui.html.Div
import com.vaadin.ui.html.H2
import com.vaadin.ui.layout.HorizontalLayout
import com.vaadin.ui.paper.dialog.GeneratedPaperDialog

import java.io.Serializable
import java.util.function.Consumer

/**
 * A generic dialog for confirming or cancelling an action.
 *
 * @param <T>
 * The type of the action's subject
</T> */
@HtmlImport("frontend://bower_components/paper-dialog/paper-dialog.html")
internal class ConfirmationDialog<T : Serializable> : Composite<GeneratedPaperDialog<*>>() {

    private val titleField = H2()
    private val messageLabel = Div()
    private val extraMessageLabel = Div()
    private val confirmButton = Button()
    private val cancelButton = Button("Cancel")
    private var registrationForConfirm: Registration? = null
    private var registrationForCancel: Registration? = null

    /**
     * Constructor.
     */
    init {
        content.setModal(true)
        // Enabling modality disables cancel-on-esc (and cancel-on-outside-click)
        // We want to cancel on esc
        content.setNoCancelOnEscKey(false)

        element.classList.add("confirm-dialog")
        confirmButton.element.setAttribute("dialog-confirm", true)
        confirmButton.element.setAttribute("theme", "tertiary")
        confirmButton.isAutofocus = true
        cancelButton.element.setAttribute("dialog-dismiss", true)
        cancelButton.element.setAttribute("theme", "tertiary")

        val buttonBar = HorizontalLayout(confirmButton,
                cancelButton)
        buttonBar.className = "buttons"

        val labels = Div(messageLabel, extraMessageLabel)
        labels.className = "text"

        content.add(titleField, labels, buttonBar)
    }

    /**
     * Opens the confirmation dialog.
     *
     * The dialog will display the given title and message(s), then call
     * `confirmHandler` if the Confirm button is clicked, or
     * `cancelHandler` if the Cancel button is clicked.
     *
     * @param title
     * The title text
     * @param message
     * Detail message (optional, may be empty)
     * @param additionalMessage
     * Additional message (optional, may be empty)
     * @param actionName
     * The action name to be shown on the Confirm button
     * @param isDisruptive
     * True if the action is disruptive, such as deleting an item
     * @param item
     * The subject of the action
     * @param confirmHandler
     * The confirmation handler function
     * @param cancelHandler
     * The cancellation handler function
     */
    fun open(title: String, message: String, additionalMessage: String,
             actionName: String, isDisruptive: Boolean, item: T, confirmHandler: Consumer<T>,
             cancelHandler: Runnable) {
        titleField.text = title
        messageLabel.text = message
        extraMessageLabel.text = additionalMessage
        confirmButton.text = actionName

        if (registrationForConfirm != null) {
            registrationForConfirm!!.remove()
        }
        registrationForConfirm = confirmButton
                .addClickListener { e -> confirmHandler.accept(item) }
        if (registrationForCancel != null) {
            registrationForCancel!!.remove()
        }
        registrationForCancel = cancelButton
                .addClickListener { e -> cancelHandler.run() }
        if (isDisruptive) {
            confirmButton.element.setAttribute("theme", "tertiary danger")
        }
        content.open()
    }
}
