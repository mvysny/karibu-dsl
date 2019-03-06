@file:Suppress("DEPRECATION")

package com.github.mvysny.karibudsl.v8

import com.vaadin.shared.Registration
import com.vaadin.ui.*

@VaadinDsl
fun (@VaadinDsl HasComponents).popupViewOld(small: String? = null, block: (@VaadinDsl PopupView).() -> Unit = {}): PopupView {
    val result = init(PopupView(SimpleContent.EMPTY), block)
    if (small != null) result.minimizedValueAsHTML = small
    return result
}

@Deprecated("Use KPopupView")
data class SimpleContent(val small: String, val large: Component) : PopupView.Content {
    companion object {
        val EMPTY = SimpleContent("", Label(""))
    }

    constructor(content: PopupView.Content) : this(content.minimizedValueAsHTML, content.popupComponent)

    override fun getPopupComponent(): Component? = large

    override fun getMinimizedValueAsHTML() = small
}

@Deprecated("Use KPopupView")
private var (@VaadinDsl PopupView).simpleContent: SimpleContent
    get() {
        val content = content
        return content as? SimpleContent ?: SimpleContent(content)
    }
    set(value) {
        content = value
    }

/**
 * Allows you to set the popup component directly, without changing [minimizedValueAsHTML]
 */
@Deprecated("Use KPopupView")
var (@VaadinDsl PopupView).popupComponent: Component
    get() = content.popupComponent
    set(value) {
        content = simpleContent.copy(large = value)
    }

/**
 * Allows you to set the minimized text directly, without changing [popupComponent]
 */
@Deprecated("Use KPopupView")
var (@VaadinDsl PopupView).minimizedValueAsHTML: String
    get() = content.minimizedValueAsHTML
    set(value) {
        content = simpleContent.copy(small = value)
    }

/**
 * A replacement for broken [PopupView]. It has two modes of construction: with eager contents:
 * ```
 * popupView {
 *   verticalLayout { ... }
 * }
 * ```
 * With lazy-initialized contents (initialized after the popup is shown for the first time):
 * ```
 * popupView {
 *   lazy {
 *     verticalLayout { ... }
 *   }
 * }
 * ```
 */
class KPopupView : Composite(), SpecialContainer {
    private val popup = PopupView()
    private var popupContents: Component? = null
    private var popupContentsLazyInitializer: (() -> Component)? = null

    init {
        compositionRoot = popup
        popup.content = object : PopupView.Content {
            override fun getPopupComponent(): Component {
                if (popupContents == null) {
                    val i = checkNotNull(popupContentsLazyInitializer) { "No contents has been set to this KPopupView, neither directly nor via the 'lazy' function" }
                    popupContents = i()
                }
                return popupContents!!
            }

            override fun getMinimizedValueAsHTML(): String {
                return this@KPopupView.minimizedValueAsHTML
            }
        }
    }

    var minimizedValueAsHTML: String = ""
        set(value) {
            field = value
            popup.markAsDirty() // to pick up the new value of this property
        }

    override fun addComponent(component: Component) {
        check(popupContents == null) { "This component can only host one child" }
        popupContents = component
    }

    override fun removeComponent(component: Component) {
        if (popupContents == component) {
            popupContents = null
        }
    }

    /**
     * Registers a [block] which produces popup contents lazily, when the popup is shown for the first time. The block
     * is called at most once.
     *
     * Example of use:
     * ```
     * popupView {
     *   lazy {
     *     verticalLayout { ... }
     *   }
     * }
     * ```
     */
    @VaadinDsl
    fun lazy(block: (@VaadinDsl HasComponents).() -> Unit) {
        check(popupContentsLazyInitializer == null) { "lazy initializer has already been set" }
        check(popupContents == null) { "popup contents had been already set eagerly" }
        popupContentsLazyInitializer = {
            val panel = Panel()
            panel.block()
            requireNotNull(panel.content) { "The lazy initializer have not added any component" }
        }
    }

    /**
     * Gets/sets the visibility of the popup. Does not hide the minimal
     * representation.
     */
    var isPopupVisible: Boolean
        get() = popup.isPopupVisible
        set(value) {
            popup.isPopupVisible = value
        }

    /**
     * Should the popup automatically hide when the user takes the mouse cursor
     * out of the popup area? If this is false, the user must click outside the
     * popup to close it. The default is true.
     */
    var isHideOnMouseOut: Boolean
        get() = popup.isHideOnMouseOut
        set(value) {
            popup.isHideOnMouseOut = value
        }

    /**
     * Add a listener that is called whenever the visibility of the popup is
     * changed.
     * @param listener the listener to add, not null
     * @return a registration object for removing the listener
     */
    fun addPopupVisibilityListener(listener: PopupView.PopupVisibilityListener): Registration = popup.addPopupVisibilityListener(listener)
}

@VaadinDsl
fun (@VaadinDsl HasComponents).popupView(minimizedValueAsHTML: String = "", block: (@VaadinDsl KPopupView).() -> Unit = {}): KPopupView = init(KPopupView()) {
    this.minimizedValueAsHTML = minimizedValueAsHTML
    block()
}
