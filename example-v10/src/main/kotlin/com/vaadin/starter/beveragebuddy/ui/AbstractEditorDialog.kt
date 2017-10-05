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

import com.vaadin.data.Binder
import com.vaadin.shared.Registration
import com.vaadin.ui.Composite
import com.vaadin.ui.button.Button
import com.vaadin.ui.common.HtmlImport
import com.vaadin.ui.formlayout.FormLayout
import com.vaadin.ui.html.Div
import com.vaadin.ui.html.H2
import com.vaadin.ui.layout.HorizontalLayout
import com.vaadin.ui.paper.dialog.GeneratedPaperDialog

import java.io.Serializable
import java.util.Objects
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Abstract base class for dialogs adding, editing or deleting items.
 *
 * Subclasses are expected to
 *
 *  * add, during construction, the needed UI components to
 * [formLayout] and bind them using [binder], as well
 * as
 *  * override [confirmDelete] to open the confirmation dialog with
 * the desired message (by calling
 * [openConfirmationDialog].
 *
 *
 * @param <T>
 * the type of the item to be added, edited or deleted
</T> */
@HtmlImport("frontend://bower_components/paper-dialog/paper-dialog.html")
abstract class AbstractEditorDialog<T : Serializable>
/**
 * Constructs a new instance.
 *
 * @param itemType
 * The readable name of the item type
 * @param itemSaver
 * Callback to save the edited item
 * @param itemDeleter
 * Callback to delete the edited item
 */
protected constructor(private val itemType: String,
                      private val itemSaver: BiConsumer<T, Operation>, private val itemDeleter: Consumer<T>) : Composite<GeneratedPaperDialog<*>>() {

    private val titleField = H2()
    private val saveButton = Button("Save")
    private val cancelButton = Button("Cancel")
    private val deleteButton = Button("Delete")
    private var registrationForSave: Registration? = null

    /**
     * Gets the form layout, where additional components can be added for
     * displaying or editing the item's properties.
     *
     * @return the form layout
     */
    protected val formLayout = FormLayout()
    private val buttonBar = HorizontalLayout(saveButton,
            cancelButton, deleteButton)

    /**
     * Gets the binder.
     *
     * @return the binder
     */
    protected val binder = Binder<T>()
    /**
     * Gets the item currently being edited.
     *
     * @return the item currently being edited
     */
    protected var currentItem: T? = null
        private set

    private val confirmationDialog = ConfirmationDialog<T>()
    private val notification = PaperToast()

    /**
     * The operations supported by this dialog. Delete is enabled when editing
     * an already existing item.
     */
    enum class Operation(val nameInTitle: String, val nameInText: String,
                                             val isDeleteDisabled: Boolean) {
        ADD("Add New", "add", true),
        EDIT("Edit", "edit", false)
    }

    init {

        initTitle()
        initFormLayout()
        initButtonBar()
        initNotification()
        content.setModal(true)
        // Enabling modality disables cancel-on-esc (and cancel-on-outside-click)
        // We want to cancel on esc
        content.setNoCancelOnEscKey(false)
    }

    private fun initTitle() {
        content.add(titleField)
    }

    private fun initFormLayout() {
        formLayout.setResponsiveSteps(FormLayout.ResponsiveStep("0", 1),
                FormLayout.ResponsiveStep("50em", 2))
        formLayout.addClassName("no-padding")
        val div = Div(formLayout)
        div.addClassName("has-padding")
        content.add(div)
    }

    private fun initButtonBar() {
        saveButton.isAutofocus = true
        saveButton.element.setAttribute("theme", "primary")
        cancelButton.element.setAttribute("dialog-dismiss", true)
        deleteButton.addClickListener { deleteClicked() }
        deleteButton.element.setAttribute("theme", "tertiary danger")
        buttonBar.className = "buttons"
        content.add(buttonBar)
    }

    private fun initNotification() {
        content.add(notification)
        notification.addClassName("notification")
    }

    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item
     * The item to edit; it may be an existing or a newly created
     * instance
     * @param operation
     * The operation being performed on the item
     */
    fun open(item: T, operation: Operation) {
        currentItem = item
        titleField.text = operation.nameInTitle + " " + itemType
        if (registrationForSave != null) {
            registrationForSave!!.remove()
        }
        registrationForSave = saveButton
                .addClickListener { saveClicked(operation) }
        binder.readBean(currentItem)

        deleteButton.isDisabled = operation.isDeleteDisabled
        content.open()
    }

    private fun saveClicked(operation: Operation) {
        val isValid = binder.writeBeanIfValid(currentItem!!)

        if (isValid) {
            itemSaver.accept(currentItem!!, operation)
            content.close()
        } else {
            val status = binder.validate()
            notification.show(status.validationErrors
                    .map { it.errorMessage }
                    .joinToString("; "))
        }
    }

    private fun deleteClicked() {
        if (confirmationDialog.element.parent == null) {
            ui.ifPresent { ui -> ui.add(confirmationDialog) }
        }
        confirmDelete()
    }

    protected abstract fun confirmDelete()

    /**
     * Opens the confirmation dialog before deleting the current item.
     *
     * The dialog will display the given title and message(s), then call
     * [deleteConfirmed] if the Delete button is clicked.
     *
     * @param title
     * The title text
     * @param message
     * Detail message (optional, may be empty)
     * @param additionalMessage
     * Additional message (optional, may be empty)
     */
    protected fun openConfirmationDialog(title: String, message: String,
                                         additionalMessage: String) {
        content.close()
        confirmationDialog.open(title, message, additionalMessage, "Delete",
                true, currentItem!!, Consumer { this.deleteConfirmed(it) },
                Runnable { content.open() })
    }

    private fun deleteConfirmed(item: T) {
        itemDeleter.accept(item)
        content.close()
    }
}
