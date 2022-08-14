package com.schurkenhuber.lovecalculator.test.telegrambot.util

import com.schurkenhuber.lovecalculator.telegrambot.trimCommandName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TrimCommandNameTest {
    @Test
    fun trimLoveCommandNameTest() {
        assertEquals("Lukas & The Simpsons", trimCommandName("/love Lukas & The Simpsons", "/love"))
    }

    @Test
    fun trimLoveCommandNameSpaceSeparatedTest() {
        assertEquals("Homer Marge", trimCommandName("/love     Homer Marge    ", "/love"))
    }
}