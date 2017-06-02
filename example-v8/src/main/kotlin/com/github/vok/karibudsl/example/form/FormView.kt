package com.github.vok.karibudsl.example.form

import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.UserError
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

/**
 * Demonstrates a form which edits given [Person] or creates a new one. Annotation-based validation is performed automatically.
 * @author mavi
 */
@AutoView
class FormView: VerticalLayout(), View {

    private val binder = beanValidationBinder<Person>()

    init {
        label("Add New Person Demo") {
            addStyleNames(ValoTheme.LABEL_COLORED, ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN)
        }
        // create the UI.
        formLayout {
            textField("Full Name:") {
                // binding to a BeanValidationBinder will also validate the value automatically.
                // please read https://vaadin.com/docs/-/part/framework/datamodel/datamodel-forms.html for more information
                bind(binder).trimmingConverter().bind(Person::fullName)
            }
            label("To break the validation, just specify a future date")
            dateField("Date of Birth:") {
                bind(binder).bind(Person::dateOfBirth)
            }
            comboBox<MaritalStatus>("Marital Status:") {
                setItems(*MaritalStatus.values())
                bind(binder).bind(Person::maritalStatus)
                // so that you can trigger a validation error
                isEmptySelectionAllowed = true
            }
            checkBox("Is Alive") {
                bind(binder).bind(Person::alive)
            }
            textArea("Comment:") {
                bind(binder).bind(Person::comment)
            }
        }
        horizontalLayout {
            button("Save") {
                styleName = ValoTheme.BUTTON_PRIMARY
                onLeftClick {
                    val person = Person()
                    if (!binder.writeBeanIfValid(person)) {
                        componentError = UserError("The form contains invalid values")
                    } else {
                        componentError = null
                        this@FormView.label("Saved $person")
                        createPerson()
                    }
                }
            }
            button("Random") {
                onLeftClick { edit(Person.createRandom()) }
            }
        }
    }

    fun createPerson() = edit(Person())

    fun edit(person: Person) {
        binder.readBean(person)
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
        edit(Person())
    }

    companion object {
        fun navigateTo() = navigateToView<FormView>()
    }
}