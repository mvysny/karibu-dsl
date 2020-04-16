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
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.shared.Registration

/**
 * A generic dialog for confirming or cancelling an action.
 */
internal class ConfirmationDialog : Dialog() {

    private val titleField: H2
    private lateinit var messageLabel: Div
    private lateinit var extraMessageLabel: Div
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button
    private var registrationForConfirm: Registration? = null

    init {
        element.classList.add("confirm-dialog")
        isCloseOnEsc = true
        isCloseOnOutsideClick = false

        titleField = h2 {
            className = "confirm-dialog-heading"
        }
        div {
            // labels
            className = "confirm-text"
            messageLabel = div()
            extraMessageLabel = div()
        }
        horizontalLayout {
            // button bar
            className = "confirm-dialog-buttons"
            confirmButton = button {
                addClickListener { close() }
                isAutofocus = true
            }
            cancelButton = button("Cancel") {
                addClickListener { close() }
                addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            }
        }
    }

    /**
     * Opens the confirmation dialog with given [title].
     *
     * The dialog will display the given title and message(s), then call
     * [confirmHandler] if the Confirm button is clicked.
     * @param message Detail message (optional, may be empty)
     * @param additionalMessage Additional message (optional, may be empty)
     * @param actionName The action name to be shown on the Confirm button
     * @param isDisruptive True if the action is disruptive, such as deleting an item
     */
    fun open(title: String, message: String = "", additionalMessage: String = "",
             actionName: String, isDisruptive: Boolean, confirmHandler: () -> Unit) {
        titleField.text = title
        messageLabel.text = message
        extraMessageLabel.text = additionalMessage
        confirmButton.text = actionName

        registrationForConfirm?.remove()
        registrationForConfirm = confirmButton.addClickListener { confirmHandler() }
        if (isDisruptive) {
            confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
        }
        open()
    }
}
