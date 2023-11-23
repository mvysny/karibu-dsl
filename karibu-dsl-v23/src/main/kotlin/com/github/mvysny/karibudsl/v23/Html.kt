package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.html.RangeInput

@VaadinDsl
public fun (@VaadinDsl HasComponents).rangeInput(block: (@VaadinDsl RangeInput).() -> Unit = {}): RangeInput
        = init(RangeInput(), block)
