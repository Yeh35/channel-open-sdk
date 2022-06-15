package io.github.yeh35.channelopenapi.schma

data class Webhook(
    val id: Long = 0,
    val channelId: Long = 0,
    val name: String,
    val url: String,
    val token: String = "",
    val createdAt: Long = 0,
    val scopes: Set<Scope>,
    val keywords: Set<String>,
    val watchGroups: Boolean,
    val apiVersion: ApiVersion,
    val watchUserChats: Boolean,
    val blocked: Boolean,
) {

    companion object {

        fun create(
            name: String,
            url: String,
            scopes: Set<Scope>,
            keywords: Set<String>,
            apiVersion: ApiVersion,
            blocked: Boolean
        ): Webhook {
            return Webhook(
                id = 0,
                channelId = 0,
                name = name,
                url = url,
                token = "",
                scopes = scopes,
                apiVersion = apiVersion,
                blocked = blocked,
                keywords = keywords,
                createdAt = 0,
                watchGroups = true,
                watchUserChats = true,
            )
        }

    }

}