package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.markdown.Markdown
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class AbstractMasterDetailLayoutTests {
    init {
        Assumptions.assumeTrue(VaadinVersion.get.isAtLeast(24, 9))
    }
    @BeforeEach fun setup() { MockVaadin.setup() }
    @AfterEach fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        val l = VerticalLayout()
        l.masterDetailLayout()
        l._expectOne<MasterDetailLayout>()
    }

    @Test fun populateMaster() {
        val l = VerticalLayout()
        l.masterDetailLayout {
            master {
                button("Foo")
            }
        }
        l._expectOne<Button> { text = "Foo" }
    }
    @Test fun populateDetail() {
        val l = VerticalLayout()
        l.masterDetailLayout {
            detail {
                button("Foo")
            }
        }
        l._expectOne<Button> { text = "Foo" }
    }
}