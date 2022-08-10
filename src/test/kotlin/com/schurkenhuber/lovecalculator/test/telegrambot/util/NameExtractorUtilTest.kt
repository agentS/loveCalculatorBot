package com.schurkenhuber.lovecalculator.test.telegrambot.util

import com.schurkenhuber.lovecalculator.telegrambot.util.extractNamesFromMessage

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class NameExtractorUtilTest {
    @Test
    fun testExtractNamesWithSeparator() {
        val (leftHandSide, rightHandSide) = extractNamesFromMessage(" Lukas & The Simpsons ")
        assertEquals("Lukas", leftHandSide)
        assertEquals("The Simpsons", rightHandSide)
    }

    @Test
    fun testExtractNamesWithDefaultSeparator() {
        val (leftHandSide, rightHandSide) = extractNamesFromMessage("Martin Silke")
        assertEquals("Martin", leftHandSide)
        assertEquals("Silke", rightHandSide)
    }

    @Test
    fun testExtractNamesWithDefaultSeparatorAndBlankPadding() {
        val (leftHandSide, rightHandSide) = extractNamesFromMessage("    Stefan Anna   ")
        assertEquals("Stefan", leftHandSide)
        assertEquals("Anna", rightHandSide)
    }

    @Test
    fun testExtractNamesWithDefaultSeparatorAndExtraBlankPadding() {
        val (leftHandSide, rightHandSide) = extractNamesFromMessage("    Stefan      Anna   ")
        assertEquals("Stefan", leftHandSide)
        assertEquals("Anna", rightHandSide)
    }

    @Test
    fun testExtractOnlyOneName() {
        assertThrows<IllegalArgumentException> {
            val (leftHandSide, rightHandSide) = extractNamesFromMessage("Burns")
        }
    }
}