package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.messages.MessageInput
import com.vaadin.flow.component.messages.MessageList
import com.vaadin.flow.component.messages.MessageListItem

@VaadinDsl
public fun (@VaadinDsl HasComponents).messageInput(block: (@VaadinDsl MessageInput).()->Unit = {}): MessageInput =
    init(MessageInput(), block)

@VaadinDsl
public fun (@VaadinDsl HasComponents).messageList(
    messages: List<MessageListItem> = listOf(),
    block: (@VaadinDsl MessageList).()->Unit = {}
): MessageList = init(MessageList(messages), block)
