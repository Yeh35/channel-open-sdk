package com.yeh35.channelopenapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.yeh35.channelopenapi.dto.*
import com.yeh35.channelopenapi.dto.BotDto
import com.yeh35.channelopenapi.dto.BotsDto
import com.yeh35.channelopenapi.dto.ManagersDto
import com.yeh35.channelopenapi.dto.SingleBotDto
import com.yeh35.channelopenapi.schma.Bot
import com.yeh35.channelopenapi.schma.Manager
import com.yeh35.channelopenapi.schma.TeamChat
import com.yeh35.channelopenapi.schma.Webhook
import com.yeh35.channelopenapi.schma.block.Message
import com.yeh35.channelopenapi.util.DateTimeUtil
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.nio.charset.StandardCharsets
import java.time.*
import java.util.concurrent.TimeUnit

/**
 * 체널톡 Open SDK의 시작 지점
 * Channel은 하나의 Channel과 1 대 1 매칭된다.
 * @author yeh sang oh
 */
class Channel private constructor(
    private val xAccessKey: String,
    private val xAccessSecret: String
) {

    private val webClient: WebClient

    init {
        val httpClient: HttpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000)).doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }

        val strategies = ExchangeStrategies.builder().codecs { clientDefaultCodecsConfigurer ->
            clientDefaultCodecsConfigurer.defaultCodecs()
                .jackson2JsonEncoder(
                    Jackson2JsonEncoder(
                        ObjectMapper().registerModule(
                            KotlinModule.Builder()
                                .configure(KotlinFeature.NullIsSameAsDefault, true)
                                .configure(KotlinFeature.NullToEmptyCollection, true)
                                .configure(KotlinFeature.NullToEmptyMap, true)
                                .build()
                        ),
                        MediaType.APPLICATION_JSON
                    )
                )
            clientDefaultCodecsConfigurer.defaultCodecs()
                .jackson2JsonDecoder(
                    Jackson2JsonDecoder(
                        ObjectMapper().registerModule(
                            KotlinModule.Builder()
                                .configure(KotlinFeature.NullIsSameAsDefault, true)
                                .configure(KotlinFeature.NullToEmptyCollection, true)
                                .configure(KotlinFeature.NullToEmptyMap, true)
                                .build()
                        ),
                        MediaType.APPLICATION_JSON
                    )
                )
        }.build()

        webClient = WebClient.builder().baseUrl("https://api.channel.io")
            .clientConnector(ReactorClientHttpConnector(httpClient)).exchangeStrategies(strategies).build()
    }

    /**
     * 봇 정보 얻기
     *
     * @param sinceToBack 이전에 등록한 봇만 필터링
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun getBots(sinceToBack: LocalDate = LocalDate.now()): List<Bot> {
        val epoch = DateTimeUtil.localDateToUnixMilli(sinceToBack)

        val retrieve = webClient.get().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/bots")
                    .queryParam("since", epoch)
                    .queryParam("limit", "500")
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
        }.retrieve()

        val bodyToMono = retrieve.bodyToMono<BotsDto>()
        return bodyToMono.block()!!.bots!!.map { it.toBot() }.toList()
    }

    /**
     * 봇 신규 저장 및 업데이트
     * 봇 식별은 name으로 한다.
     *
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun saveBot(bot: Bot): Bot {
        val body = webClient.post().run {
            this.uri { builder -> builder.replacePath("/open/v5/bots").build() }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
            this.bodyValue(BotDto.Bot4SaveDto.of(bot))
        }.exchangeToMono { response ->
            if (response.statusCode().is2xxSuccessful) {
                response.bodyToMono<SingleBotDto>()
            } else {
                response.bodyToMono<String>()
            }
        }.block()

        if (body is String) {
            throw RuntimeException(body)
        }

        body as SingleBotDto
        return body.bot!!.toBot()
    }

    /**
     * 봇 삭제
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun deleteBot(bot: Bot): Boolean {
        if (bot.id == 0L) {
            return true
        }

        val block = webClient.delete().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/bots/${bot.id}")
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
        }.exchangeToMono { response ->
            Mono.just(response.statusCode().is2xxSuccessful)
        }.block()

        return block ?: false
    }

    /**
     * 매니저 전부 가져오기
     *
     * @param sinceToBack 이전에 등록한 메니저 필터링
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun getManagerAll(sinceToBack: LocalDate = LocalDate.now()): List<Manager> {
        val epoch = DateTimeUtil.localDateToUnixMilli(sinceToBack)

        val managersDto = webClient.get().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/managers")
                    .queryParam("since", epoch)
                    .queryParam("limit", "500")
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
        }.retrieve()
            .bodyToMono<ManagersDto>()
            .block()

        return managersDto!!.managers
    }

    /**
     * 특정 사람의 "나와의 대화방"에 메시지 전송
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun findByManagerId(managerId: Long): Manager {

        val resultDto = webClient.get().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/managers/${managerId}")
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
        }.retrieve()
            .bodyToMono<ManagerSingleDto>()
            .block()

        return resultDto!!.manager
    }

    /**
     * 특정 매니저의 "나와의 대화방"에 메시지 전송
     *
     * @return 전송된 매니저 수
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun sendAnnouncement(manager: Manager, bot: Bot, message: Message): Long {
        val body = webClient.post().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/announcements/announce")
                    .queryParam("botName", bot.name)
                    .queryParam("managerIds", manager.id)
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
            this.bodyValue(message)
        }.exchangeToMono { response ->
            if (response.statusCode().is2xxSuccessful) {
                response.bodyToMono<ResultDto>()
            } else {
                response.bodyToMono<String>()
            }
        }.block()

        if (body is String) {
            throw RuntimeException(body)
        }

        body as ResultDto
        return body.result
    }

    /**
     * 모든 매니저의 "나와의 대화방"에 메시지 전송
     *
     * @return 전송된 매니저 수
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun sendAnnouncementAll(bot: Bot, message: Message): Long {
        val body = webClient.post().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/announcements/announce/all")
                    .queryParam("botName", bot.name)
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
            this.bodyValue(message)
        }.exchangeToMono { response ->
            if (response.statusCode().is2xxSuccessful) {
                response.bodyToMono<ResultDto>()
            } else {
                response.bodyToMono<String>()
            }
        }.block()

        if (body is String) {
            throw RuntimeException(body)
        }

        body as ResultDto
        return body.result
    }

    /**
     * 모든 TeamChat 얻기
     *
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun getTeamChatAll(sinceToBack: LocalDate = LocalDate.now()): List<TeamChat> {
        val epoch = DateTimeUtil.localDateToUnixMilli(sinceToBack)

        val resultDto = webClient.get().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/groups")
                    .queryParam("since", epoch)
                    .queryParam("limit", "500")
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
        }.retrieve()
            .bodyToMono<GroupsDto>()
            .block()

        return resultDto!!.groups
    }

    /**
     * TeamChat에 메시지 보내기
     *
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun sendTeamChat(teamChat: TeamChat, bot: Bot, message: Message): Boolean {
        val result = webClient.post().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/groups/${teamChat.id}/messages")
                    .queryParam("botName", bot.name)
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
            this.bodyValue(message)
        }.exchangeToMono { response ->
            Mono.just(response.statusCode().is2xxSuccessful)
        }.block()

        return result!!
    }

    /**
     * 모든 Webhook 얻기
     *
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun getWebhookAll(sinceToBack: LocalDate = LocalDate.now()): List<Webhook> {
        val epoch = DateTimeUtil.localDateToUnixMilli(sinceToBack)

        val resultDto = webClient.get().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/webhooks")
                    .queryParam("since", epoch)
                    .queryParam("limit", "500")
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
        }.retrieve()
            .bodyToMono<WebhooksDto>()
            .block()

        return resultDto!!.webhooks
    }

    /**
     * Webhook 추가
     *
     * @author sang oh yeh
     * @since 0.0.1
     */
    fun createWebhook(webhook: Webhook): Boolean {
        val result = webClient.post().run {
            this.uri { builder -> builder.replacePath("/open/v5/webhooks").build() }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
            this.bodyValue(webhook)
        }.exchangeToMono { response ->
            Mono.just(response.statusCode().is2xxSuccessful)
        }.block()

        return result!!
    }

    /**
    * Webhook 삭제
    *
    * @author sang oh yeh
    * @since 0.0.1
    */
    fun deleteWebhook(webhook: Webhook): Any {
        if (webhook.id == 0L) {
            return true
        }

        val block = webClient.delete().run {
            this.uri { builder ->
                builder.replacePath("/open/v5/webhooks/${webhook.id}")
                    .build()
            }
            this.accept(MediaType.APPLICATION_JSON)
            this.acceptCharset(StandardCharsets.UTF_8)
            this.header("x-access-key", xAccessKey)
            this.header("x-access-secret", xAccessSecret)
        }.exchangeToMono { response ->
            Mono.just(response.statusCode().is2xxSuccessful)
        }.block()

        return block ?: false
    }

    companion object {

        fun create(xAccessKey: String, xAccessSecret: String): Channel {
            return Channel(
                xAccessKey = xAccessKey, xAccessSecret = xAccessSecret
            )
        }
    }
}