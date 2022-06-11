package com.yeh35.channelopenapi.schma

data class Online(
    val id: String,
    val channelId: Long,
    val personId: Long,
    val personType: PersonType
)