package io.github.yeh35.channelopenapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.yeh35.channelopenapi.schma.*
import io.github.yeh35.channelopenapi.schma.Scope.MESSAGE_CREATED_TEAM_CHAT
import io.github.yeh35.channelopenapi.schma.Scope.MESSAGE_CREATED_USER_CHAT
import io.github.yeh35.channelopenapi.schma.block.*
import org.junit.jupiter.api.Test
import java.awt.Color
import java.time.LocalDate
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

//    @Test
    fun testTest() {
        val xAccessKey = ""
        val xAccessSecret = ""
        val channel = Channel.create(xAccessKey, xAccessSecret)

        // 등록된 봇 전부 가져오기
        val sinceToBack = LocalDate.now()
        val bots = channel.getBots(sinceToBack)
        println("count : ${bots.count()}")

        // 봇 수정하기
        val bot = bots[0]
        bot.color = Color.RED
        val updatedBot = channel.saveBot(bot)
        println("updatedBot : $updatedBot")

        // 봇 생성하기
        val newBot = Bot.create("new Bot", Color.GREEN)
        channel.saveBot(newBot)
        println("newBot : $newBot")

        // 봇 삭제하기
        val isSuccess = channel.deleteBot(newBot)
        println("deleteResult : $isSuccess")

        // 매니저 전부 가져오기
        val managerAll = channel.getManagerAll(sinceToBack)
        println("managerAll : $managerAll")

        // ID로 특정 매니저 찾기
        val manager = managerAll[1]
        channel.findByManagerId(manager.id)

        val message = Message(
            blocks = listOf(
                TextBlock("Hi Open API"),
                CodeBlock("""println("Hi Open API")"""),
                BulletBlock(
                    "List",
                    blocks = listOf(
                        TextBlock("item 1"),
                        TextBlock("item 2"),
                        TextBlock("item 3")
                    )
                )
            ),
            options = setOf(MessageOption.ACT_AS_MANAGER)
        )

        // 모든 매니저에게 보내기
        channel.sendAnnouncementAll(bot = bot, message = message)

        // 특정 매니저에게 보내기
        channel.sendAnnouncement(manager = manager, bot = bot, message = message)

        // 모든 TeamChat 가져오기
        val teamChatAll = channel.getTeamChatAll()
        println("teamChatAll size: ${teamChatAll.size}")

        // 활성화된 TeamChat 필터링하기
        val teamChat = teamChatAll.stream()
            .filter { it.active }
            .toList()[0]

        //특정 TeamChat으로 메시지 보내기
        channel.sendTeamChat(teamChat = teamChat, bot = bot, message = message)

        // 등록된 Webhook 가져오기
        val webhookAll = channel.getWebhookAll()
        println("webhookAll Size: ${webhookAll.size}")

        // Webhook 생성하기
        val webhook = Webhook.create(
            name = "TestCode",
            url = "http://localhost:8080",
            scopes = setOf(MESSAGE_CREATED_TEAM_CHAT, MESSAGE_CREATED_USER_CHAT),
            keywords = setOf("안녕", "왜 안될까?"),
            apiVersion = ApiVersion.V5,
            blocked = false
        )
        val isSave = channel.createWebhook(webhook)
        println("saveResult: $isSave")

        // Webhook 삭제하기
        val isDelete = channel.deleteWebhook(webhook)
        println("delete WebHook: $isDelete")


        val deleteResult = channel.deleteBot(newBot)
        println("deleteResult : $deleteResult")
    }
}