package com.schurkenhuber.lovecalculator.telegrambot

import com.elbekd.bot.Bot
import com.elbekd.bot.model.toChatId
import com.elbekd.bot.server
import com.elbekd.bot.types.Message
import com.schurkenhuber.lovecalculator.calculateLoveScore
import com.schurkenhuber.lovecalculator.telegrambot.util.extractNamesFromMessage
import io.github.cdimascio.dotenv.dotenv

private const val COMMAND_START = "/start"
private const val COMMAND_LOVE = "/love"

fun main() {
    val dotenv = dotenv()
    // val bot = Bot.createPolling(dotenv.get("TELEGRAM_BOT_TOKEN"))
    val bot = Bot.createWebhook(dotenv.get("TELEGRAM_BOT_TOKEN"), dotenv.get("TELEGRAM_USERNAME")) {
        this.url = dotenv.get("TELEGRAM_BOT_WEBHOOK_URL")
        this.server {
            this.host = dotenv.get("TELEGRAM_BOT_LISTENING_ADDRESS")
            this.port = Integer.parseInt(dotenv.get("TELEGRAM_BOT_LISTENING_PORT"))
        }
    }
    bot.onCommand(COMMAND_START) { (message, _) ->
        bot.sendMessage(message.chat.id.toChatId(), "Hello, I am the love calculator bot. I can tell you the probability of love between two people based on their names. My sophisticated algorithm guarantees accurate and reproducible results. So why don't you enter your name and the name of your crush.")
    }
    bot.onCommand(COMMAND_LOVE) { (message, _) ->
        val rawInput = message.text
        if (rawInput != null) {
            val namesCombined = trimCommandName(rawInput, COMMAND_LOVE)
            handleLoveScoreComputation(bot, message, namesCombined)
        } else {
            sendUsageMessage(bot, message)
        }
    }
    bot.onMessage { message ->
        val namesCombined = message.text
        if (namesCombined != null) {
            handleLoveScoreComputation(bot, message, namesCombined)
        } else {
            sendUsageMessage(bot, message)
        }
    }
    bot.start()
}

suspend fun handleLoveScoreComputation(bot: Bot, message: Message, namesCombined: String) {
    try {
        val (leftHandSide, rightHandSide) = extractNamesFromMessage(namesCombined)
        val loveScore = calculateLoveScore(leftHandSide, rightHandSide)
        bot.sendMessage(message.chat.id.toChatId(), "My highly advanced algorithm computed a love score of $loveScore for $leftHandSide and $rightHandSide.")
    } catch (exception: Throwable) {
        sendUsageMessage(bot, message)
    }
}

suspend fun sendUsageMessage(bot: Bot, message: Message) {
    bot.sendMessage(message.chat.id.toChatId(), "Please specify the names of the crushes separated by either an ampersand character or a space, e.g. Homer & Marge.")
}

fun trimCommandName(text: String, commandName: String) =
    text.substringAfter(commandName).trim()
