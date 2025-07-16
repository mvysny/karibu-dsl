@file:Suppress("DEPRECATION")

package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.upload.Receiver
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.server.streams.UploadHandler
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import kotlin.test.expect

abstract class UploadTest {
    @BeforeEach
    fun setup() { MockVaadin.setup() }
    @AfterEach
    fun teardown() { MockVaadin.tearDown() }

    @Test fun dsl() {
        UI.getCurrent().upload {  }
        _expectOne<Upload>()
        UI.getCurrent().removeAll()
        UI.getCurrent().upload(Receiver { _, _ -> ByteArrayOutputStream() })
        _expectOne<Upload>()
        UI.getCurrent().upload(UploadHandler.inMemory { a, b -> })
        UI.getCurrent().upload {
            uploadButton { span("Upload!") }
        }
    }

    @Test fun `uploadButton dsl`() {
        lateinit var span: Span
        UI.getCurrent().upload {
            uploadButton { span("Upload!") { span = this } }
        }
        _expectOne<Span> { text = "Upload!" }
        expect(span) { _get<Span>() }
    }

    @Test fun `i18n dsl`() {
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
