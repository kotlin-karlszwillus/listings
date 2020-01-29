package com.kmb.kotlin

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CarTest {

    lateinit var myCar: Car

    @BeforeEach
    fun setup() {
        println("setup")
        myCar = Car("Ford", "Mondeo")
    }

    @Test
    fun testMarketingInfo() {
        println("testMarketingInfo")
        assertEquals("Ford Mondeo", myCar.getMarketingInfo())
        assertEquals(false, myCar.isForSale)
    }

    @Test
    fun testExchangingCar() {
        println("testExchangingCar")
        myCar = Car("Mercedes", "Sprinter", 17000)
        assertEquals("Mercedes Sprinter", myCar.getMarketingInfo())
        assertEquals(true, myCar.isForSale)
    }

    @Test
    fun testPutOnSale() {
        println("testPutOnSale")
        myCar.price = 10000
        assertEquals("Ford Mondeo", myCar.getMarketingInfo())
        assertEquals(true, myCar.isForSale)
        assertEquals(10000, myCar.price)
    }

    @Test
    fun testPriceIsCorrect() {
        println("testPriceIsCorrect $myCar")
        assertEquals(10000, myCar.price)
    }
}

