package io.github.yeh35.channelopenapi.schma.block

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.security.InvalidParameterException

data class Button(
    val title: String,
    val colorVariant: Color,
    val url: String
) {

    enum class Color(@JsonValue val json: String) {
        RED("red"),
        GREEN("green"),
        PINK("pink"),
        PURPLE("purple"),
        ORANGE("orange"),
        BLACK("black"),
        COBALT("cobalt");

        @JsonCreator
        fun jsonConverter(json: String): Color {
            return when (json) {
                RED.json -> RED
                GREEN.json -> GREEN
                PINK.json -> PINK
                PURPLE.json -> PURPLE
                ORANGE.json -> ORANGE
                BLACK.json -> BLACK
                COBALT.json -> COBALT
                else -> {
                    throw InvalidParameterException("$json is invalid")
                }
            }
        }
    }

}