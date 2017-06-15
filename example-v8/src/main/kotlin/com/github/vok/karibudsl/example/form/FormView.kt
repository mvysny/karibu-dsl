package com.github.vok.karibudsl.example.form

import com.github.vok.karibudsl.*
import com.github.vok.karibudsl.example.title
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.UserError
import com.vaadin.ui.Alignment
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

/**
 * Demonstrates a form which edits given [Person] or creates a new one. Annotation-based validation is performed automatically.
 * @author mavi
 */
@AutoView
class FormView: VerticalLayout(), View {

    // binder is used to bind value-editing components to the Person bean itself.
    // for more information just check https://vaadin.com/vaadin-fw8-documentation-portlet/framework/datamodel/datamodel-forms.html
    private val binder = beanValidationBinder<Person>()

    init {
        title("Forms")

        // create the UI.
        formLayout {
            isMargin = false; w = 800.px

            section("Personal Info")
            textField("Name") {
                w = 50.perc
                // binding to a BeanValidationBinder will also validate the value automatically.
                // please read https://vaadin.com/docs/-/part/framework/datamodel/datamodel-forms.html for more information
                bind(binder).trimmingConverter().bind(Person::fullName)
            }
            dateField("Birthday") {
                bind(binder).bind(Person::dateOfBirth)
                placeholder = "To break the validation, just specify a future date"
                description = "To break the validation, just specify a future date"
            }
            comboBox<MaritalStatus>("Marital Status:") {
                setItems(*MaritalStatus.values())
                bind(binder).bind(Person::maritalStatus)
                // so that you can trigger a validation error
                isEmptySelectionAllowed = true
                isTextInputAllowed = false
            }
            radioButtonGroup<Sex>("Sex") {
                styleName = ValoTheme.OPTIONGROUP_HORIZONTAL
                setItems(*Sex.values())
                bind(binder).bind(Person::sex)
            }
            section("Contact Info", ValoTheme.LABEL_H3)
            textField("Email") {
                w = 50.perc
                bind(binder).bind(Person::email)
            }
            textField("Location") {
                w = 50.perc
                bind(binder).bind(Person::location)
            }
            textField("Phone") {
                w = 50.perc
                bind(binder).bind(Person::phone)
            }
            horizontalLayout {
                caption = "Newsletter"
                checkBox("Subscribe to newsletter") {
                    alignment = Alignment.MIDDLE_CENTER
                    bind(binder).bind(Person::newsletter)
                }
                comboBox<NewsletterFrequency> {
                    alignment = Alignment.MIDDLE_CENTER
                    styleName = ValoTheme.COMBOBOX_SMALL; w = 120.px
                    isTextInputAllowed = false
                    bind(binder).bind(Person::newsletterFrequency)
                    setItems(*NewsletterFrequency.values())
                }
            }
            section("Additional Info", ValoTheme.LABEL_H4)
            textField("Website") {
                w = fillParent
                placeholder = "http://"
                bind(binder).bind(Person::website)
            }
            textArea("Short Bio") {
                w = fillParent; h = 4.em
                bind(binder).bind(Person::shortBio)
            }
            richTextArea("Bio") {
                w = fillParent
                bind(binder).bind(Person::longBio)
            }
        }
        horizontalLayout {
            button("Save") {
                styleName = ValoTheme.BUTTON_PRIMARY
                onLeftClick {
                    val person = Person()
                    // binder.validate() will highlight all invalid fields
                    if (binder.validate().hasErrors() || !binder.writeBeanIfValid(person)) {
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
        edit(Person.createRandom())
    }

    companion object {
        fun navigateTo() = navigateToView<FormView>()
    }
}

fun FormLayout.section(caption: String, style: String = ValoTheme.LABEL_H2, block: Label.()->Unit={}) = init(Label(caption)) {
    addStyleNames(style, ValoTheme.LABEL_COLORED)
    block()
}