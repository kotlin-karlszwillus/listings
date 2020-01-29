package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.unary

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 23.06.16.
 */
class ASTArithmeticNegation(operand: ASTExpression) : ASTUnaryOperator(operand) {

    override fun evaluate(runtime: Runtime): Value {
        try {
            val evaluatedValue = this.operand.evaluate(runtime)
            return evaluatedValue.negate()
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString() = "(-${this.operand})"
}
