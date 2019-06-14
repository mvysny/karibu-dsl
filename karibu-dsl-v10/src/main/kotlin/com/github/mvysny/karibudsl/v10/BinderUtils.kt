package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.data.binder.*
import com.vaadin.flow.data.converter.*
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.page.ExtendedClientDetails
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.server.VaadinSession
import com.vaadin.flow.server.WebBrowser
import java.math.BigDecimal
import java.math.BigInteger
import java.time.*
import java.util.*
import kotlin.reflect.KMutableProperty1
import java.time.ZoneOffset
import java.time.LocalDateTime


/**
 * Trims the user input string before storing it into the underlying property data source. Vital for mobile-oriented apps:
 * Android keyboard often adds whitespace to the end of the text when auto-completion occurs. Imagine storing a username ending with a space upon registration:
 * such person can no longer log in from his PC unless he explicitely types in the space.
 */
fun <BEAN> Binder.BindingBuilder<BEAN, String?>.trimmingConverter(): Binder.BindingBuilder<BEAN, String?> =
        withConverter(object : Converter<String?, String?> {
            override fun convertToModel(value: String?, context: ValueContext?): Result<String?> =
                    Result.ok(value?.trim())

            override fun convertToPresentation(value: String?, context: ValueContext?): String? {
                // must not return null here otherwise TextField will fail with NPE:
                // // workaround for https://github.com/vaadin/framework/issues/8664
                return value ?: ""
            }
        })

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toInt(): Binder.BindingBuilder<BEAN, Int?> =
        withConverter(StringToIntegerConverter(karibuDslI18n("cantConvertToInteger")))

@JvmName("doubleToInt")
fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toInt(): Binder.BindingBuilder<BEAN, Int?> =
        withConverter(DoubleToIntConverter)

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toDouble(): Binder.BindingBuilder<BEAN, Double?> =
        withConverter(StringToDoubleConverter(karibuDslI18n("cantConvertToDecimal")))

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toLong(): Binder.BindingBuilder<BEAN, Long?> =
        withConverter(StringToLongConverter(karibuDslI18n("cantConvertToInteger")))

@JvmName("doubleToLong")
fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toLong(): Binder.BindingBuilder<BEAN, Long?> =
        withConverter(DoubleToLongConverter)

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigDecimal(): Binder.BindingBuilder<BEAN, BigDecimal?> =
        withConverter(StringToBigDecimalConverter(karibuDslI18n("cantConvertToDecimal")))

@JvmName("doubleToBigDecimal")
fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toBigDecimal(): Binder.BindingBuilder<BEAN, BigDecimal?> =
        withConverter(DoubleToBigDecimalConverter)

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigInteger(): Binder.BindingBuilder<BEAN, BigInteger?> =
        withConverter(StringToBigIntegerConverter(karibuDslI18n("cantConvertToInteger")))

@JvmName("doubleToBigInteger")
fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toBigInteger(): Binder.BindingBuilder<BEAN, BigInteger?> =
        withConverter(DoubleToBigIntegerConverter)

/**
 * The time zone as reported by the browser. Use [com.vaadin.flow.component.page.Page.retrieveExtendedClientDetails]
 * to get [ExtendedClientDetails].
 */
val ExtendedClientDetails.timeZone: ZoneId
    get() = if (!timeZoneId.isNullOrBlank()) {
        // take into account zone ID. This is important for historical dates, to properly compute date with daylight savings.
        ZoneId.of(timeZoneId)
    } else {
        // fallback to time zone offset
        ZoneOffset.ofTotalSeconds(timezoneOffset / 1000)
    }

/**
 * You need to populate this field first, by using [com.vaadin.flow.component.page.Page.retrieveExtendedClientDetails].
 */
var extendedClientDetails: ExtendedClientDetails?
    get() = VaadinSession.getCurrent().getAttribute(ExtendedClientDetails::class.java)
    set(value) {
        VaadinSession.getCurrent().setAttribute(ExtendedClientDetails::class.java, value)
    }

/**
 * The time zone as reported by the browser. You need to populate the [extendedClientDetails] first, otherwise the
 * UTC Time zone is going to be returned!
 */
val browserTimeZone: ZoneId get() = extendedClientDetails?.timeZone ?: ZoneId.of("UTC")

/**
 * Returns the current date and time at browser's current time zone.
 */
val ExtendedClientDetails.currentDateTime: LocalDateTime
    get() =
        LocalDateTime.now(ZoneOffset.ofTotalSeconds(timezoneOffset / 1000))

fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateToDateConverter(browserTimeZone))

@JvmName("localDateTimeToDate")
fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateTimeToDateConverter(browserTimeZone))

fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toInstant(): Binder.BindingBuilder<BEAN, Instant?> =
        withConverter(LocalDateToInstantConverter(browserTimeZone))

@JvmName("localDateTimeToInstant")
fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toInstant(): Binder.BindingBuilder<BEAN, Instant?> =
        withConverter(LocalDateTimeToInstantConverter(browserTimeZone))

/**
 * Allows you to create [BeanValidationBinder] like this: `beanValidationBinder<Person>()` instead of `BeanValidationBinder(Person::class.java)`
 */
inline fun <reified T : Any> beanValidationBinder(): BeanValidationBinder<T> = BeanValidationBinder(T::class.java)

/**
 * Allows you to bind the component directly in the component's definition. E.g.
 * ```
 * textField("Name:") {
 *   bind(binder).trimmingConverter().bind(Person::name)
 * }
 * ```
 */
fun <BEAN, FIELDVALUE> HasValue<*, FIELDVALUE>.bind(binder: Binder<BEAN>): Binder.BindingBuilder<BEAN, FIELDVALUE> {
    var builder = binder.forField(this)
    @Suppress("UNCHECKED_CAST")
    if (this is TextField) builder = builder.withNullRepresentation("" as FIELDVALUE)
    return builder
}

/**
 * A type-safe binding which binds only to a property of given type, found on given bean.
 * @param prop the bean property
 */
fun <BEAN, FIELDVALUE> Binder.BindingBuilder<BEAN, FIELDVALUE>.bind(prop: KMutableProperty1<BEAN, out FIELDVALUE?>): Binder.Binding<BEAN, FIELDVALUE?> =
// oh crap, don't use binding by getter and setter - validations won't work!
// we need to use bind(String) even though that will use undebuggable crappy Java 8 lambdas :-(
//        bind({ bean -> prop.get(bean) }, { bean, value -> prop.set(bean, value) })
        bind(prop.name)

/**
 * A converter that converts between [LocalDate] and [Instant].
 * @property zoneId the time zone id to use.
 */
class LocalDateToInstantConverter(val zoneId: ZoneId = browserTimeZone) : Converter<LocalDate?, Instant?> {
    override fun convertToModel(localDate: LocalDate?, context: ValueContext): Result<Instant?> =
            Result.ok(localDate?.atStartOfDay(zoneId)?.toInstant())

    override fun convertToPresentation(date: Instant?, context: ValueContext): LocalDate? =
            date?.atZone(zoneId)?.toLocalDate()
}

/**
 * A converter that converts between [LocalDateTime] and [Instant].
 * @property zoneId the time zone to use
 */
class LocalDateTimeToInstantConverter(val zoneId: ZoneId = browserTimeZone) : Converter<LocalDateTime?, Instant?> {
    override fun convertToModel(localDate: LocalDateTime?, context: ValueContext): Result<Instant?> =
            Result.ok(localDate?.atZone(zoneId)?.toInstant())

    override fun convertToPresentation(date: Instant?, context: ValueContext): LocalDateTime? =
            date?.atZone(zoneId)?.toLocalDateTime()
}

object DoubleToIntConverter : Converter<Double?, Int?> {
    override fun convertToPresentation(value: Int?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<Int?> = Result.ok(value?.toInt())
}

object DoubleToLongConverter : Converter<Double?, Long?> {
    override fun convertToPresentation(value: Long?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<Long?> = Result.ok(value?.toLong())
}

object DoubleToBigDecimalConverter : Converter<Double?, BigDecimal?> {
    override fun convertToPresentation(value: BigDecimal?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<BigDecimal?> = Result.ok(value?.toBigDecimal())
}

object DoubleToBigIntegerConverter : Converter<Double?, BigInteger?> {
    override fun convertToPresentation(value: BigInteger?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<BigInteger?> {
        val bi = if (value == null) null else BigInteger(value.toLong().toString())
        return Result.ok(bi)
    }
}
