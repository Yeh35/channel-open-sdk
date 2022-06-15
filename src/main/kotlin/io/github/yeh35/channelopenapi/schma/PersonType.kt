package io.github.yeh35.channelopenapi.schma

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.security.InvalidParameterException

enum class PersonType(@JsonValue val json: String) {
    MANAGER("manager"),
    USER("user");

    companion object {

        @JsonCreator
        fun jsonConverter(json: String): PersonType {
            return when(json) {
                MANAGER.json -> MANAGER
                USER.json -> USER
                else -> {
                    throw InvalidParameterException("$json is invalid")
                }
            }
        }
    }
}
