package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.page.ExtendedClientDetails
import com.vaadin.flow.server.VaadinSession
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

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
val browserTimeZone: ZoneId
    get() = extendedClientDetails?.timeZone ?: ZoneId.of("UTC")

/**
 * Returns the current date and time at browser's current time zone.
 */
val ExtendedClientDetails.currentDateTime: LocalDateTime
    get() =
        LocalDateTime.now(ZoneOffset.ofTotalSeconds(timezoneOffset / 1000))
