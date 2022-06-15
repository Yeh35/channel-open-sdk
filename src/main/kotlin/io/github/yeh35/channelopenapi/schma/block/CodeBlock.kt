package io.github.yeh35.channelopenapi.schma.block

class CodeBlock(
    value: String,
    language: String = "ko",
) : Block(
    type = BlockType.CODE,
    language = language,
    value = value,
)