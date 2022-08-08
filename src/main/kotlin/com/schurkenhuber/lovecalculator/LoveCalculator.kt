package com.schurkenhuber.lovecalculator

private const val CONNECTING_VERB = "LOVES"

fun calculateLoveScore(leftHandSide: String, rightHandSide: String): Int {
    val letterFrequency = computeLetterFrequency(leftHandSide, rightHandSide)
    return addLetterFrequencyVector(letterFrequency)
}

private fun computeLetterFrequency(leftHandSide: String, rightHandSide: String): List<Int> {
    val input = "$leftHandSide $CONNECTING_VERB $rightHandSide".uppercase().trim()
    val characterIndices = mutableMapOf<Char, Int>()
    val frequencies = mutableListOf<Int>()
    var latestIndex = 0

    for (character in input) {
        if (character != ' ') {
            if (!characterIndices.containsKey(character)) {
                characterIndices.put(character, latestIndex)
                frequencies.add(latestIndex, 0)
                latestIndex += 1
            }
            val countIndex = characterIndices[character] ?: throw IllegalStateException("The index for the character '$character' has not been registered.")

            ++frequencies[countIndex]
        }
    }

    return frequencies
}

private fun addLetterFrequencyVector(letterFrequencies: List<Int>): Int {
    return when (letterFrequencies.size) {
        2 -> Integer.parseInt("${letterFrequencies[0]}${letterFrequencies[1]}")
        1 -> letterFrequencies[0]
        0 -> 0
        else ->
            if (letterFrequencies.size == 3 && letterFrequencies[0] == 1 && letterFrequencies[1] == 0 && letterFrequencies[2] == 0)
                100
            else {
                val nextLetterFrequencies = mutableListOf<Int>()
                val endIndex = (letterFrequencies.size / 2) + (letterFrequencies.size % 2)
                for (index in letterFrequencies.indices) {
                    if (index < endIndex) {
                        val nextCount =
                            if (letterFrequencies.size % 2 != 0 && index == (endIndex - 1))
                                letterFrequencies[index]
                            else
                                letterFrequencies[index] + letterFrequencies[letterFrequencies.size - index - 1]
                        if (nextCount < 10) {
                            nextLetterFrequencies.add(index, nextCount)
                        } else {
                            nextLetterFrequencies.add(index, nextCount / 10 + nextCount % 10)
                        }
                    }
                }
                addLetterFrequencyVector(nextLetterFrequencies)
            }
    }
}


