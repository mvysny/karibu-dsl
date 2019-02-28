package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * A potentially open date range. If both [from] and [to] are `null`, then the interval accepts any date.
 * @property to the maximum accepted value, inclusive. If `null` then the date range has no upper limit.
 * @property from the minimum accepted value, inclusive. If `null` then the date range has no lower limit.
 */
data class DateInterval(var from: LocalDate?, var to: LocalDate?) : Serializable {
    /**
     * True if the interval includes all possible dates (both [from] and [to] are `null`).
     */
    val isUniversalSet: Boolean
        get() = from == null && to == null

    companion object {
        fun now() = DateInterval(LocalDate.now(), LocalDate.now())
    }
}

/**
 * Only shows a single button as its contents. When the button is clicked, it opens a dialog and allows the user to specify a range
 * of dates. When the user sets the values, the dialog is
 * hidden and the date range is set as the value of the popup.
 *
 * The current date range is also displayed as the caption of the button.
 */
class DateRangePopup: CustomField<DateInterval>() {
    private val formatter get() = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(UI.getCurrent().locale ?: Locale.getDefault())
    private lateinit var fromField: DatePicker
    private lateinit var toField: DatePicker
    private lateinit var set: Button
    private lateinit var clear: Button
    private val dialog = Dialog()
    /**
     * The button which opens the popup [dialog].
     */
    private val content: Button = content {
        button {
            onLeftClick {
                isDialogVisible = !isDialogVisible
            }
        }
    }

    init {
        dialog.apply {
            isCloseOnEsc = true
            isCloseOnOutsideClick = true
            verticalLayout {
                fromField = datePicker("From:")
                toField = datePicker("To:")
                horizontalLayout {
                    set = button("Set") {
                        onLeftClick {
                            updateValue()
                            updateCaption()
                            dialog.close()
                        }
                    }
                    clear = button("Clear") {
                        onLeftClick {
                            fromField.value = null
                            toField.value = null
                            updateValue()
                            updateCaption()
                            dialog.close()
                        }
                    }
                }
            }
        }
        updateCaption()
    }

    var isDialogVisible: Boolean
        get() = dialog.isOpened
        set(value) {
            dialog.isOpened = value
        }

    override fun generateModelValue(): DateInterval? {
        val from: LocalDate? = fromField.value
        val to: LocalDate? = toField.value
        return if (from == null && to == null) null else DateInterval(from, to)
    }

    override fun setPresentationValue(newPresentationValue: DateInterval?) {
        fromField.value = newPresentationValue?.from
        toField.value = newPresentationValue?.to
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

    override fun isReadOnly(): Boolean = !fromField.isEnabled

    override fun setRequiredIndicatorVisible(requiredIndicatorVisible: Boolean) {
        fromField.isRequiredIndicatorVisible = requiredIndicatorVisible
        toField.isRequiredIndicatorVisible = requiredIndicatorVisible
    }

    override fun isRequiredIndicatorVisible(): Boolean = fromField.isRequiredIndicatorVisible
}

fun (@VaadinDsl HasComponents).dateRangePopup(block: (@VaadinDsl DateRangePopup).() -> Unit = {})
        = init(DateRangePopup(), block)
