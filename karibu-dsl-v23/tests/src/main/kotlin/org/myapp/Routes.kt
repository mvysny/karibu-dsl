package org.myapp

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route("foo")
@PageTitle("Main")
class MainRoute : Div()

@Route("demo")
class DemoRoute : Div()
