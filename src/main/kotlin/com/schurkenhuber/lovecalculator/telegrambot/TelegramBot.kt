package com.schurkenhuber.lovecalculator.telegrambot

import com.elbekd.bot.Bot
import com.elbekd.bot.model.toChatId
import com.elbekd.bot.server
import com.elbekd.bot.types.Message
import com.elbekd.bot.util.SendingFile
import com.schurkenhuber.lovecalculator.calculateLoveScore
import com.schurkenhuber.lovecalculator.telegrambot.util.extractNamesFromMessage
import io.github.cdimascio.dotenv.dotenv
import java.io.File
import java.io.FileNotFoundException
import kotlin.io.path.outputStream

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
        val (photoFilename, line, imageType) = selectGIFFilenameAndLine(loveScore)
        bot.sendMessage(message.chat.id.toChatId(), "My highly advanced algorithm computed a love score of $loveScore for $leftHandSide and $rightHandSide.")
        val photoPath = "img/$photoFilename"
        if (imageType == ImageType.GIF) {
            bot.sendVideo(message.chat.id.toChatId(), video = SendingFile(loadResourceFile(photoPath)))
        } else if (imageType == ImageType.PHOTO) {
            bot.sendPhoto(message.chat.id.toChatId(), photo = SendingFile(loadResourceFile(photoPath)))
        }
        bot.sendMessage(message.chat.id.toChatId(), line)
    } catch (exception: Throwable) {
        sendUsageMessage(bot, message)
    }
}

fun selectGIFFilenameAndLine(loveScore: Int): Triple<String, String, ImageType> =
    when (loveScore) {
        in 0..15 -> Triple("molemanKiss.gif", "Nobody's gay for Moleman...", ImageType.GIF)
        in 16..30 -> Triple("guessWhoLikesYou.gif", "If you're together already, we predict a breakup in 10, 9, 8... You get the picture. If you're not together, run away and stay clear. Sorry, but this isn't going to work.", ImageType.GIF)
        in 31..45 -> Triple("theDud.gif", "You got the dud!", ImageType.GIF)
        in 46..60 -> Triple("iChooseYou.gif", "Although our sophisticated algorithms say that it won't work out, why not give it a shot? We're sure you can beat probability.", ImageType.GIF)
        in 61..70 -> Triple("willy.gif", "Lead on!", ImageType.GIF)
        in 71..90 -> Triple("colonelHomer.gif", "Marriage is a lot like an orange. First you have the skin, then the sweet, sweet innards.", ImageType.GIF)
        in 91..100 -> Triple("aerosmithLoveTester.jpg", "Wow. This is rare. This is a match made in heaven. Go make out, NOW.", ImageType.PHOTO)
        else -> throw IllegalArgumentException("The love score must be between 0% and 100%.")
    }

fun loadResourceFile(path: String): File {
    val temporaryFile = kotlin.io.path.createTempFile()
    val inputStream = object {}.javaClass.classLoader.getResourceAsStream(path)
    inputStream.use { input ->
        temporaryFile.outputStream().use { output ->
            input?.copyTo(output) ?: throw FileNotFoundException("The file $path does not exist.")
        }
    }
    return temporaryFile.toFile()
}

suspend fun sendUsageMessage(bot: Bot, message: Message) {
    bot.sendMessage(message.chat.id.toChatId(), "Please specify the names of the crushes separated by either an ampersand character or a space, e.g. Homer & Marge.")
}

fun trimCommandName(text: String, commandName: String) =
    text.substringAfter(commandName).trim()
