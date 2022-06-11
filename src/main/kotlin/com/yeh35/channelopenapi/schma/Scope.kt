package com.yeh35.channelopenapi.schma

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.security.InvalidParameterException

enum class Scope(@JsonValue val json: String) {

    USER_CHAT_OPENED("userChatOpened"),
    MESSAGE_CREATED_USER_CHAT("messageCreatedUserChat"),
    MESSAGE_CREATED_TEAM_CHAT("messageCreatedTeamChat"),
    USER_UPDATED_UNSUBSCRIBED("userUpdatedUnsubscribed"),
    USER_UPDATED_CONTAC("userUpdatedContac");

    companion object {

        @JsonCreator
        fun jsonConverter(json: String): Scope {
            return when(json) {
                USER_CHAT_OPENED.json -> USER_CHAT_OPENED
                MESSAGE_CREATED_USER_CHAT.json -> MESSAGE_CREATED_USER_CHAT
                MESSAGE_CREATED_TEAM_CHAT.json -> MESSAGE_CREATED_TEAM_CHAT
                USER_UPDATED_UNSUBSCRIBED.json -> USER_UPDATED_UNSUBSCRIBED
                USER_UPDATED_CONTAC.json -> USER_UPDATED_CONTAC
                else -> {
                    throw InvalidParameterException("$json is invalid")
                }
            }
        }
    }
}
