package com.yeh35.channelopenapi.schma

data class TeamChat(
    val id: String,
    val channelId: String,
    val name: String,
    val scope: String,
    val managerIds: List<Long>,
    val icon: String = "",
    val active: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)