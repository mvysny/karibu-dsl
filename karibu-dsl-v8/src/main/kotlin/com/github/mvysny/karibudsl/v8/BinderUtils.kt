package com.github.mvysny.karibudsl.v8

import com.vaadin.data.*
import com.vaadin.data.converter.*
import com.vaadin.server.Page
import com.vaadin.server.WebBrowser
import com.vaadin.ui.AbstractTextField
import java.math.BigDecimal
import java.math.BigInteger
import java.time.*
import java.util.*
import kotlin.reflect.KMutableProperty1

/**
 * Trims the user input string before storing it into the underlying property data source. Vital for mobile-oriented apps:
 * Android keyboard often adds whitespace to the end of the text when auto-completion occurs. Imagine storing a username ending with a space upon registration:
 * such person can no longer log in from his PC unless he explicitely types in the space.
 * @param blanksToNulls if true then a blank String value is passed as `null` to the model. Defaults to false.
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.trimmingConverter(blanksToNulls: Boolean = false): Binder.BindingBuilder<BEAN, String?> =
        withConverter(object : Converter<String?, String?> {
            override fun convertToModel(value: String?, context: ValueContext?): Result<String?> {
                var trimmedValue: String? = value?.trim()
                if (blanksToNulls && trimmedValue.isNullOrBlank()) {
                    trimmedValue = null
                }
                return Result.ok(trimmedValue)
            }
            override fun convertToPresentation(value: String?, context: ValueContext?): String? {
                // must not return null here otherwise TextField will fail with NPE:
                // // workaround for https://github.com/vaadin/framework/issues/8664
                return value ?: ""
            }
        })

public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toInt(): Binder.BindingBuilder<BEAN, Int?> =
        withConverter(StringToIntegerConverter(karibuDslI18n("cantConvertToInteger")))
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toDouble(): Binder.BindingBuilder<BEAN, Double?> =
        withConverter(StringToDoubleConverter(karibuDslI18n("cantConvertToDecimal")))
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toLong(): Binder.BindingBuilder<BEAN, Long?> =
        withConverter(StringToLongConverter(karibuDslI18n("cantConvertToInteger")))
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigDecimal(): Binder.BindingBuilder<BEAN, BigDecimal?> =
        withConverter(StringToBigDecimalConverter(karibuDslI18n("cantConvertToDecimal")))
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigInteger(): Binder.BindingBuilder<BEAN, BigInteger?> =
        withConverter(StringToBigIntegerConverter(karibuDslI18n("cantConvertToInteger")))

public val WebBrowser.timeZone: ZoneId
    get() = if (!timeZoneId.isNullOrBlank()) {
        // take into account zone ID. This is important for historical dates, to properly compute date with daylight savings.
        ZoneId.of(timeZoneId)
    } else {
        // fallback to time zone offset
        ZoneOffset.ofTotalSeconds(timezoneOffset / 1000)
    }

public val browserTimeZone: ZoneId get() = Page.getCurrent().webBrowser.timeZone

/**
 * Returns the current date and time at browser's current time zone.
 */
public val WebBrowser.currentDateTime: LocalDateTime
    get() =
        LocalDateTime.now(ZoneOffset.ofTotalSeconds(timezoneOffset / 1000))

public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateToDateConverter(browserTimeZone))
@JvmName("localDateTimeToDate")
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateTimeToDateConverter(browserTimeZone))
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toInstant(): Binder.BindingBuilder<BEAN, Instant?> =
    withConverter(LocalDateToInstantConverter(browserTimeZone))

@JvmName("localDateTimeToInstant")
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toInstant(): Binder.BindingBuilder<BEAN, Instant?> =
    withConverter(LocalDateTimeToInstantConverter(browserTimeZone))

/**
 * Allows you to create [BeanValidationBinder] like this: `beanValidationBinder<Person>()` instead of `BeanValidationBinder(Person::class.java)`
 */
public inline fun <reified T : Any> beanValidationBinder(): BeanValidationBinder<T> = BeanValidationBinder(T::class.java)

/**
 * Allows you to bind the component directly in the component's definition. E.g.
 * ```
 * textField("Name:") {
 *   bind(binder).trimmingConverter().bind(Person::name)
 * }
 * ```
 */
public fun <BEAN, FIELDVALUE> HasValue<FIELDVALUE>.bind(binder: Binder<BEAN>): Binder.BindingBuilder<BEAN, FIELDVALUE> {
    var builder = binder.forField(this)
    @Suppress("UNCHECKED_CAST")
    if (this is AbstractTextField) builder = builder.withNullRepresentation("" as FIELDVALUE)
    return builder
}

/**
 * A type-safe binding which binds only to a property of given type, found on given bean.
 * @param prop the bean property
 */
public fun <BEAN, FIELDVALUE> Binder.BindingBuilder<BEAN, FIELDVALUE>.bind(prop: KMutableProperty1<BEAN, out FIELDVALUE?>): Binder.Binding<BEAN, FIELDVALUE?> {
// oh crap, don't use binding by getter and setter - validations won't work!
// we need to use bind(String) even though that will use undebuggable crappy Java 8 lambdas :-(
//        bind({ bean -> prop.get(bean) }, { bean, value -> prop.set(bean, value) })
    return bind(prop.name)
}

/**
 * A converter that converts between [LocalDate] and [Instant].
 * @property zoneId the time zone id to use.
 */
public class LocalDateToInstantConverter(public val zoneId: ZoneId = browserTimeZone) : Converter<LocalDate?, Instant?> {
    override fun convertToModel(localDate: LocalDate?, context: ValueContext): Result<Instant?> =
        Result.ok(localDate?.atStartOfDay(zoneId)?.toInstant())

    override fun convertToPresentation(date: Instant?, context: ValueContext): LocalDate? =
        date?.atZone(zoneId)?.toLocalDate()
}

/**
 * A converter that converts between [LocalDateTime] and [Instant].
 * @property zoneId the time zone to use
 */
public class LocalDateTimeToInstantConverter(public val zoneId: ZoneId = browserTimeZone) : Converter<LocalDateTime?, Instant?> {
    override fun convertToModel(localDate: LocalDateTime?, context: ValueContext): Result<Instant?> =
        Result.ok(localDate?.atZone(zoneId)?.toInstant())

    override fun convertToPresentation(date: Instant?, context: ValueContext): LocalDateTime? =
        date?.atZone(zoneId)?.toLocalDateTime()
}
