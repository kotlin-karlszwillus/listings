package sample

import com.openpromos.jni.nrexpressionlib.Interpreter
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.dom.createElement

actual class Calculator {
    actual val runtime = CalculatorRuntimeDelegate()

    actual fun calculate() {
        val value = document.getElementById("calc") as HTMLInputElement
        value.value.apply {
            writeValue(Interpreter.evaluateExpression(value.value, runtime, "start").toString())
        }
    }

    fun writeValue(s: String) {
        (document.getElementById("result") as HTMLDivElement).innerHTML = s
    }

    actual fun version() = "JS 1.0.0-Snapshot"
}