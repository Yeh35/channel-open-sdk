package io.github.yeh35.channelopenapi.schma

import java.awt.Color
import java.time.LocalDateTime

/**
 * Channel Open API에서는 name으로 bot을 체크한다.
 */
data class Bot(
    val id: Long,
    val name: String,
    var avatarUrl: String,
    var channelId: String,
    var color: Color,
    val createdAt: LocalDateTime,
) {

    companion object {

        fun create(name: String, Color: Color): Bot {
            return Bot(
                id = 0,
                name = name,
                avatarUrl = "",
                channelId = "",
                color = Color,
                createdAt = LocalDateTime.now()
            )
        }
    }

}