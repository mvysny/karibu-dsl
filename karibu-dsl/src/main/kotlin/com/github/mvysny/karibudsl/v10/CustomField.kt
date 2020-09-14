package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.dom.Element
import java.lang.reflect.Method

private val customFieldAddMethod: Method = CustomField::class.java.getDeclaredMethod("add", Array<Component>::class.java).apply {
    isAccessible = true
}

/**
 * Allows you to set the contents of your [CustomField] as follows:
 * ```
 * class DateRangePopup: CustomField<DateInterval>() {
 *   private val content: Button = content {
 *     button {
 *       onLeftClick {
 *         isDialogVisible = !isDialogVisible
 *       }
 *     }
 *   }
 * }
 * ```
 */
public fun <T : Component> CustomField<*>.content(block: HasComponents.() -> T): T {
    val dummy = object : HasComponents {
        override fun getElement(): Element = throw UnsupportedOperationException("Not expected to be called")
        override fun add(vararg components: Component) {
            customFieldAddMethod.invoke(this@content, components)
        }
    }
    return dummy.block()
}
