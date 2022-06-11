package com.yeh35.channelopenapi.schma

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.security.InvalidParameterException

enum class ApiVersion(@JsonValue val json: String) {
    V3("v3"),
    V4("v4"),
    V5("v5");

    companion object {

        @JsonCreator
        fun jsonConverter(json: String): ApiVersion {
            return when(json) {
                V3.json -> V3
                V4.json -> V4
                V5.json -> V5
                else -> {
                    throw InvalidParameterException("$json is invalid")
                }
            }
        }
    }
}
