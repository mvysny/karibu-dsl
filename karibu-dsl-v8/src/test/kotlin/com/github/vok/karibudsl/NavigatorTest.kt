package com.github.vok.karibudsl

import com.github.karibu.testing.MockVaadin
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import kotlin.test.expect

class AutoViewProviderTest : DynaTest({
    beforeEach { MockVaadin.setup() }

    test("ParseViewName") {
        expect("foo") { AutoViewProvider.parseViewName("!foo/25") }
        expect("foo") { AutoViewProvider.parseViewName("foo") }
        expect("foo") { AutoViewProvider.parseViewName("foo/") }
    }

    test("findAnnotation") {
        expect("interface") { MyView::class.java.findAnnotation(AutoView::class.java)!!.value }
        expect("abstractclass") { AbstractView::class.java.findAnnotation(AutoView::class.java)!!.value }
        // see findAnnotation for explanation why this is null
        expect(null) { MyViewInheritingAnnotationFromSuperClass::class.java.findAnnotation(AutoView::class.java)?.value }
        expect(null) { MyViewInheritingAnnotationFromInterface::class.java.findAnnotation(AutoView::class.java)?.value }
    }

    test("AutoViewDiscovery") {
        autoDiscoverViews("com.github")
        expect("abstractclass") { AutoViewProvider.getMapping(AbstractView::class.java) }
    }

    test("calling autoDiscoverViews() multiple times won't fail") {
        autoDiscoverViews("com.github")
        autoDiscoverViews("com.github")
        expect("abstractclass") { AutoViewProvider.getMapping(AbstractView::class.java) }
    }
})

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
