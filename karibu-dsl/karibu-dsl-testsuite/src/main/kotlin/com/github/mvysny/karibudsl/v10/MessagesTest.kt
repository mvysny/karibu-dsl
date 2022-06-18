package com.github.mvysny.karibudsl.v10

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.messages.MessageInput
import com.vaadin.flow.component.messages.MessageList

@DynaTestDsl
fun DynaNodeGroup.messagesTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().messageInput()
        UI.getCurrent().messageList()
        _expectOne<MessageInput>()
        _expectOne<MessageList>()
    }
}
