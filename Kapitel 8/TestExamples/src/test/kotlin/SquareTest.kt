package com.kmb.kotlin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SquareTest {

    @Test
    fun testRegularSquares() {
        assertEquals(0, square(0))
        assertEquals(4, square(2))
    }
}

