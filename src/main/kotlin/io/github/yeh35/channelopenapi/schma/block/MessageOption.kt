package io.github.yeh35.channelopenapi.schma.block

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.security.InvalidParameterException

enum class MessageOption(@JsonValue val json: String) {

    ACT_AS_MANAGER("actAsManager"),
    DO_NOT_POST("doNotPost"),
    DO_NOT_SEARCH("doNotSearch"),
    DO_NOT_SEND_APP("doNotSendApp"),
    IMMUTABLE("immutable"),
    PRIVATE("private"),
    SILENT("silent");

    companion object {

        @JsonCreator
        fun jsonConverter(json: String): MessageOption {
            return when (json) {
                ACT_AS_MANAGER.json -> ACT_AS_MANAGER
                DO_NOT_POST.json -> DO_NOT_POST
                DO_NOT_SEARCH.json -> DO_NOT_SEARCH
                DO_NOT_SEND_APP.json -> DO_NOT_SEND_APP
                IMMUTABLE.json -> IMMUTABLE
                PRIVATE.json -> PRIVATE
                SILENT.json -> SILENT
                else -> {
                    throw InvalidParameterException("$json is invalid")
                }
            }
        }
    }
}