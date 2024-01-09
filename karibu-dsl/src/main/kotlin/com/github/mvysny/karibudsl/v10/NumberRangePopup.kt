package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.AbstractCompositeField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import java.io.Serializable

/**
 * A potentially open numeric range. If both [start] and [endInclusive] are `null`, then the interval accepts any number.
 *
 * Immutable, thread-safe.
 * @property start the minimum accepted value, inclusive. If `null` then the numeric range has no lower limit.
 * @property endInclusive the maximum accepted value, inclusive. If `null` then the numeric range has no upper limit.
 */
public data class NumberInterval<T>(override val start: T?, override val endInclusive: T?) : Serializable, ClosedInterval<T>
    where T: Number, T: Comparable<T>

internal data class MutableDoubleInterval(override var start: Double?, override var endInclusive: Double?) : Serializable, ClosedInterval<Double> {
    fun toNumberInterval(): NumberInterval<Double>? = when {
        this.isUniversalSet -> null
        else -> NumberInterval(start, endInclusive)
    }
    companion object {
        fun from(interval: NumberInterval<Double>?): MutableDoubleInterval = MutableDoubleInterval(interval?.start, interval?.endInclusive)
    }
}

/**
 * Only shows a single button as its contents. When the button is clicked, it opens a dialog and allows the user to specify a range
 * of numbers. When the user sets the values, the dialog is
 * hidden and the number range is set as the value of the popup.
 *
 * The current numeric range is also displayed as the caption of the button.
 */
public class NumberRangePopup : AbstractCompositeField<Button, NumberRangePopup, NumberInterval<Double>>(null) {
    private lateinit var ltInput: NumberField
    private lateinit var gtInput: NumberField

    @Suppress("UNCHECKED_CAST")
    private val binder: Binder<MutableDoubleInterval> = Binder(MutableDoubleInterval::class.java).apply {
        bean = MutableDoubleInterval(null, null)
    }
    private lateinit var set: Button
    private lateinit var clear: Button
    private val dialog = Dialog()
    private val content = Button()

    init {
        dialog.apply {
            isCloseOnEsc = true
            isCloseOnOutsideClick = true
            verticalLayout {
                horizontalLayout {
                    gtInput = numberField {
                        placeholder = karibuDslI18n("from")
                        bind(binder).bind(MutableDoubleInterval::start)
                    }
                    text("..")
                    ltInput = numberField {
                        placeholder = karibuDslI18n("to")
                        bind(binder).bind(MutableDoubleInterval::endInclusive)
                    }
                }
                horizontalLayout {
                    set = button(karibuDslI18n("set")) {
                        onLeftClick {
                            val value: MutableDoubleInterval = binder.bean
                            setModelValue(value.toNumberInterval(), true)
                            updateCaption()
                            dialog.close()
                        }
                    }
                    clear = button(karibuDslI18n("clear")) {
                        onLeftClick {
                            binder.fields.forEach { it.clear() }
                            setModelValue(null, true)
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

    override fun setPresentationValue(newPresentationValue: NumberInterval<Double>?) {
        binder.bean = MutableDoubleInterval.from(newPresentationValue)
        updateCaption()
    }

    private fun updateCaption() {
        val value: NumberInterval<Double>? = value
        if (value == null || value.isUniversalSet) {
            content.text = karibuDslI18n("all")
        } else {
            if (value.isSingleItem) {
                content.text = "[x] = ${value.start}"
            } else if (value.isBound) {
                content.text = "${value.start} ≤ [x] ≤ ${value.endInclusive}"
            } else if (value.start != null) {
                content.text = "[x] ≥ ${value.start}"
            } else {
                content.text = "[x] ≤ ${value.endInclusive}"
            }
        }
    }

    override fun setReadOnly(readOnly: Boolean) {
        set.isEnabled = !readOnly
        clear.isEnabled = !readOnly
        ltInput.isEnabled = !readOnly
        gtInput.isEnabled = !readOnly
    }

    override fun initContent(): Button = content

    override fun isReadOnly(): Boolean = !ltInput.isEnabled

    override fun setRequiredIndicatorVisible(requiredIndicatorVisible: Boolean) {
        ltInput.isRequiredIndicatorVisible = requiredIndicatorVisible
        gtInput.isRequiredIndicatorVisible = requiredIndicatorVisible
    }

    override fun isRequiredIndicatorVisible(): Boolean = ltInput.isRequiredIndicatorVisible
}

public fun (@VaadinDsl HasComponents).numberRangePopup(block: (@VaadinDsl NumberRangePopup).() -> Unit = {}): NumberRangePopup
        = init(NumberRangePopup(), block)
