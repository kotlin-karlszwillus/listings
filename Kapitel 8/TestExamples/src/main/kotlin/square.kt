package com.kmb.kotlin

import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sqrt

val MAX_NUMBER = sqrt(Int.MAX_VALUE.toDouble())-1

fun square(i : Int) = i * i

fun doMath(i: Int, mathFunction: (Int)->Int) = mathFunction(i)

fun isValidInput(i: Int?) = i != null && i != Int.MIN_VALUE && i.absoluteValue < MAX_NUMBER

fun getErrorMessage(argument: String?) = when {
    argument == null -> "Bitte eine Zahl angeben"
    argument.toIntOrNull() == null -> "Ganze Zahlen, bitte"
    !isValidInput(argument.toInt()) -> "Ich rechne nur bis ${sqrt(MAX_NUMBER).roundToInt()}"
    else -> ""
}

fun main (args : Array<String>) {
    val number = args.getOrNull(0)?.toIntOrNull() ?: 0
    println(doMath(number) {it * it })
    /*if (args.isNotEmpty()) {
        for (argument in args) {
            val input = argument.toIntOrNull()
            println(if (isValidInput(input)) square(input!!) else getErrorMessage(argument) )
        }
    }
    else {
        println(getErrorMessage(null))
    }*/
}