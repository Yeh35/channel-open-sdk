package com.yeh35.channelopenapi.dto

import com.yeh35.channelopenapi.schma.TeamChat

internal data class GroupsDto(
    var groups: List<TeamChat> = listOf()
)