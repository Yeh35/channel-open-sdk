package io.github.yeh35.channelopenapi.dto

import io.github.yeh35.channelopenapi.schma.Bot
import io.github.yeh35.channelopenapi.util.ColorUtil.Companion.converterRadix16
import io.github.yeh35.channelopenapi.util.DateTimeUtil
import java.awt.Color

internal data class BotDto(
    var id: String? = null,
    var name: String? = null,
    var avatarUrl: String? = null,
    var channelId: String? = null,
    var color: String? = null,
    var createdAt: Long? = null,
) {

    fun toBot(): Bot {
        return Bot(
            id = id!!.toLong(),
            name = name!!,
            avatarUrl = avatarUrl!!,
            channelId = channelId!!,
            color = Color.decode(color!!),
            createdAt = DateTimeUtil.unixMilliToLocalDateTime(createdAt!!)
        )
    }

    companion object {
        fun of(bot: Bot): BotDto {

            return BotDto(
                id = bot.id.toString(),
                name = bot.name,
                avatarUrl = bot.avatarUrl,
                channelId = bot.channelId,
                color = converterRadix16(bot.color),
                createdAt = DateTimeUtil.localDateTimeToUnixMilli(bot.createdAt)
            )
        }
    }

    data class Bot4SaveDto(
        var id: String? = null,
        var name: String? = null,
        var channelId: String? = null,
        var color: String? = null,
    ) {

        companion object {
            fun of(bot: Bot): Bot4SaveDto {

                return Bot4SaveDto(
                    id = bot.id.toString(),
                    name = bot.name,
                    channelId = bot.channelId,
                    color = converterRadix16(bot.color),
                )
            }
        }
    }
}