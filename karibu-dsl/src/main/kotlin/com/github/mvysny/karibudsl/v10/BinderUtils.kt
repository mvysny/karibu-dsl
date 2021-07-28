package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.data.binder.*
import com.vaadin.flow.data.converter.*
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.validator.*
import java.lang.reflect.Method
import java.math.BigDecimal
import java.math.BigInteger
import java.time.*
import java.util.*
import kotlin.reflect.KMutableProperty1
import java.time.LocalDateTime

/**
 * Trims the user input string before storing it into the underlying property data source. Vital for mobile-oriented apps:
 * Android keyboard often adds whitespace to the end of the text when auto-completion occurs. Imagine storing a username ending with a space upon registration:
 * such person can no longer log in from his PC unless he explicitly types in the space.
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
                // workaround for https://github.com/vaadin/framework/issues/8664
                return value ?: ""
            }
        })

/**
 * Converts [String] from [textField] to [Int]-typed bean field.
 *
 * It's probably better to use [integerField] directly instead.
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toInt(): Binder.BindingBuilder<BEAN, Int?> =
        withConverter(StringToIntegerConverter(karibuDslI18n("cantConvertToInteger")))

/**
 * Converts [Double] from [numberField] to [Int]-typed bean field.
 *
 * It's probably better to use [integerField] directly instead.
 */
@JvmName("doubleToInt")
public fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toInt(): Binder.BindingBuilder<BEAN, Int?> =
        withConverter(DoubleToIntConverter)

/**
 * Converts [String] from [textField] to [Double]-typed bean field.
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toDouble(
        errorMessage: String = karibuDslI18n("cantConvertToDecimal")
): Binder.BindingBuilder<BEAN, Double?> =
        withConverter(StringToDoubleConverter(errorMessage))

/**
 * Converts [String] from [textField] to [Long]-typed bean field.
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toLong(
        errorMessage: String = karibuDslI18n("cantConvertToInteger")
): Binder.BindingBuilder<BEAN, Long?> =
        withConverter(StringToLongConverter(errorMessage))

/**
 * Converts [Double] from [numberField] to [Long]-typed bean field.
 *
 * It's probably better to use [integerField] and int-to-long conversion instead.
 */
@JvmName("doubleToLong")
public fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toLong(): Binder.BindingBuilder<BEAN, Long?> =
        withConverter(DoubleToLongConverter)

/**
 * Converts [Int] from [integerField] to [Long]-typed bean field.
 */
@JvmName("intToLong")
public fun <BEAN> Binder.BindingBuilder<BEAN, Int?>.toLong(): Binder.BindingBuilder<BEAN, Long?> =
    withConverter(IntToLongConverter)

/**
 * Converts [String] from [textField] to [BigDecimal]-typed bean field.
 *
 * It's probably better to use [bigDecimalField] directly instead.
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigDecimal(
        errorMessage: String = karibuDslI18n("cantConvertToDecimal")
): Binder.BindingBuilder<BEAN, BigDecimal?> =
        withConverter(StringToBigDecimalConverter(errorMessage))

/**
 * Converts [Double] from [numberField] to [BigDecimal]-typed bean field.
 *
 * It's probably better to use [bigDecimalField] directly instead.
 */
@JvmName("doubleToBigDecimal")
public fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toBigDecimal(): Binder.BindingBuilder<BEAN, BigDecimal?> =
        withConverter(DoubleToBigDecimalConverter)

/**
 * Converts [String] from [textField] to [BigInteger]-typed bean field.
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigInteger(): Binder.BindingBuilder<BEAN, BigInteger?> =
        withConverter(StringToBigIntegerConverter(karibuDslI18n("cantConvertToInteger")))

/**
 * Converts [Double] from [numberField] to [BigInteger]-typed bean field.
 */
@JvmName("doubleToBigInteger")
public fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.toBigInteger(): Binder.BindingBuilder<BEAN, BigInteger?> =
        withConverter(DoubleToBigIntegerConverter)

/**
 * Converts [LocalDate] from [datePicker] to [Date]-typed bean field. Uses [browserTimeZone].
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateToDateConverter(browserTimeZone))

/**
 * Converts [LocalDateTime] from [dateTimePicker] to [Date]-typed bean field. Uses [browserTimeZone].
 */
@JvmName("localDateTimeToDate")
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateTimeToDateConverter(browserTimeZone))

/**
 * Converts [LocalDate] from [datePicker] to [Instant]-typed bean field. Uses [browserTimeZone].
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toInstant(): Binder.BindingBuilder<BEAN, Instant?> =
        withConverter(LocalDateToInstantConverter(browserTimeZone))

/**
 * Converts [LocalDateTime] from [dateTimePicker] to [Instant]-typed bean field. Uses [browserTimeZone].
 */
@JvmName("localDateTimeToInstant")
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toInstant(): Binder.BindingBuilder<BEAN, Instant?> =
        withConverter(LocalDateTimeToInstantConverter(browserTimeZone))

/**
 * Converts [LocalDate] from [datePicker] to [Calendar]-typed bean field. Uses [browserTimeZone].
 */
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toCalendar(): Binder.BindingBuilder<BEAN, Calendar?> =
    toDate().withConverter(DateToCalendarConverter)

/**
 * Converts [LocalDateTime] from [dateTimePicker] to [Calendar]-typed bean field. Uses [browserTimeZone].
 */
@JvmName("localDateTimeToCalendar")
public fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toCalendar(): Binder.BindingBuilder<BEAN, Calendar?> =
    toDate().withConverter(DateToCalendarConverter)

/**
 * Allows you to create [BeanValidationBinder] like this: `beanValidationBinder<Person>()` instead of `BeanValidationBinder(Person::class.java)`
 */
public inline fun <reified T : Any> beanValidationBinder(): BeanValidationBinder<T> = BeanValidationBinder(T::class.java)

/**
 * Allows you to bind the component directly in the component's definition. E.g.
 * ```
 * textField("Name:") {
 *   bind(binder).bind(Person::name)
 * }
 * ```
 */
public fun <BEAN, FIELDVALUE> HasValue<*, FIELDVALUE>.bind(binder: Binder<BEAN>): Binder.BindingBuilder<BEAN, FIELDVALUE> {
    var builder: Binder.BindingBuilder<BEAN, FIELDVALUE> = binder.forField(this)

    // fix NPE for TextField and TextArea by having a converter which converts null to "" and back.
    @Suppress("UNCHECKED_CAST")
    if (this is TextField || this is TextArea || this is EmailField) {
        builder = builder.withNullRepresentation("" as FIELDVALUE)
    }
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
 * A converter that converts from [LocalDate] [datePicker] to [Instant] bean field.
 * @property zoneId the time zone id to use.
 */
public class LocalDateToInstantConverter(public val zoneId: ZoneId = browserTimeZone) : Converter<LocalDate?, Instant?> {
    override fun convertToModel(localDate: LocalDate?, context: ValueContext): Result<Instant?> =
            Result.ok(localDate?.atStartOfDay(zoneId)?.toInstant())

    override fun convertToPresentation(date: Instant?, context: ValueContext): LocalDate? =
            date?.atZone(zoneId)?.toLocalDate()
}

/**
 * A converter that converts from [LocalDateTime] [dateTimePicker] to [Instant] bean field.
 * @property zoneId the time zone to use
 */
public class LocalDateTimeToInstantConverter(public val zoneId: ZoneId = browserTimeZone) : Converter<LocalDateTime?, Instant?> {
    override fun convertToModel(localDate: LocalDateTime?, context: ValueContext): Result<Instant?> =
            Result.ok(localDate?.atZone(zoneId)?.toInstant())

    override fun convertToPresentation(date: Instant?, context: ValueContext): LocalDateTime? =
            date?.atZone(zoneId)?.toLocalDateTime()
}

/**
 * Converts [Double] from [numberField] to [Int]-typed bean field.
 *
 * It's probably better to use [integerField] directly instead.
 */
public object DoubleToIntConverter : Converter<Double?, Int?> {
    override fun convertToPresentation(value: Int?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<Int?> = Result.ok(value?.toInt())
}

/**
 * Converts [Double] from [numberField] to [Long]-typed bean field.
 *
 * It's probably better to use [integerField] and int-to-long conversion instead.
 */
public object DoubleToLongConverter : Converter<Double?, Long?> {
    override fun convertToPresentation(value: Long?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<Long?> = Result.ok(value?.toLong())
}

/**
 * Converts [Int] from [integerField] to [Long]-typed bean field.
 */
public object IntToLongConverter : Converter<Int?, Long?> {
    override fun convertToPresentation(value: Long?, context: ValueContext?): Int? = value?.toInt()
    override fun convertToModel(value: Int?, context: ValueContext?): Result<Long?> = Result.ok(value?.toLong())
}

/**
 * Converts [Double] from [numberField] to [BigDecimal]-typed bean field.
 *
 * It's probably better to use [bigDecimalField] directly instead.
 */
public object DoubleToBigDecimalConverter : Converter<Double?, BigDecimal?> {
    override fun convertToPresentation(value: BigDecimal?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<BigDecimal?> = Result.ok(value?.toBigDecimal())
}

/**
 * Converts [Double] from [numberField] to [BigInteger]-typed bean field.
 */
public object DoubleToBigIntegerConverter : Converter<Double?, BigInteger?> {
    override fun convertToPresentation(value: BigInteger?, context: ValueContext?): Double? = value?.toDouble()
    override fun convertToModel(value: Double?, context: ValueContext?): Result<BigInteger?> {
        val bi = if (value == null) null else BigInteger(value.toLong().toString())
        return Result.ok(bi)
    }
}

/**
 * Converts [Date] to [Calendar]-typed bean field. Append to [LocalDateTimeToDateConverter] or
 * [LocalDateToDateConverter] to allow for LocalDate-to-Calendar conversions.
 */
public object DateToCalendarConverter : Converter<Date?, Calendar?> {
    override fun convertToModel(value: Date?, context: ValueContext?): Result<Calendar?> = when (value) {
        null -> Result.ok(null)
        else -> {
            val calendar = Calendar.getInstance()
            calendar.time = value
            Result.ok(calendar)
        }
    }

    override fun convertToPresentation(value: Calendar?, context: ValueContext?): Date? = value?.time
}

public class StringNotBlankValidator(public val errorMessage: String = "must not be blank") : Validator<String?> {
    override fun apply(value: String?, context: ValueContext): ValidationResult = when {
        value.isNullOrBlank() -> ValidationResult.error(errorMessage)
        else -> ValidationResult.ok()
    }
}

public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.validateNotBlank(
        errorMessage: String = "must not be blank"
): Binder.BindingBuilder<BEAN, String?> =
        withValidator(StringNotBlankValidator(errorMessage))

public fun <BEAN> Binder.BindingBuilder<BEAN, String?>.validEmail(
        errorMessage: String = "must be a valid email address"
): Binder.BindingBuilder<BEAN, String?> =
        withValidator(EmailValidator(errorMessage))

public fun <BEAN> Binder.BindingBuilder<BEAN, Float?>.validateInRange(range: ClosedRange<Float>): Binder.BindingBuilder<BEAN, Float?> =
        withValidator(FloatRangeValidator("must be in $range", range.start, range.endInclusive))

@JvmName("validateIntInRange")
public fun <BEAN> Binder.BindingBuilder<BEAN, Int?>.validateInRange(range: IntRange): Binder.BindingBuilder<BEAN, Int?> =
        withValidator(IntegerRangeValidator("must be in $range", range.start, range.endInclusive))

@JvmName("validateLongInRange")
public fun <BEAN> Binder.BindingBuilder<BEAN, Long?>.validateInRange(range: LongRange): Binder.BindingBuilder<BEAN, Long?> =
        withValidator(LongRangeValidator("must be in $range", range.start, range.endInclusive))

@JvmName("validateDoubleInRange")
public fun <BEAN> Binder.BindingBuilder<BEAN, Double?>.validateInRange(range: ClosedRange<Double>): Binder.BindingBuilder<BEAN, Double?> =
        withValidator(DoubleRangeValidator("must be in $range", range.start, range.endInclusive))

@JvmName("validateBigIntegerInRange")
public fun <BEAN> Binder.BindingBuilder<BEAN, BigInteger?>.validateInRange(range: ClosedRange<BigInteger>): Binder.BindingBuilder<BEAN, BigInteger?> =
        withValidator(BigIntegerRangeValidator("must be in $range", range.start, range.endInclusive))

@JvmName("validateBigDecimalInRange")
public fun <BEAN> Binder.BindingBuilder<BEAN, BigDecimal?>.validateInRange(range: ClosedRange<BigDecimal>): Binder.BindingBuilder<BEAN, BigDecimal?> =
        withValidator(BigDecimalRangeValidator("must be in $range", range.start, range.endInclusive))

/**
 * Guesses whether the binder has been configured with read-only.
 *
 * Since Binder doesn't remember whether it is read-only, we have to guess.
 */
@Suppress("UNCHECKED_CAST")
public val Binder<*>.guessIsReadOnly: Boolean
    get() {
        val bindingsGetter: Method = Binder::class.java.getDeclaredMethod("getBindings")
        bindingsGetter.isAccessible = true
        val bindings: Collection<Binder.Binding<*, *>> = bindingsGetter.invoke(this) as Collection<Binder.Binding<*, *>>
        return bindings.any { it.setter != null && it.isReadOnly }
    }
