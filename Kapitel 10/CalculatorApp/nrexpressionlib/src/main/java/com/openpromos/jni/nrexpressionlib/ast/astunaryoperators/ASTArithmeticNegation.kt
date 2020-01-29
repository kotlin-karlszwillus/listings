package com.openpromos.jni.nrexpressionlib.ast.astunaryoperators

import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTUnaryOperator
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 23.06.16.
 */
class ASTArithmeticNegation(operand: ASTExpression) : ASTUnaryOperator(operand) {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        try {
            val evaluatedValue = this.operand.evaluate(runtime)
            return evaluatedValue.negate()
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return "(-" + this.operand.toString() + ")"
    }
}
