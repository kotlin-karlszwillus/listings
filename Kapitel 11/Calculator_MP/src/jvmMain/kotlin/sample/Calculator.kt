package sample

import com.openpromos.jni.nrexpressionlib.Interpreter


actual class Calculator {
    actual val runtime = CalculatorRuntimeDelegate()
    var expression: String = ""

    actual fun version() = "JVM Version 1.0.0"

    actual fun calculate() {
        println(Interpreter.evaluateExpression(expression, runtime, "start").toString())
    }
}

fun main(args: Array<String>) {
    val calc = Calculator()
    println(calc.version())
    calc.expression = args[0]
    println(calc.calculate())
}

