package com.github.vok.karibudsl

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import org.junit.Before
import org.junit.Test
import kotlin.test.expect

/**
 * @author mavi
 */
class AutoViewProviderTest {
    @Before
    fun setupVaadin() = MockVaadin.setup()

    @Test
    fun testParseViewName() {
        expect("foo") { AutoViewProvider.Companion.parseViewName("!foo/25") }
        expect("foo") { AutoViewProvider.Companion.parseViewName("foo") }
        expect("foo") { AutoViewProvider.Companion.parseViewName("foo/") }
    }

    @Test
    fun findAnnotationTest() {
        expect("interface") { MyView::class.java.findAnnotation(AutoView::class.java)!!.value }
        expect("abstractclass") { AbstractView::class.java.findAnnotation(AutoView::class.java)!!.value }
        // see findAnnotation for explanation why this is null
        expect(null) { MyViewInheritingAnnotationFromSuperClass::class.java.findAnnotation(AutoView::class.java)?.value }
        expect(null) { MyViewInheritingAnnotationFromInterface::class.java.findAnnotation(AutoView::class.java)?.value }
    }
}

@AutoView("interface")
interface MyView : View

@AutoView("abstractclass")
open class AbstractView : View {
    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}

class MyViewInheritingAnnotationFromInterface : MyView {
    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
    }
}

class MyViewInheritingAnnotationFromSuperClass: AbstractView()
