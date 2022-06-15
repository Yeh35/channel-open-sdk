package io.github.yeh35.channelopenapi.dto

import io.github.yeh35.channelopenapi.schma.Webhook

data class WebhooksDto(
    val webhooks: List<Webhook> = listOf()
)
