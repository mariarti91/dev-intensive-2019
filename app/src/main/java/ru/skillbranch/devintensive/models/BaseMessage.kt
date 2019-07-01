package ru.skillbranch.devintensive.models

import java.util.*

enum class MessageTypes{
    TEXT,
    IMAGE
}

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {

    abstract fun formatMessage():String

    companion object AbstractFactory{
        var lastId = -1
        fun makeMessage(from:User?, chat:Chat, date:Date = Date(), type:MessageTypes = MessageTypes.TEXT, payload:Any?):BaseMessage{
            lastId++
            return when(type)
            {
                MessageTypes.TEXT -> TextMessage("$lastId", from, chat, date=date, text = payload as String)
                MessageTypes.IMAGE -> ImageMessage("$lastId", from, chat, date=date, image = payload as String)
            }
        }
    }
}