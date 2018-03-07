package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.shared.Registration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * A custom field backed by a complex hierarchy of components, perhaps editing different parts of the value. As an example, a [DateRangePopup]
 * component is implemented, which allows the user to specify a date range and holds the value as a `ClosedRange<LocalDate>`.
 *
 * This component should initialize the hierarchy eagerly and return the root of the hierarchy via [initContent]. The component returned from
 * [initContent] is then displayed as the field.
 *
 * This component should propagate calls to [setReadOnly] and [setRequiredIndicatorVisible] to all children fields.
 *
 * The component internal components may read the value by calling [getValue], however they MUST NOT call [setValue] to set the new value! This call
 * is reserved for the clients of this component only. The internal components must only update their state in the [propagateValueInwards]
 * function; when there is a new value changed by the user, this new value must be propagated by calling [propagateValueOutwards].
 *
 * @param T the type of this component
 * @param V the value being edited. Should be immutable, to avoid nasty surprises when somebody changes the fields of the value, since this change
 * does not trigger the value change listeners.
 */
abstract class CustomField<T: CustomField<T, V>, V> : Composite<Component>(), HasValue<T, V> {
    private val listeners = mutableListOf<HasValue.ValueChangeListener<T, V>>()
    private var internalValue: V? = null
    /**
     * If this is true, then we are currently calling [propagateValueInwards] and we want to ignore any calls to
     * [propagateValueOutwards] called by value change listeners triggered by the value changes.
     */
    private var dontFireListeners: Boolean = false

    override fun setValue(value: V?) {
        if (internalValue != value) {
            val oldValue = internalValue
            internalValue = value
            val event = HasValue.ValueChangeEvent(this as T, this, oldValue, false)
            dontFireListeners = true
            propagateValueInwards(value)
            dontFireListeners = false
            fireListeners(event)
        }
    }

    abstract override fun initContent(): Component

    /**
     * Propagates the new value to the internal content components. The components may in turn fire value change events and call
     * [propagateValueOutwards] - it will do nothing during the invocation of this method.
     */
    protected abstract fun propagateValueInwards(value: V?)

    /**
     * All content fields should call this method with the new [value] when they're modified.
     * This will properly update this CustomField's internal value and fire the event listeners.
     */
    protected fun propagateValueOutwards(value: V?) {
        if (!dontFireListeners && internalValue != value) {
            val oldValue = internalValue
            internalValue = value
            val event = HasValue.ValueChangeEvent(this as T, this, oldValue, true)
            fireListeners(event)
        }
    }

    override fun getValue(): V? = internalValue

    private fun fireListeners(value: HasValue.ValueChangeEvent<T, V>) {
        listeners.forEach { it.onComponentEvent(value) }
    }

    override fun addValueChangeListener(listener: HasValue.ValueChangeListener<T, V>): Registration {
        listeners.add(listener)
        return Registration { listeners.remove(listener) }
    }

    override fun getClientValuePropertyName(): String = throw UnsupportedOperationException()

    override fun getClientPropertyChangeEventName(): String = throw UnsupportedOperationException()

    abstract override fun setReadOnly(readOnly: Boolean)

    abstract override fun isReadOnly(): Boolean

    abstract override fun setRequiredIndicatorVisible(requiredIndicatorVisible: Boolean)

    abstract override fun isRequiredIndicatorVisible(): Boolean
}

/**
 * A button which opens a dialog on click and allows the user to specify a range of dates. When the user sets the values, the dialog is
 * hidden and the date range is set as the value of the popup.
 */
class DateRangePopup: CustomField<DateRangePopup, ClosedRange<LocalDate>>() {
    private val formatter get() = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(UI.getCurrent().locale ?: Locale.getDefault())
    private lateinit var fromField: DatePicker
    private lateinit var toField: DatePicker
    private lateinit var set: Button
    private lateinit var clear: Button
    private val dialog = Dialog()
    private val content = Button()

    init {
        dialog.apply {
            isCloseOnEsc = true
            isCloseOnOutsideClick = false
            addOpenedChangeListener({
                if (!isOpened) {
                    element.removeFromParent();
                }
            })
            verticalLayout {
                fromField = datePicker("From:")
                toField = datePicker("To:")
                horizontalLayout {
                    set = button("Set") {
                        onLeftClick {
                            val from: LocalDate? = fromField.value
                            val to: LocalDate? = toField.value
                            if (from == null || to == null) {
                                propagateValueOutwards(null)
                            } else {
                                propagateValueOutwards(from..to)
                            }
                            updateCaption()
                            dialog.close()
                        }
                    }
                    clear = button("Clear") {
                        onLeftClick {
                            fromField.value = null
                            toField.value = null
                            propagateValueOutwards(null)
                            updateCaption()
                            dialog.close()
                        }
                    }
                }
            }
        }
        content.apply {
            onLeftClick {
                dialog.isOpened = !dialog.isOpened
            }
        }
        updateCaption()
    }

    override fun propagateValueInwards(value: ClosedRange<LocalDate>?) {
        fromField.value = value?.start
        toField.value = value?.endInclusive
        updateCaption()
    }

    private fun format(date: LocalDate?) = if (date == null) "" else formatter.format(date)

    private fun updateCaption() {
        val value = value
        if (value == null) {
            content.text = "All"
        } else {
            content.text = "${format(fromField.value)} - ${format(toField.value)}"
        }
    }

    override fun setReadOnly(readOnly: Boolean) {
        set.isEnabled = !readOnly
        clear.isEnabled = !readOnly
        fromField.isEnabled = !readOnly
        toField.isEnabled = !readOnly
    }

    override fun initContent(): Component = content

    override fun isReadOnly(): Boolean = !fromField.isEnabled

    override fun setRequiredIndicatorVisible(requiredIndicatorVisible: Boolean) {
        fromField.isRequiredIndicatorVisible = requiredIndicatorVisible
        toField.isRequiredIndicatorVisible = requiredIndicatorVisible
    }

    override fun isRequiredIndicatorVisible(): Boolean = fromField.isRequiredIndicatorVisible
}
