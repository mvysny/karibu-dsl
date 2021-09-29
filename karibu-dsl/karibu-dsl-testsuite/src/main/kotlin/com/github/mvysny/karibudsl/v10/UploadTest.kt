package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.upload.Receiver
import com.vaadin.flow.component.upload.Upload
import java.io.ByteArrayOutputStream
import kotlin.test.expect

fun DynaNodeGroup.uploadTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("dsl") {
        UI.getCurrent().upload {  }
        _expectOne<Upload>()
        UI.getCurrent().removeAll()
        UI.getCurrent().upload(Receiver { _, _ -> ByteArrayOutputStream() })
        _expectOne<Upload>()
    }

    test("i18n dsl") {
        val upload = UI.getCurrent().upload {
            i18n {
                dropFiles {
                    many = "Foo"
                }
                addFiles {
                    one = "Bar"
                }
                error {
                    fileIsTooBig = "Baz"
                }
                uploading {
                    status {
                        connecting = "Foo connecting"
                    }
                    remainingTime {
                        prefix = "Bar prefix"
                    }
                    error {
                        forbidden = "Baz forbidden"
                    }
                }
            }
        }
        expect("Foo") { upload.i18n.dropFiles.many }
        expect("Bar") { upload.i18n.addFiles.one }
        expect("Baz") { upload.i18n.error.fileIsTooBig }
        expect("Foo connecting") { upload.i18n.uploading.status.connecting }
        expect("Bar prefix") { upload.i18n.uploading.remainingTime.prefix }
        expect("Baz forbidden") { upload.i18n.uploading.error.forbidden }
    }
}
