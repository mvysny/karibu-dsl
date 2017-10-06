package com.github.vok.karibudsl.example.form

import java.io.Serializable
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size

/**
 * A simple information about a person. This example shows also the validation annotations which are automatically picked
 * up by Vaadin's [BeanValidationBinder]. See the [FormView] for the UI code which edits instances of this class.
 * @property fullName the full name of the person, required
 * @property dateOfBirth the date of birth, must be in the past
 * @property maritalStatus marital status
 * @property alive if false, the person is deceased
 * @property comment optional comment
 */
data class Person(
        @field:NotNull
        @field:Size(min = 3, max = 200)
        var fullName: String? = null,

        @field:NotNull
        @field:PastOrPresent
        var dateOfBirth: LocalDate? = null,

        @field:NotNull
        var maritalStatus: MaritalStatus? = null,

        @field:NotNull
        var sex: Sex? = null,

        @field:NotNull
        @field:Email
        var email: String? = null,

        var location: String? = null,

        var phone: String? = null,

        var newsletter: Boolean? = true,

        var newsletterFrequency: NewsletterFrequency? = NewsletterFrequency.Weekly,

        var website: String? = null,

        var shortBio: String? = null,

        var longBio: String? = null

        ) : Serializable {
    companion object {
        fun createRandom(): Person {
            val random = Random()
            return Person(fullName = "Random ${random.nextInt(100)}",
                    dateOfBirth = LocalDate.now().minusYears(random.nextInt(100).toLong()).plusDays(random.nextInt(365).toLong()),
                    maritalStatus = MaritalStatus.values().random,
                    sex = Sex.values().random,
                    email = "amet@consectetur.com",
                    location = "Quid, Securi",
                    shortBio = "Quis aute iure reprehenderit in voluptate velit esse. Cras mattis iudicium purus sit amet fermentum.",
                    longBio = """Integer legentibus erat a ante historiarum dapibus. Vivamus sagittis lacus vel augue laoreet rutrum faucibus. A communi observantia non est recedendum. Morbi fringilla convallis sapien, id pulvinar odio volutpat. Ab illo tempore, ab est sed immemorabili. Quam temere in vitiis, legem sancimus haerentia.<br/><br/>
Morbi odio eros, volutpat ut pharetra vitae, lobortis sed nibh. Quam diu etiam furor iste tuus nos eludet? Cum sociis natoque penatibus et magnis dis parturient. Quam diu etiam furor iste tuus nos eludet? Tityre, tu patulae recubans sub tegmine fagi dolor.<br/><br/>
Curabitur blandit tempus ardua ridiculus sed magna. Phasellus laoreet lorem vel dolor tempus vehicula. Etiam habebis sem dicantur magna mollis euismod. Hi omnes lingua, institutis, legibus inter se differunt."""
            )
        }
    }
}

enum class MaritalStatus {
    Single,
    Married,
    Divorced,
    Widowed
}

enum class Sex {
    Male,
    Female
}

val <T> Array<T>.random: T get() = get(Random().nextInt(size))

enum class NewsletterFrequency {
    Daily,
    Weekly,
    Monthly
}
