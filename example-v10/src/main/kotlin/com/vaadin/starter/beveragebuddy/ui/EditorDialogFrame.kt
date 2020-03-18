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
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.BinderValidationStatus
import com.vaadin.flow.shared.Registration
import java.io.Serializable

/**
 * The editor form which edits beans of type [T]. The UI is contained in [component] and all fields are referenced
 * via [binder]. Nest this into the [EditorDialogFrame].
 */
interface EditorForm<T : Serializable> {
    /**
     * The displayable name of the item type [T].
     */
    val itemType: String
    /**
     * All form fields are registered to this binder.
     */
    val binder: Binder<T>
    /**
     * Contains all form UI fields.
     */
    val component: FormLayout
}

/**
 * A dialog frame for dialogs adding, editing or deleting items.
 *
 * Users are expected to:
 *  * Set [form] with a proper implementation.
 *  * Set [onSaveItem] and [onDeleteItem]
 *  * Call [open] to show the dialog.
 * @param T the type of the item to be added, edited or deleted
 * @property form the form itself
 */
class EditorDialogFrame<T : Serializable>(private val form: EditorForm<T>) : Dialog() {

    private val titleField: H2
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var deleteButton: Button
    private var registrationForSave: Registration? = null

    /**
     * The item currently being edited.
     */
    var currentItem: T? = null
        private set

    /**
     * Callback to save the edited item. The dialog frame is closed automatically.
     */
    lateinit var onSaveItem: (item: T)->Unit

    /**
     * Callback to delete the edited item. Should open confirmation dialog (or delete the item directly if possible).
     * Note that the frame is not closed automatically and must be closed manually.
     */
    lateinit var onDeleteItem: (item: T)->Unit

    init {
        isCloseOnEsc = true
        isCloseOnOutsideClick = false

        titleField = h2()
        div {
            // form layout wrapper
            addClassName("has-padding")

            add(form.component)
            form.component.apply {
                setResponsiveSteps(
                        FormLayout.ResponsiveStep("0", 1),
                        FormLayout.ResponsiveStep("50em", 2)
                )
                addClassName("no-padding")
            }
        }
        horizontalLayout {
            // button bar
            className = "buttons"
            saveButton = button("Save") {
                isAutofocus = true
                setPrimary()
            }
            cancelButton = button("Cancel") {
                addClickListener { close() }
            }
            deleteButton = button("Delete") {
                addThemeVariants(ButtonVariant.LUMO_ERROR)
                addClickListener { onDeleteItem(currentItem!!) }
            }
        }
    }

    /**
     * Opens given item for editing in this dialog.
     *
     * @param item The item to edit; it may be an existing or a newly created instance
     * @param creating if true, the item is being created; if false, the item is being edited.
     */
    fun open(item: T, creating: Boolean) {
        currentItem = item
        val operation = if (creating) "Add new" else "Edit"
        titleField.text = "$operation ${form.itemType}"
        registrationForSave?.remove()
        registrationForSave = saveButton.addClickListener { saveClicked() }
        form.binder.readBean(currentItem)

        deleteButton.isEnabled = !creating
        open()
    }

    private fun saveClicked() {
        if (form.binder.writeBeanIfValid(currentItem!!)) {
            onSaveItem(currentItem!!)
            close()
        } else {
            Notification.show("Please correct the errors in the form", 2000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR)
        }
    }
}
