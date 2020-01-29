package sample

import com.openpromos.jni.nrexpressionlib.Interpreter


actual class Calculator {
    actual val runtime = CalculatorRuntimeDelegate()

    actual fun version() = "Hello from Native"

    actual fun calculate() {}

    fun main(args: Array<String>) {
        val expression = args[0]
        val result = Interpreter.evaluateExpression(expression, runtime, "start").toString()
        println("Hello world $result")
    }
}
