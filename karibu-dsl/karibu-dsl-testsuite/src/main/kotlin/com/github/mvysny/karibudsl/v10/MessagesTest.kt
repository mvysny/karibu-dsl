package com.github.mvysny.karibudsl.v10

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.messages.MessageInput
import com.vaadin.flow.component.messages.MessageList
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class MessagesTest {
    @BeforeEach
    fun setup() { MockVaadin.setup() }
    @AfterEach
    fun teardown() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().messageInput()
        UI.getCurrent().messageList()
        _expectOne<MessageInput>()
        _expectOne<MessageList>()
    }
}
