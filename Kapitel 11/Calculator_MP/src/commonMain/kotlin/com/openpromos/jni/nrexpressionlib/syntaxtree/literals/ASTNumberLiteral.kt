package com.openpromos.jni.nrexpressionlib.syntaxtree.literals

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.value.NumberValue
import com.openpromos.jni.nrexpressionlib.value.Value
import kotlin.math.round
import kotlin.math.roundToInt

/**
 * Created by voigt on 22.06.16.
 */
class ASTNumberLiteral(fromString: String?) : ASTLiteral() {
    private val initialValue : String = fromString ?: "null"
    var value = 0.toDouble()

    init {
        try {
            fromString?.let{ value = fromString.toDouble() }
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Bad value: $initialValue")
        }
    }

    override fun evaluate(runtime: Runtime): Value {
        return NumberValue(this.value)
    }

    override fun toString(): String {
        val roundedValue =  round(this.value)

        return if (roundedValue == this.value) {
            // prevents Integers from being printed as 2.00
            roundedValue.roundToInt().toString()
        } else this.value.toString()

    }
}
