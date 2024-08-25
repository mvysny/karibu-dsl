package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

@Tag("my-test-element")
class MyTest : Component() {
    var userName: String by ElementStringProperty()
    var isDarkTheme: Boolean by ElementBooleanProperty(false)
    var myAttr: String? by ElementAttributeProperty()
}

@VaadinDsl
fun (@VaadinDsl HasComponents).myTest(block: (@VaadinDsl MyTest).() -> Unit = {}) = init(MyTest(), block)

abstract class ElementTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().apply {
            myTest {
                userName = "Martin"
                isDarkTheme = true
                myAttr = "Hello"
            }
        }
    }

    @Test fun serializable() {
        MyTest().cloneBySerialization()
    }

    @Test fun stringProperty() {
        val t = MyTest()
        expect("") { t.userName }
        t.userName = "Martin"
        expect("Martin") { t.userName }
        expect("Martin") { t.element.getProperty("userName") }
        t.userName = ""
        expect("") { t.userName }
        expect(null) { t.element.getProperty("userName") }
    }

    @Test fun booleanProperty() {
        val t = MyTest()
        expect(false) { t.isDarkTheme }
        t.isDarkTheme = true
        expect(true) { t.isDarkTheme }
        expect(true) { t.element.getProperty("isDarkTheme", false) }
        t.isDarkTheme = false
        expect(false) { t.isDarkTheme }
        expect(false) { t.element.getProperty("isDarkTheme", false) }
    }

    @Test fun attribute() {
        val t = MyTest()
        expect(null) { t.myAttr }
        t.myAttr = "Martin"
        expect("Martin") { t.myAttr!! }
        expect("Martin") { t.element.getAttribute("my-attr") }
        t.myAttr = ""
        expect("") { t.myAttr!! }
        expect("") { t.element.getAttribute("my-attr") }
        t.myAttr = null
        expect(null) { t.myAttr }
        expect(null) { t.element.getAttribute("my-attr") }
    }
}
