package com.github.mvysny.karibudsl.v8.v7compat

import com.github.mvysny.karibudsl.v8.AbstractStringBasedClassList
import com.github.mvysny.karibudsl.v8.ClassList
import com.github.mvysny.karibudsl.v8.VaadinDsl
import com.vaadin.v7.ui.components.calendar.event.CalendarEvent
import java.lang.UnsupportedOperationException

private class CalendarEventClassList(val calendarEvent: CalendarEvent) : AbstractStringBasedClassList() {
    override fun getStyleName(): String = calendarEvent.styleName
    override fun setStyleName(styleNames: Set<String>) {
        throw UnsupportedOperationException("Unfortunately CalendarEvent.styleName can not be changed")
    }
}

/**
 * Returns a mutable set of styles currently present on the component.
 */
val (@VaadinDsl CalendarEvent).styleNames: ClassList get() = CalendarEventClassList(this)
