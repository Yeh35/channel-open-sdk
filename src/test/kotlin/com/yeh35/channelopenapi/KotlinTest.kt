package com.yeh35.channelopenapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.yeh35.channelopenapi.schma.*
import com.yeh35.channelopenapi.schma.Scope.MESSAGE_CREATED_TEAM_CHAT
import com.yeh35.channelopenapi.schma.Scope.MESSAGE_CREATED_USER_CHAT
import com.yeh35.channelopenapi.schma.block.*
import org.junit.jupiter.api.Test
import java.awt.Color
import kotlin.streams.toList

class KotlinTest {

    @Test
    fun testColor() {
        println(Color.RED.run { String.format("#%02x%02x%02x", red, green, blue) })
    }

    @Test
    fun testBlock() {
        val blocks = listOf<Block>(
            TextBlock("안녕"),
            CodeBlock("println(hello)"),
            BulletBlock(
                "bullet", blocks = listOf(
                    TextBlock("bullet: 안녕1"),
                    TextBlock("bullet: 안녕2"),
                )
            )
        )

        val objectMapper = ObjectMapper()
        println(objectMapper.writeValueAsString(blocks))
    }

    @Test
    fun testMapper() {
        val json = "{\n" +
                "    \"id\": \"218869\",\n" +
                "    \"channelId\": \"92517\",\n" +
                "    \"accountId\": \"156100\",\n" +
                "    \"username\": \"jaws0910\",\n" +
                "    \"name\": \"예상오\",\n" +
                "    \"showDescriptionToFront\": false,\n" +
                "    \"email\": \"jaws0910@gmail.com\",\n" +
                "    \"showEmailToFront\": false,\n" +
                "    \"mobileNumber\": \"+821077646003\",\n" +
                "    \"showMobileNumberToFront\": false,\n" +
                "    \"role\": \"owner\",\n" +
                "    \"removed\": false,\n" +
                "    \"createdAt\": 1653917436966,\n" +
                "    \"displayAsChannel\": false,\n" +
                "    \"defaultGroupWatch\": \"all\",\n" +
                "    \"defaultDirectChatWatch\": \"all\",\n" +
                "    \"defaultUserChatWatch\": \"all\",\n" +
                "    \"chatAlertSound\": \"woody\",\n" +
                "    \"operatorScore\": 0.0,\n" +
                "    \"touchScore\": 17.189999,\n" +
                "    \"operatorEmailReminder\": true,\n" +
                "    \"operator\": true,\n" +
                "    \"operatorScheduling\": false,\n" +
                "    \"avatarUrl\": \"https://cf.channel.io/avatar/emoji/koala.f392e0.png\",\n" +
                "    \"avatarUrl\": \"https://cf.channel.io/avatar/emoji/koala.f392e0.png\",\n" +
                "    \"managerId\": \"218869\"\n" +
                "}"

        val objectMapper = ObjectMapper().registerModule(
            KotlinModule.Builder()
                .configure(KotlinFeature.NullIsSameAsDefault, true)
                .configure(KotlinFeature.NullToEmptyCollection, true)
                .configure(KotlinFeature.NullToEmptyMap, true)
                .build()
        )
        println(objectMapper.readValue(json, Manager::class.java))
    }


    fun testTest() {
        val xAccessKey = ""
        val xAccessSecret = ""

        val channel = Channel.create(xAccessKey, xAccessSecret)
        val bots = channel.getBots()
        println("count : ${bots.count()}")

        println(bots[0])
        val updatedBot = channel.saveBot(bots[0])
        println("updatedBot : $updatedBot")

        val newBot = channel.saveBot(Bot.create("new Bot", Color.GREEN))
        println("newBot : $newBot")

        val managerAll = channel.getManagerAll()
        println("managerAll : $managerAll")

        val manager = managerAll[1]
        channel.findByManagerId(manager.id)

        val message = Message(
            blocks = listOf(
                TextBlock("안녕"),
                TextBlock("안 안녕")
            )
        )
        channel.sendAnnouncementAll(bot = newBot, message = message)
        channel.sendAnnouncement(manager = manager, bot = newBot, message = message)

        val teamChatAll = channel.getTeamChatAll()
        println("teamChatAll: $teamChatAll")

        val teamChat = teamChatAll.stream()
            .filter { it.active }
            .toList()[0]
        channel.sendTeamChat(teamChat, newBot, message = message)


        val webhookAll = channel.getWebhookAll()
        println("webhookAll: $webhookAll")

        val webhook = Webhook.create(
            "TestCode",
            "http://localhost:8080",
            setOf(MESSAGE_CREATED_TEAM_CHAT, MESSAGE_CREATED_USER_CHAT),
            keywords = setOf("안녕", "왜 안될까?"),
            apiVersion = ApiVersion.V5,
            blocked = false
        )
        val saveResult = channel.createWebhook(webhook)
        println("saveResult: $saveResult")

        channel.deleteWebhook(webhook).let {
            println("delete WebHook: $it")
        }

        val deleteResult = channel.deleteBot(newBot)
        println("deleteResult : $deleteResult")
    }
}