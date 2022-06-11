package com.yeh35.channelopenapi.dto

import com.yeh35.channelopenapi.schma.Manager
import com.yeh35.channelopenapi.schma.Online

internal data class ManagerSingleDto(
    val manager: Manager,
    val online: Online? = null
)