package io.github.yeh35.channelopenapi.dto

import io.github.yeh35.channelopenapi.schma.TeamChat

internal data class GroupsDto(
    var groups: List<TeamChat> = listOf()
)