package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.upload.Receiver
import com.vaadin.flow.component.upload.Upload
import java.io.ByteArrayOutputStream

class UploadTest : DynaTest({
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("dsl") {
        UI.getCurrent().upload {  }
        _expectOne<Upload>()
        UI.getCurrent().removeAll()
        UI.getCurrent().upload(Receiver { _, _ -> ByteArrayOutputStream() })
        _expectOne<Upload>()
    }
})
