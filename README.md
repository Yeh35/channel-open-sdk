# README.md

# [Channel Talk Open API SDK](https://github.com/Yeh35/channel-open-sdk)
---
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

이 프로젝트는 Channel Talk에서 제공하는 [OpenAPI](https://api-doc.channel.io/)를 Java / Kotlin에서 편하게 사용하기 위한 라이브러리입니다.
Channel Open Api version 5를 기준으로 제작되었습니다.

Channel Talk Open Api를 직접 사용하기에는 Open Api의 문서는 설명이 부족하며, HTTP Body에 중복된 파라미터가 존재합니다. 하지만 이 라이브러리를 사용하면 이런 내용들을 전부 추상화하여 보다 간편하고 깔끔한 코드로 개발할 수 있습니다.

This project is a library of Open API provided by Channel Talk for ease of use in Java/Kotlin.
Built on Channel Open Application 5.

To use Channel Talk Open Api directly, Open Api's documents lack explanation and duplicate parameters exist in the HTTP body. But with this library, you can abstract all of this and develop it into simpler, cleaner code.

# Dependencies

---

이 라이브러리를 사용하기 위해 프로젝트에 의존성을 추가해야합니다.
You must add dependencies to your project to use this library.

<aside>
📌  https://repo1.maven.org/maven2/io/github/yeh35/ 에서 확인 가능합니다.

</aside>

## Gradle

```groovy
dependencies {
	implementation("io.projectreactor.netty:reactor-netty:1.0.19")
	implementation("org.springframework:spring-webflux:5.3.20")
	implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
	implementation("io.github.yeh35:channel-open-api:0.13.1")
}
```

# Setting

---

<aside>
📌 새 인증키 얻는 방법이 여기와 다른 경우 https://developers.channel.io/docs/openapi-auth 를 확인해주세요
If the method of obtaining a new authentication key is different from here, please check the https://developers.channel.io/docs/openapi-auth

</aside>

1. `설정` > `API` > `새 인증 키 만들기` 
*`Settings` > `API Key management` > `Create new credential`*
    
    ![Untitled](README/Untitled.png)
    
    ![Untitled](README/Untitled%201.png)
    
2. 인증 키 확인
Check the Authentication Key
    
    ![Untitled](README/Untitled%202.png)
    
3. 코드 준비
Code Setting
    - Java
  
      ```java
        String xAccessKey = "";
        String xAccessSecret = "";
        Channel channel = Channel.Companion.create(xAccessKey, xAccessSecret);
      ```
        
    - Kotlin
        
        ```kotlin
        val xAccessKey = ""
        val xAccessSecret = ""
        val channel = Channel.create(xAccessKey, xAccessSecret)
        ```
        

이것으로 Open API를 사용할 준비가 끝났습니다. 매우 간단하죠? 
You are now ready to use the Open API. It's very simple, right?

# How to use

---

기본적으로 `Channel.create()`로 만든 `Channel` 인스턴스를 계속 사용합니다.
하나의 `Channel`인스턴스는 하나의 Channel Talk의 Channel과 1대1로 대응됩니다. 
Channel Talk의 여러 Channel을 관리해야하는 경우 인스턴스를 추가로 생성하면 됩니다.

By default, continue to use the Channel instance created with `Channel.create()`.
One `Channel` instance corresponds one-on-one to a Channel in one Channel Talk.
If you need to manage multiple channels in Channel Talk, you can create additional instances.

제공되는 예제 코드는 Kotiln입니다. Kotlin을 모른다고 해도 이해하는데 어려움은 없을 것입니다. 

The example code provided is Kotiln. Even if you don't know Kotlin, you won't have a hard time understanding it.

## Bot

봇을 조회하고 생성하고 수정, 삭제합니다. 참고로 봇의 식별은 ID가 아닌 name으로 합니다.
View, create, update, and delete the bot. For your information, the identification of the bot should be name, not ID

```kotlin
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
```

## Manager

Manager의 식별은 ID로 합니다.
The identification of the Manager should be the ID.

```kotlin
// 매니저 전부 가져오기
val sinceToBack = LocalDate.now()
val managerAll = channel.getManagerAll(sinceToBack)
println("managerAll : $managerAll")

// ID로 특정 매니저 찾기
val manager = managerAll[1]
channel.findByManagerId(manager.id)
```

## TeamChat

```kotlin
// 모든 TeamChat 가져오기
val teamChatAll = channel.getTeamChatAll()
println("teamChatAll size: ${teamChatAll.size}")

// 활성화된 TeamChat 필터링하기
val teamChat = teamChatAll.stream()
    .filter { it.active }
    .toList()[0]
```

## Write Message

`Message`는 `block`하고 `option`으로 이뤄져 있습니다.
`block`은 Message의 내용입니다. 형태는 `TEXT`, `CODE`, `BULLETS`로 이뤄져 있습니다.
`option`은 전송되는 Message에 Manager 알람을 넣는 등의 옵션을 줄 수 있습니다.

`Message` consists of `block` and `option`.
the `block` is the content of the message. The form consists of `TEXT`, `CODE`, and `BULLETS`.
the `option` can give you options such as putting a Manager alarm in the message that is sent.

```kotlin
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
```

![Untitled](README/Untitled%203.png)

## Send Message

생성한 Message는 Manager에게 직접 보내거나, TeamChat으로 보낼 수 있습니다.
You can send the generated message directly to the Manager or to TeamChat.

### Send Message to Manager private room

```kotlin
// 모든 매니저에게 보내기
channel.sendAnnouncementAll(bot = bot, message = message)

// 특정 매니저에게 보내기
channel.sendAnnouncement(manager = manager, bot = bot, message = message)
```

### Send Message to TeamChat

```kotlin
//특정 TeamChat으로 메시지 보내기
channel.sendTeamChat(teamChat = teamChat, bot = bot, message = message)
```

## Webhook

여러가지 Webhook도 코드를 통해서 설정할 수 있습니다.
You can also set up various webhooks through code.

```kotlin
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

// 등록된 Webhook 가져오기
val webhookAll = channel.getWebhookAll()
println("webhookAll Size: ${webhookAll.size}")

for (webhook in webhookAll) {

    if (webhook.name == "TestCode") {
        // Webhook 삭제하기
        val isDelete = channel.deleteWebhook(webhook)
        println("delete WebHook: $isDelete")
    }
}
```

# Issues

---

1. Gradle을 통해서 Build 하니 Pom.xml이 .jar파일에 같이 안 묶여져서 결과적으로 사용자가 dependency를 여러개 받아야하는 문제가 발생하였습니다.
    
    <aside>
    📌 혹시라도 의존성 문제가 생기신분은 이슈 남겨주세요.
    
    </aside>
    
2. 전송 실패시 Exception 프로세스가 아직은 제대로 정립되진 않았습니다.
Open API로 여러 프로젝트를 진행하며 아이디어를 얻은 후에 진행할 생각입니다.
3. 이 프로젝트는 아직 모든 Channel Talk Open API를 커버하진 못합니다.
남아있는 API까지 커버할 수 있도록 도와주세요. ㅠ_ㅠ
4. 혹시 라이브러리에 버그 또는 아이디어가 있다면 언제든 Github Issue를 열어주세요
5. Http 통신에 있어서 [`Spring WebClient`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html)를 사용하지만 라이브러리에서는 비동기 처리를 지원하지 않습니다. 
`WebClient`에 의존하지 않으면서 비동기 처리에 대한 아이디어가 있으면 편하게 언제든 연락주세요
