package com.yeh35.channelopenapi.dto

import com.yeh35.channelopenapi.schma.Webhook

data class WebhooksDto(
    val webhooks: List<Webhook> = listOf()
)
