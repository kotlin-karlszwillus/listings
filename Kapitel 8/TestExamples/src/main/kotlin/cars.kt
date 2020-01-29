package com.kmb.kotlin

data class Car(val make: String, val model: String, var price: Int? = null) {
    val isForSale
        get() = price != null

    fun getMarketingInfo() = "$make $model"
}

fun main(args: Array<String>) {
    val myCar = Car("Ford", "Mondeo")
}