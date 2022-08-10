package com.schurkenhuber.lovecalculator.telegrambot.util

import java.lang.IllegalArgumentException

private const val SEPARATOR_CHARACTER = '&'
private const val FALLBACK_SEPARATOR_CHARACTER = ' '

fun extractNamesFromMessage(message: String): Pair<String, String> {
    val names =
        if (message.contains(SEPARATOR_CHARACTER)) message.split(SEPARATOR_CHARACTER)
        else  message.trim().split(FALLBACK_SEPARATOR_CHARACTER).filter { name -> !name.isEmpty() && !name.isBlank() }
    if (names.size < 2) {
        throw IllegalArgumentException("The input string does not contain two names separated by either '$SEPARATOR_CHARACTER' or '$FALLBACK_SEPARATOR_CHARACTER'")
    }
    return names[0].trim() to names[1].trim()
}
