package com.schurkenhuber.lovecalculator.telegrambot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.webhook
import io.github.cdimascio.dotenv.dotenv
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val dotenv = dotenv()
    println(dotenv.get("TELEGRAM_BOT_WEBHOOK_URL"))

    val bot = bot {
        token = dotenv.get("TELEGRAM_BOT_TOKEN")
        webhook {
            url = dotenv.get("TELEGRAM_BOT_WEBHOOK_URL")
            maxConnections = 10
            allowedUpdates = listOf("message")
        }
        dispatch {
            command("start") {
                bot.sendMessage(
                    chatId = ChatId.fromId(this.message.chat.id),
                    text = "I am the love calculator bot. I can tell you the probability of love between two people based on their names. My sophisticated algorithm guarantees accurate and reproducible results. So why don't you enter your name and the name of your crush."
                )
            }
            command("love") {
                bot.sendMessage(
                    chatId = ChatId.fromId(this.message.chat.id),
                    text = this.message.text ?: "no message"
                )
            }
            text {
                bot.sendMessage(
                    chatId = ChatId.fromId(this.message.chat.id),
                    text = this.message.text ?: "no message"
                )
            }
        }
    }
    bot.startWebhook()

    val environment = applicationEngineEnvironment {
        module {
            routing {
                post("/${dotenv.get("TELEGRAM_BOT_TOKEN")}") {
                    val response = this.call.receiveText()
                    bot.processUpdate(response)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

    embeddedServer(Netty, environment).start(wait = true)
}
