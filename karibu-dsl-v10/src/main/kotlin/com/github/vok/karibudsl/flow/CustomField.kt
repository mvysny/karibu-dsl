package com.github.vok.karibudsl.flow

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.shared.Registration

/**
 * A custom field backed by a complex hierarchy of components, perhaps editing different parts of the value. As an example, a [DateRangePopup]
 * component is implemented, which allows the user to specify a date range and holds the value as a [DateInterval].
 *
 * This component should initialize the hierarchy eagerly and return the root of the hierarchy via [initContent]. The component returned from
 * [initContent] is then displayed as the contents of the field.
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
abstract class CustomField<T: CustomField<T, V>, V> : Composite<Component>(), HasValue<AbstractField.ComponentValueChangeEvent<T, V>, V> {
    private val listeners = mutableListOf<HasValue.ValueChangeListener<in AbstractField.ComponentValueChangeEvent<T, V>>>()
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
            @Suppress("UNCHECKED_CAST")
            val event = AbstractField.ComponentValueChangeEvent(this as T, this, oldValue, false)
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
            @Suppress("UNCHECKED_CAST")
            val event = AbstractField.ComponentValueChangeEvent(this as T, this, oldValue, true)
            fireListeners(event)
        }
    }

    override fun getValue(): V? = internalValue

    private fun fireListeners(value: AbstractField.ComponentValueChangeEvent<T, V>) {
        listeners.forEach { it.valueChanged(value) }
    }

    override fun addValueChangeListener(listener: HasValue.ValueChangeListener<in AbstractField.ComponentValueChangeEvent<T, V>>): Registration {
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

