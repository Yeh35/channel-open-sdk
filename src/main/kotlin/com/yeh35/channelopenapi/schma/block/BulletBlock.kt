package com.yeh35.channelopenapi.schma.block

class BulletBlock(
    value: String,
    language: String = "ko",
    val blocks: List<TextBlock> = listOf(),
) : Block(
    type = BlockType.BULLETS,
    language = language,
    value = value,
)