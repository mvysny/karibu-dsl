package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.upload.Receiver
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.server.AbstractStreamResource

@VaadinDsl
public fun (@VaadinDsl HasComponents).upload(receiver: Receiver? = null, block: (@VaadinDsl Upload).() -> Unit = {}): Upload
        = init(Upload(receiver), block)
