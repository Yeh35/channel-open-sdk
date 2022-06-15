package io.github.yeh35.channelopenapi.dto

import io.github.yeh35.channelopenapi.schma.Manager
import io.github.yeh35.channelopenapi.schma.Online

internal data class ManagersDto(
    val managers: List<Manager>,
    val onlines: List<Online> = listOf()
)

