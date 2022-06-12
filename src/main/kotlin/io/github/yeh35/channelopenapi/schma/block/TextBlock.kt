package io.github.yeh35.channelopenapi.schma.block

class TextBlock(
    value: String,
    language: String = "ko",
) : Block(
    type = BlockType.TEXT,
    language = language,
    value = value,
)