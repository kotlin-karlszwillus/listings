package com.kmb.kotlin

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class ParamatrizedSquareTest {

    @ParameterizedTest
    @ValueSource(ints = intArrayOf(1, 2, 3))
    fun testRegularSquares(value: Int) {
        assertEquals(value * value, square(value))
    }
}

