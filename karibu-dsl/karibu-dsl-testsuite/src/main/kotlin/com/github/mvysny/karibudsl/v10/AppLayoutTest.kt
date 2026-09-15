package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

open class AppLayoutTest {
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        val a = UI.getCurrent().appLayout {
            navbar {
                drawerToggle()
                h3("My App")
            }
            drawer {
                routerLink(VaadinIcon.ARCHIVE, "Foo")
                routerLink(VaadinIcon.ACADEMY_CAP, "Bar")
            }
            content {
                span("Hello world!")
            }
        }
        a.cloneBySerialization()
    }

    @Test fun `navbar children land in the navbar slot`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            navbar {
                drawerToggle()
                h3("My App")
            }
        }
        val slots = a.children.toList().map { it.element.getAttribute("slot") }
        expect(listOf("navbar", "navbar")) { slots }
        expect(2) { a.children.filter { it is DrawerToggle || it is H3 }.count() }
    }

    @Test fun `drawer children land in the drawer slot`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            drawer {
                span("Item 1")
                span("Item 2")
            }
        }
        val drawerChildren = a.children.toList().filter { it.element.getAttribute("slot") == "drawer" }
        expect(2) { drawerChildren.size }
    }

    @Test fun `content block sets the content`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            content {
                span("Hello world!")
            }
        }
        val content = a.content
        check(content is Span)
        expect("Hello world!") { content.text }
    }

    @Test fun `removeNavbar clears the navbar slot`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            navbar {
                drawerToggle()
                h3("My App")
            }
        }
        expect(2) { a.children.count() }
        a.removeNavbar(false)
        expect(0) { a.children.count() }
    }

    @Test fun `removeNavbar only clears the slot it was asked for`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            navbar { span("plain") }
            navbar(touchOptimized = true) { span("touch") }
        }
        expect(2) { a.children.count() }
        a.removeNavbar(true)
        expect(listOf("plain")) { a.children.toList().map { (it as Span).text } }
        a.removeNavbar(false)
        expect(0) { a.children.count() }
    }

    @Test fun `removeDrawer clears the drawer slot`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            drawer {
                span("Item 1")
                span("Item 2")
            }
        }
        expect(2) { a.children.count() }
        a.removeDrawer()
        expect(0) { a.children.count() }
    }

    @Test fun `removeContent clears the content`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            content { span("Hello world!") }
        }
        expect(true) { a.content != null }
        a.removeContent()
        expect(null) { a.content }
    }

    @Test fun `content can be re-set after removeContent`() {
        val a: AppLayout = UI.getCurrent().appLayout {
            content { span("first") }
        }
        a.removeContent()
        a.content { span("second") }
        val content = a.content
        check(content is Span)
        expect("second") { content.text }
    }

    @Test fun `content block accepts at most one child`() {
        val ex = runCatching {
            UI.getCurrent().appLayout {
                content {
                    span("a")
                    span("b")
                }
            }
        }.exceptionOrNull()
        check(ex is IllegalStateException) { "expected IllegalStateException, got $ex" }
    }
}
