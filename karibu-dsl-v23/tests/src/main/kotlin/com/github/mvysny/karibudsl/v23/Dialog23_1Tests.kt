package com.github.mvysny.karibudsl.v23

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.openDialog
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v10.pro._fireCancel
import com.github.mvysny.kaributesting.v10.pro._fireConfirm
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H3
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect
import kotlin.test.fail

abstract class Dialog23_1Tests {
    @BeforeEach
    fun setup() {
        MockVaadin.setup()
    }

    @AfterEach
    fun teardown() {
        MockVaadin.tearDown()
    }

    @Test
    fun `dsl syntax test`() {
        openDialog {
            header { h3("Header") }
            footer { button("Save") }
        }
        _expectOne<Dialog>()
    }

    @Test
    fun smoke() {
        openDialog {
            header { h3("Header") }
            footer { button("Save") }
        }
        _expectOne<Dialog>()
        _expectOne<H3> { text = "Header" }
        _expectOne<Button> { text = "Save" }
    }

    @Nested
    inner class confirmDialog {
        @Test
        fun smoke() {
            openConfirmDialog {
                setConfirmIsDanger()
                setCloseOnCancel("Cancel")
            }
            _expectOne<ConfirmDialog>()
        }

        @Test
        fun testConfirm() {
            var deleteCalled = false
            openConfirmDialog(
                "Delete beverage",
                "Are you sure you want to delete stuff?"
            ) {
                setConfirmButton("Delete") {
                    deleteCalled = true
                }
                setConfirmIsDanger()
                setCloseOnCancel("Cancel")
            }

            _expectOne<ConfirmDialog>()
            _get<ConfirmDialog>()._fireConfirm()
            expect(true) { deleteCalled }
            _expectNone<ConfirmDialog>()
        }

        @Test
        fun testCancel() {
            openConfirmDialog(
                "Delete beverage",
                "Are you sure you want to delete stuff?"
            ) {
                setConfirmButton("Delete") {
                    fail("shouldn't be called")
                }
                setConfirmIsDanger()
                setCloseOnCancel("Cancel")
            }

            _expectOne<ConfirmDialog>()
            _get<ConfirmDialog>()._fireCancel()
            _expectNone<ConfirmDialog>()
        }
    }
}
