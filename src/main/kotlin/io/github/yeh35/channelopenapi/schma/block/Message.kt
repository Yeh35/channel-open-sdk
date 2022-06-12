package io.github.yeh35.channelopenapi.schma.block

import io.github.yeh35.channelopenapi.schma.block.MessageOption.ACT_AS_MANAGER

data class Message(
    val blocks: List<Block>,
    val options: Set<MessageOption> = setOf(ACT_AS_MANAGER)
)