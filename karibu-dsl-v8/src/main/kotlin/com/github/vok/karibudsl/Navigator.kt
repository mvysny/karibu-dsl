package com.github.vok.karibudsl

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.navigator.ViewProvider
import com.vaadin.ui.UI
import io.michaelrocks.bimap.HashBiMap
import io.michaelrocks.bimap.MutableBiMap
import org.atmosphere.util.annotation.AnnotationDetector
import org.slf4j.LoggerFactory
import java.net.URLDecoder
import java.net.URLEncoder
import javax.servlet.ServletContainerInitializer
import javax.servlet.ServletContext
import javax.servlet.annotation.HandlesTypes

private fun String.upperCamelToLowerHyphen(): String {
    val sb = StringBuilder()
    for (i in 0..this.length - 1) {
        var c = this[i]
        if (Character.isUpperCase(c)) {
            c = Character.toLowerCase(c)
            if (shouldPrependHyphen(i)) {
                sb.append('-')
            }
        }
        sb.append(c)
    }
    return sb.toString()
}

private fun String.shouldPrependHyphen(i: Int): Boolean =
    if (i == 0) {
        // Never put a hyphen at the beginning
        false
    } else if (!Character.isUpperCase(this[i - 1])) {
        // Append if previous char wasn't upper case
        true
    } else if (i + 1 < this.length && !Character.isUpperCase(this[i + 1])) {
        // Append if next char isn't upper case
        true
    } else {
        false
    }


/**
 * Internal class which enumerates views. Do not use directly - instead, just add [autoViewProvider] to your [com.vaadin.navigator.Navigator],
 * see [autoViewProvider] for more details.
 */
@HandlesTypes(AutoView::class)
class AutoViewProvider : ServletContainerInitializer {
    companion object : ViewProvider {
        override fun getViewName(viewAndParameters: String): String? {
            val viewName = parseViewName(viewAndParameters)
            return if (viewNameToClass.containsKey(viewName)) viewName else null
        }

        internal fun parseViewName(viewAndParameters: String) : String {
            val viewName = viewAndParameters.removePrefix("!")
            val firstSlash = viewName.indexOf('/')
            return viewName.substring(0..(if(firstSlash < 0) viewName.length - 1 else firstSlash - 1))
        }

        override fun getView(viewName: String): View? = viewNameToClass.get(viewName)?.newInstance()

        /**
         * Maps view name to the view class.
         */
        private val viewNameToClass: MutableBiMap<String, Class<out View>> = HashBiMap()

        fun <T: View> getMapping(clazz: Class<T>): String =
                viewNameToClass.inverse[clazz] ?: throw IllegalArgumentException("$clazz is not registered as a view class. Available view classes: ${viewNameToClass.values.joinToString(transform = { it.name })}")

        @JvmStatic
        private val log = LoggerFactory.getLogger(AutoViewProvider::class.java)
    }

    private fun Class<*>.toViewName(): String {
        val annotation = requireNotNull(findAnnotation(AutoView::class.java)) { "Missing @AutoView annotation on $this" }
        val name = annotation.value
        return if (name == VIEW_NAME_USE_DEFAULT) simpleName.removeSuffix("View").upperCamelToLowerHyphen() else name
    }

    override fun onStartup(c: MutableSet<Class<*>>?, ctx: ServletContext?) {
        c?.forEach { viewClass ->
            if (viewClass.isInterface) {
                // the auto-discovery mechanism doesn't support classes implementing interfaces annotated with @AutoView
                log.error("@AutoView-annotated classes must not be interfaces: $viewClass is an interface")
            } else if (viewClass.isAnnotation) {
                // the auto-discovery mechanism doesn't support transitive annotations
                log.error("@AutoView-annotated classes must not be annotations: $viewClass is an annotation")
            } else {
                val viewName = viewClass.toViewName()
                if (viewNameToClass.containsKey(viewName) && viewClass != viewNameToClass[viewName]) {
                    throw IllegalStateException("Views $viewClass and ${viewNameToClass[viewName]} are trying to register under a common name '$viewName'. Please annotate one of those views with the @AutoView annotation and specify a different name")
                }
                check(View::class.java.isAssignableFrom(viewClass)) { "$viewClass is annotated with @AutoView yet it does not implement Vaadin ${View::class.java.name}" }
                viewNameToClass.forcePut(viewName, viewClass.asSubclass(View::class.java))
            }
        }
    }
}

/**
 * Set this view provider to the [com.vaadin.navigator.Navigator]:
 *
 * `navigator.addProvider(autoViewProvider)`
 *
 * The view provider will auto-discover all of your views and will create names for them, see [AutoView] for more details.
 * Only views annotated with [AutoView] are discovered - this is to avoid automagically exposing all of views packaged in all jars,
 * even unwanted ones.
 *
 * To navigate to a view, just call the [navigateToView] helper method which will generate the correct URI fragment and will navigate.
 * You can parse the parameters back later on in your [View.enter], by calling `event.parameterList`.
 */
val autoViewProvider = AutoViewProvider

private const val VIEW_NAME_USE_DEFAULT = "USE_DEFAULT"

fun navigateToView(view: Class<out View>, vararg params: String) {
    require(view.findAnnotation(AutoView::class.java) != null) { "$view is not annotated with @AutoView; unfortunately all views must be annotated directly, otherwise ServletContainerInitializer won't auto-discover it for us" }
    val mapping = AutoViewProvider.getMapping(view)
    val param = if (params.isEmpty()) "" else params.map { URLEncoder.encode(it, "UTF-8") }.joinToString("/", "/")
    val navigator = UI.getCurrent().navigator ?: throw IllegalStateException("Navigator not set in your ${UI.getCurrent().javaClass.simpleName}. Just add the following code to your UI.init() method: { navigator = Navigator(this, content as ViewDisplay); navigator.addProvider(autoViewProvider) }")
    navigator.navigateTo("$mapping$param")
}

internal fun <T: Annotation> Class<*>.findAnnotation(ac: Class<T>): T? {
    // unfortunately, the class discovery algorithm of ServletContainerInitializer+@HandlesTypes is quite weak.
    // it will not handle transitive annotations; for example it will not handle classes implementing interfaces annotated with @AutoView
    // therefore, we'll just use the getAnnotation()
    return getAnnotation(ac)
}

/**
 * Asks the current UI navigator to navigate to given view.
 *
 * As a convention, you should introduce a static method `navigateTo(params)` to all of your views,
 * which will then simply call this function.
 * @param V the class of the view, not null.
 * @param params an optional list of string params. The View will receive the params via
 * [View.enter]'s [ViewChangeListener.ViewChangeEvent], use [parameterList] to parse them back in.
 */
inline fun <reified V : View> navigateToView(vararg params: String) = navigateToView(V::class.java, *params)

/**
 * Parses the parameters back from the URI fragment. See [navigateToView] for details. Call in [ViewChangeListener.ViewChangeEvent] provided to you in the
 * [View.enter] method.
 *
 * Note that the parameters are not named - instead, this is a simple list of values.
 *
 * To obtain a particular parameter or null if the URL has no such parameter, just call [Map.get].
 * @return list of parameters, empty if there are no parameters.
 */
val ViewChangeListener.ViewChangeEvent.parameterList: Map<Int, String>
    get() = parameters.trim().split('/').map { URLDecoder.decode(it, "UTF-8")!! } .filterNot(String::isEmpty).mapIndexed { i, param -> i to param }.toMap()

/**
 * Annotate your views with this annotation, and the [autoViewProvider] will auto-discover them and register them.
 *
 * By default the view will be assigned a colon-separated name, derived from your view class name. The trailing View is dropped.
 * For example, UserListView will be mapped to user-list. You can attach this annotation to a view, to modify this behavior.
 * It is often a good practice to mark one particular view as the root view, by annotating the class with `AutoView("")`.
 * This view will be shown initially when the user enters your application.
 *
 * All views must directly be annotated with this annotation. This annotation does not apply to subclasses; this annotation does not apply to
 * implementors of an annotated interface. This is an unfortunate limitation of ServletContainerInitializer.
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class AutoView(val value: String = VIEW_NAME_USE_DEFAULT)

/**
 * Auto-discovers views and register them to [autoViewProvider]. Can be called multiple times.
 *
 * DON'T CALL THIS in Servlet environment - Servlet container is responsible to discover views.
 * This function is intended to be used in tests only.
 * @param packageName set the package name for the detector to be faster; or provide null to scan the whole classpath, but this is quite slow.
 */
fun autoDiscoverViews(packageName: String? = null) {
    val entities = mutableListOf<Class<*>>()
    val detector = AnnotationDetector(object : AnnotationDetector.TypeReporter {
        override fun reportTypeAnnotation(annotation: Class<out Annotation>?, className: String?) {
            entities.add(Class.forName(className))
        }

        override fun annotations(): Array<out Class<out Annotation>> = arrayOf(AutoView::class.java)
    })
    if (packageName == null) {
        detector.detect()
    } else {
        detector.detect(packageName)
    }

    println("Auto-discovered views: ${entities.joinToString { it.simpleName }}")
    AutoViewProvider().onStartup(entities.toMutableSet(), null)
}
