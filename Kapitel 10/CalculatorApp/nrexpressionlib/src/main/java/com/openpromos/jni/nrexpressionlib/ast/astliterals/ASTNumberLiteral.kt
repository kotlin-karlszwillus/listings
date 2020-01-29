package com.openpromos.jni.nrexpressionlib.ast.astliterals

import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTLiteral
import com.openpromos.jni.nrexpressionlib.value.NumberValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 */
class ASTNumberLiteral(fromString: String?) : ASTLiteral() {
    var value: Double = 0.toDouble()

    init {
        try {
            this.value = java.lang.Double.parseDouble(fromString)
        } catch (e: NumberFormatException) {
            throw e
        }

    }

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        return NumberValue(this.value)
    }

    override fun toString(): String {
        val integer = Math.round(this.value).toInt()

        return if (integer.toDouble() == this.value) {
            integer.toString()
        } else this.value.toString()

    }
}
