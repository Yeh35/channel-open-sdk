package com.yeh35.channelopenapi.dto

import com.yeh35.channelopenapi.schma.Manager
import com.yeh35.channelopenapi.schma.Online

internal data class ManagersDto(
    val managers: List<Manager>,
    val onlines: List<Online> = listOf()
)

