package com.github.mvysny.karibudsl.v8

import com.vaadin.shared.ui.datefield.DateTimeResolution
import com.vaadin.ui.*
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * A potentially open date range. If both [from] and [to] are `null`, then the interval accepts any date.
 * @property to the maximum accepted value, inclusive. If `null` then the date range has no upper limit.
 * @property from the minimum accepted value, inclusive. If `null` then the date range has no lower limit.
 */
data class DateInterval(var from: LocalDateTime?, var to: LocalDateTime?) : Serializable {
    /**
     * True if the interval includes all possible dates (both [from] and [to] are `null`).
     */
    val isUniversalSet: Boolean
        get() = from == null && to == null

    companion object {
        fun now(zoneId: ZoneId = browserTimeZone) = DateInterval(LocalDateTime.now(zoneId), LocalDateTime.now(zoneId))
    }
}

/**
 * Only shows a single button as its contents. When the button is clicked, it opens a dialog and allows the user to specify a range
 * of dates. When the user sets the values, the dialog is
 * hidden and the date range is set as the value of the popup.
 *
 * The current date range is also displayed as the caption of the button.
 */
class DateRangePopup: CustomField<DateInterval?>() {
    private val formatter get() = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(UI.getCurrent().locale!!)
    private lateinit var fromField: InlineDateTimeField
    private lateinit var toField: InlineDateTimeField
    private lateinit var set: Button
    private lateinit var clear: Button
    /**
     * The desired resolution of this filter popup, defaults to [DateTimeResolution.MINUTE].
     */
    var resolution: DateTimeResolution = DateTimeResolution.MINUTE
        set(value) {
            field = value
            updateFields()
        }

    private var internalValue: DateInterval? = null

    init {
        styleName = "datefilterpopup"
        // force initcontents so that fromField and toField are initialized and one can set resolution to them
        content
    }

    override fun doSetValue(value: DateInterval?) {
        internalValue = value?.copy()
        updateValueToFields()
        updateCaption()
    }

    private fun updateValueToFields() {
        if (isPopupInitialized) {
            fromField.value = internalValue?.from
            toField.value = internalValue?.to
        }
    }

    override fun getValue() = internalValue?.copy()

    private fun format(date: LocalDateTime?) = if (date == null) "" else formatter.format(date)

    private fun updateCaption() {
        val content = content as KPopupView
        val value = value
        if (value == null || value.isUniversalSet) {
            content.minimizedValueAsHTML = karibuDslI18n("all")
        } else {
            content.minimizedValueAsHTML = "${format(value.from)} - ${format(value.to)}"
        }
    }

    private fun truncateDate(date: LocalDateTime?, resolution: DateTimeResolution, start: Boolean): LocalDateTime? {
        @Suppress("NAME_SHADOWING")
        var date = date ?: return null
        for (res in DateTimeResolution.values().slice(0..resolution.ordinal - 1)) {
            if (res == DateTimeResolution.SECOND) {
                date = date.withSecond(if (start) 0 else 59)
            } else if (res == DateTimeResolution.MINUTE) {
                date = date.withMinute(if (start) 0 else 59)
            } else if (res == DateTimeResolution.HOUR) {
                date = date.withHour(if (start) 0 else 23)
            } else if (res == DateTimeResolution.DAY) {
                date = date.withDayOfMonth(if (start) 1 else date.toLocalDate().lengthOfMonth())
            } else if (res == DateTimeResolution.MONTH) {
                date = date.withMonth(if (start) 1 else 12)
            }
        }
        date = date.withNano(if (start) 0 else 999)
        return date
    }

    override fun initContent(): Component = KPopupView().apply {
        w = fillParent; minimizedValueAsHTML = karibuDslI18n("all"); isHideOnMouseOut = false
        lazy {
            verticalLayout {
                styleName = "datefilterpopupcontent"; setSizeUndefined(); isSpacing = true; isMargin = true

                horizontalLayout {
                    isSpacing = true
                    fromField = inlineDateTimeField {
                        locale = this@DateRangePopup.locale
                    }
                    toField = inlineDateTimeField {
                        locale = this@DateRangePopup.locale
                    }
                }
                horizontalLayout {
                    alignment = Alignment.BOTTOM_RIGHT
                    isSpacing = true
                    set = button(karibuDslI18n("set")) {
                        onLeftClick {
                            value = DateInterval(
                                    truncateDate(fromField.value, resolution, true),
                                    truncateDate(toField.value, resolution, false)
                            )
                            isPopupVisible = false
                        }
                    }
                    clear = button(karibuDslI18n("clear")) {
                        onLeftClick {
                            value = null
                            isPopupVisible = false
                        }
                    }
                }
            }

            updateValueToFields()
            updateFields()
        }
    }

    override fun setReadOnly(readOnly: Boolean) {
        super.setReadOnly(readOnly)
        updateFields()
    }

    private val isPopupInitialized: Boolean get() = ::set.isInitialized

    private fun updateFields() {
        if (isPopupInitialized) {
            set.isEnabled = !isReadOnly
            clear.isEnabled = !isReadOnly
            fromField.isEnabled = !isReadOnly
            toField.isEnabled = !isReadOnly
            fromField.resolution = resolution
            toField.resolution = resolution
        }
    }

    var isPopupVisible: Boolean
        get() = (content as KPopupView).isPopupVisible
        set(value) {
            (content as KPopupView).isPopupVisible = value
        }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).dateRangePopup(value: DateInterval? = null, block: (@VaadinDsl DateRangePopup).()->Unit = {}) = init(DateRangePopup()) {
    if (value != null) this.value = value
    block()
}
