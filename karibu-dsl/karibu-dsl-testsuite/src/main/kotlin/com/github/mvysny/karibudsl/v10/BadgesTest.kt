package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v10._text
import com.github.mvysny.kaributools.Badge
import com.github.mvysny.kaributools.BadgeVariant
import com.github.mvysny.kaributools.label
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.details.Details
import com.vaadin.flow.component.html.Span
import kotlin.streams.toList
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.badgesTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().badge("Hello") {
            addThemeVariants(BadgeVariant.PILL, BadgeVariant.SMALL)
        }
        _expectOne<Badge>()
    }
}
