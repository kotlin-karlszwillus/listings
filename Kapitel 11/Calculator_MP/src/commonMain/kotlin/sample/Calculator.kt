package sample

expect class Calculator {
    val runtime: CalculatorRuntimeDelegate

    fun calculate(): Unit
    fun version(): String
}