package com.example.v8.uitest.example

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.mvysny.kaributesting.v8.*
import com.vaadin.ui.Button
import com.vaadin.ui.GridLayout
import com.vaadin.ui.Panel

class CommonElementsViewTest : DynaTest({
    usingApp()

    test("smoke") {
        navigateToView<CommonElementsView>()
        _expect<Panel> { caption = "Loading Indicator" }
    }

    test("notification can be shown multiple times") {
        navigateToView<CommonElementsView>()
        val button = _get<GridLayout> { caption = "Show in position" } ._find<Button>().first()
        button._click()
        expectNotifications("Notification Title" to "A more informative message about what has happened. Nihil hic munitissimus habendi senatus locus, nihil horum? Inmensae subtilitatis, obscuris et malesuada fames. Hi omnes lingua, institutis, legibus inter se differunt.")
        button._click()
        expectNotifications("Notification Title" to "A more informative message about what has happened. Nihil hic munitissimus habendi senatus locus, nihil horum? Inmensae subtilitatis, obscuris et malesuada fames. Hi omnes lingua, institutis, legibus inter se differunt.")
    }
})