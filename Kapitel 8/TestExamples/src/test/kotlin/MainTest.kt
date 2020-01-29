package com.kmb.kotlin

import org.junit.jupiter.api.Test
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class SquareTests {

    @Test
    fun testValidInput() {
        assertFalse(isValidInput(null))
        assertFalse(isValidInput(Int.MAX_VALUE))
        assertFalse(isValidInput(Int.MIN_VALUE))
        assertFalse(isValidInput(-2147483647))
        assertTrue(isValidInput(0))
        assertTrue(isValidInput(10))
        assertTrue(isValidInput(-10))
        assertTrue { isValidInput(MAX_NUMBER.roundToInt()-1) }
    }

    // In real life these would be localized and not hardcoded
    @Test
    fun testGetErrorMessage() {
        assertEquals("Bitte eine Zahl angeben", getErrorMessage(null))
        assertEquals("Ganze Zahlen, bitte", getErrorMessage("Hans"))
        assertEquals("Ich rechne nur bis ${sqrt(MAX_NUMBER).roundToInt()}", getErrorMessage("12547657"))
        assertEquals("", getErrorMessage("5"))
    }
}
