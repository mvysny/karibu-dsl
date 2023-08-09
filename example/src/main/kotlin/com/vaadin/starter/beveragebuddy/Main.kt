package com.vaadin.starter.beveragebuddy

import com.github.mvysny.vaadinboot.VaadinBoot
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.theme.Theme

@BodySize(width = "100vw", height = "100vh")
@Theme("my-theme")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
class AppShell: AppShellConfigurator

/**
 * Run this function to launch your app in Embedded Jetty.
 */
fun main(vararg args: String) {
    VaadinBoot().run()
}
