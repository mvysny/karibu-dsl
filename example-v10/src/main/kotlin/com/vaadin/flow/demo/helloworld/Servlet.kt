/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.demo.helloworld

import com.vaadin.server.VaadinServlet
import com.vaadin.server.VaadinServletConfiguration

import javax.servlet.annotation.WebServlet

/**
 * The main servlet for the application.
 *
 * It is not mandatory to have the Servlet, since Flow will automatically register a Servlet to any app with at least one `@Route` to server root context.
 */
@WebServlet(urlPatterns = arrayOf("/*"), name = "UIServlet", asyncSupported = true)
@VaadinServletConfiguration(usingNewRouting = true, productionMode = false)
class Servlet : VaadinServlet()
