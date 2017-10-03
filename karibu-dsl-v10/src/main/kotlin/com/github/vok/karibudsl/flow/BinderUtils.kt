package com.github.vok.karibudsl.flow

import com.vaadin.data.*
import com.vaadin.data.converter.*
import com.vaadin.ui.common.HasValue
import com.vaadin.ui.textfield.TextField
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

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
        withConverter(StringToIntegerConverter("Can't convert to integer"))

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toDouble(): Binder.BindingBuilder<BEAN, Double?> =
        withConverter(StringToDoubleConverter("Can't convert to decimal number"))

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toLong(): Binder.BindingBuilder<BEAN, Long?> =
        withConverter(StringToLongConverter("Can't convert to integer"))

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigDecimal(): Binder.BindingBuilder<BEAN, BigDecimal?> =
        withConverter(StringToBigDecimalConverter("Can't convert to decimal number"))

fun <BEAN> Binder.BindingBuilder<BEAN, String?>.toBigInteger(): Binder.BindingBuilder<BEAN, BigInteger?> =
        withConverter(StringToBigIntegerConverter("Can't convert to integer"))

val browserTimeZone: ZoneId
    get() {
        // @todo mavi this conversion does not take into account DST for historical dates. Modify accordingly when https://github.com/vaadin/framework/issues/7911 is fixed
        return ZoneId.systemDefault()
    }

fun <BEAN> Binder.BindingBuilder<BEAN, LocalDate?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateToDateConverter(browserTimeZone))

@JvmName("localDateTimeToDate")
fun <BEAN> Binder.BindingBuilder<BEAN, LocalDateTime?>.toDate(): Binder.BindingBuilder<BEAN, Date?> =
        withConverter(LocalDateTimeToDateConverter(browserTimeZone))

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
fun <BEAN, FIELDVALUE> Binder.BindingBuilder<BEAN, FIELDVALUE>.bind(prop: KMutableProperty1<BEAN, FIELDVALUE?>): Binder.Binding<BEAN, FIELDVALUE> =
// oh crap, don't use binding by getter and setter - validations won't work!
// we need to use bind(String) even though that will use undebuggable crappy Java 8 lambdas :-(
//        bind({ bean -> prop.get(bean) }, { bean, value -> prop.set(bean, value) })
        bind(prop.name)

/**
 * Allows you to perform the @Past validation on LocalDate fields.
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = arrayOf(PastDateValidator::class))
@MustBeDocumented
annotation class PastDate(val message: String = "Must be in the past", val groups: Array<KClass<*>> = arrayOf(), val payload: Array<KClass<out Payload>> = arrayOf())

class PastDateValidator : ConstraintValidator<PastDate, LocalDate?> {

    override fun initialize(constraintAnnotation: PastDate) {}

    override fun isValid(`object`: LocalDate?, constraintContext: ConstraintValidatorContext): Boolean {
        if (`object` == null) {
            return true
        }
        return `object` < LocalDate.now()
    }
}