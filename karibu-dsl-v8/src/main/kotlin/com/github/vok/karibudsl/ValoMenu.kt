package com.github.vok.karibudsl

import com.vaadin.icons.VaadinIcons
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewDisplay
import com.vaadin.server.Resource
import com.vaadin.server.Responsive
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme

/**
 * A main screen with a responsive Valo menu and a view placeholder, where the View contents will go upon navigation.
 * You should set this class as the contents of your UI and set it to the [Navigator] as follows:
 * ```
 * class UI {
 *   override fun init(request: VaadinRequest?) {
 *     val content = valoMenu {
 *       appTitle = "<strong>Penny's Shop</strong>"
 *       userMenu {
 *         item("John Doe", ClassResource("profilepic300px.jpg")) {
 *           item("Edit Profile")
 *           item("Preferences")
 *           addSeparator()
 *           item("Sign Out")
 *         }
 *       }
 *       menuButton(VaadinIcons.WALLET, "Shoes")
 *       menuButton(VaadinIcons.GLASS, "Liquer", badge = "3")
 *       separator("Other")
 *     }
 *     navigator = Navigator(this, content as ViewDisplay)
 *     ...
 *   }
 * }
 * ```
 */
class ValoMenu: HorizontalLayout(), ViewDisplay {
    /**
     * Tracks the registered menu items associated with view; when a view is shown, highlight appropriate menu item button.
     */
    private val views = mutableMapOf<Class<out View>, MenuButton>()

    private val menuArea: CssLayout
    private lateinit var menu: CssLayout
    /**
     * The current view placeholder - all views will be placed here. By default a full-screen CssLayout which scrolls its contents;
     * you can set a different placeholder component to replace the original one.
     *
     * Note: the old placeholder is removed from [ValoMenu], but the new one is not added. This supports more complex placeholder
     * layouts where the placeholder is not directly nested in the [ValoMenu] layout.
     * When you add a component to [ValoMenu] to act as a placeholder (or to host a placeholder), don't forget to set the following on the newly added component:
     *
     * ```
     * primaryStyleName = "valo-content"; setSizeFull(); expandRatio = 1f
     * ```
     */
    var viewPlaceholder: HasComponents = CssLayout()
    set(value) {
        removeComponent(field)
        field = value
    }

    private lateinit var titleLabel: Label
    /**
     * The application title. Warning - may contain html; html is not escaped and is sent to the browser as-is.
     */
    var appTitle: String
    get() = titleLabel.value
    set(value) { titleLabel.html(value) }

    private lateinit var userMenu: MenuBar

    fun userMenu(block: MenuBar.()->Unit) = userMenu.block()

    private lateinit var navigationButtonLayout: CssLayout
    init {
        setSizeFull(); isSpacing = false; styleName = "valo-menu-responsive"
        Responsive.makeResponsive(this)

        menuArea = cssLayout {
            primaryStyleName = ValoTheme.MENU_ROOT
            addStyleNames("sidebar", "valo-menu-part", "no-vertical-drag-hints", "no-horizontal-drag-hints")
            w = wrapContent; h = fillParent

            menu = cssLayout { // menu
                styleName = ValoTheme.MENU_PART
                horizontalLayout {
                    w = fillParent; isSpacing = false; defaultComponentAlignment = Alignment.MIDDLE_LEFT
                    styleName = ValoTheme.MENU_TITLE
                    titleLabel = label {
                        w = wrapContent
                        expandRatio = 1f
                    }
                }
                button("Menu") { // only visible when the top bar is shown
                    onLeftClick {
                        menu.toggleStyleName("valo-menu-visible", !menu.hasStyleName("valo-menu-visible"))
                    }
                    addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL, "valo-menu-toggle")
                    icon = VaadinIcons.MENU
                }
                userMenu = menuBar { // the user menu, settings
                    styleName = "user-menu"
                }
                // the navigation buttons
                navigationButtonLayout = cssLayout {
                    primaryStyleName = "valo-menuitems"
                }
            }
        }

        viewPlaceholder = cssLayout {
            primaryStyleName = "valo-content"; setSizeFull(); expandRatio = 1f
            addStyleName("v-scrollable")
        }
    }

    /**
     * Creates a separator between menu items, with given [caption] and an optional [badge]. Returns the separator component.
     */
    fun section(caption: String, badge: String? = null, block: Label.()->Unit = {}): Label = navigationButtonLayout.label {
        if (badge == null) {
            this.caption = caption
        } else {
            html("""$caption <span class="valo-menu-badge">$badge</span>""")
        }
        primaryStyleName = ValoTheme.MENU_SUBTITLE
        w = wrapContent
        addStyleName(ValoTheme.LABEL_H4)
        block()
    }

    /**
     * Registers a button to a menu with given [icon] and [caption], which optionally launches given [view].
     * @param badge optional badge which is displayed in the button's top-right corner. Usually this is a number, showing number of notifications or such.
     * @param view optional view; if not null, clicking this menu button will launch this view with no parameters; also the button will be marked selected
     * when the view is shown.
     */
    fun menuButton(caption: String, icon: Resource? = null, badge: String? = null, view: Class<out View>? = null, block: MenuButton.()->Unit = {}): MenuButton = navigationButtonLayout.menuButton {
        this.icon = icon
        this.badge = badge
        this.caption = caption
        if (view != null) {
            onLeftClick { navigateToView(view) }
            views[view] = this
        }
        block()
    }

    override fun showView(view: View) {
        // show the view itself
        viewPlaceholder.removeAllComponents()
        viewPlaceholder.addChild(view as Component)

        // make the appropriate menu button selected, to show the current view
        views.values.forEach { it.isSelected = false }
        views[view.javaClass as Class<*>]?.isSelected = true
    }
}

class MenuButton : Button() {
    init {
        primaryStyleName = ValoTheme.MENU_ITEM
    }

    /**
     * The button badge (usually a number, say, number of new messages), attached to the menu.
     */
    var badge: String? = null
    set(value) {
        field = value; updateCaption()
    }
    private var caption2: String? = null
    /**
     * Whether the menu button is drawn as selected.
     */
    var isSelected: Boolean
    get() = hasStyleName("selected")
    set(value) { toggleStyleName("selected", value) }

    override fun getCaption(): String? = caption2

    override fun setCaption(caption: String?) {
        caption2 = caption
        updateCaption()
    }

    private fun updateCaption() {
        isCaptionAsHtml = badge != null
        super.setCaption(if (badge != null) """$caption <span class="valo-menu-badge">$badge</span>""" else caption)
    }
}

private fun (@VaadinDsl HasComponents).menuButton(block: (@VaadinDsl MenuButton).()->Unit = {}) = init(MenuButton(), block)

fun (@VaadinDsl UI).valoMenu(block: (@VaadinDsl ValoMenu).()->Unit = {}): ValoMenu {
    Responsive.makeResponsive(this)
    return init(ValoMenu(), block)
}
