package com.schurkenhuber.lovecalculator.test

import com.schurkenhuber.lovecalculator.calculateLoveScore

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LoveCalculatorTest {
    @Test
    fun testLoveScoreComputation() {
        val loveScore = calculateLoveScore("Mary", "James")
        assertEquals(86, loveScore)
    }

    @Test
    fun testCustomExampleAlpha() {
        val loveScore = calculateLoveScore("Lukas", "The Simpsons")
        assertEquals(84, loveScore)
    }

    @Test
    fun testCustomExampleBravo() {
        val loveScore = calculateLoveScore("Homer", "Marge")
        assertEquals(87, loveScore)
    }

    @Test
    fun testCustomExampleCharlie() {
        val loveScore = calculateLoveScore("Kirk", "Luann")
        assertEquals(14, loveScore)
    }

    @Test
    fun testCustomExampleDelta() {
        val loveScore = calculateLoveScore("Stefan", "Anna")
        assertEquals(96, loveScore)
    }

    @Test
    fun testCustomExampleEcho() {
        val loveScore = calculateLoveScore("Martin", "Silke")
        assertEquals(16, loveScore)
    }

    @Test
    fun testCustomExampleFoxtrot() {
        val loveScore = calculateLoveScore("Herbert", "Regina")
        assertEquals(54, loveScore)
    }

    @Test
    fun testCustomExampleGolf() {
        val loveScore = calculateLoveScore("Michael Häupl", "Wein")
        assertEquals(84, loveScore)
    }

    @Test
    fun testCustomExampleHotel() {
        val loveScore = calculateLoveScore("asdf", "yxcv")
        assertEquals(76, loveScore)
    }

    @Test
    fun testCustomExampleLowScore() {
        val loveScore = calculateLoveScore("Manfred Manni", "Viel Arbeit")
        assertEquals(18, loveScore)
    }

    @Test
    fun testCustomExamplePokemon() {
        val loveScore = calculateLoveScore("Wailord", "Goldeen")
        assertEquals(46, loveScore)
    }
}