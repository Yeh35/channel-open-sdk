package io.github.yeh35.channelopenapi.schma.block

import com.fasterxml.jackson.annotation.JsonValue

enum class BlockType(@JsonValue val jsonString: String) {
    BULLETS("bullets"),
    CODE("code"),
    TEXT("text")
}